package piggo.nucleuslib.api.resource;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Nullable;
import piggo.nucleuslib.NucleusLib;
import piggo.nucleuslib.api.chem.Chemical;
import piggo.nucleuslib.api.chem.Compound;
import piggo.nucleuslib.api.chem.Element;
import piggo.nucleuslib.mixin.SimpleRegistryAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

public final class NucleusResourceReloader<T extends Chemical> extends SinglePreparationResourceReloader<Map<Identifier, T>> implements IdentifiableResourceReloadListener {

    public static final NucleusResourceReloader<Element> ELEMENTS_INSTANCE = new NucleusResourceReloaderBuilder<>(Element.class)
            .build();
    public static final NucleusResourceReloader<Compound> COMPOUNDS_INSTANCE = new NucleusResourceReloaderBuilder<>(Compound.class)
            .withDependencies(new Identifier(NucleusLib.MODID, "elements"))
            .build();

    private final Gson GSON;
    public final String STARTING_DIRECTORY;
    public final Class<T> TYPE;
    private final Collection<Identifier> FABRIC_DEPENDENCIES;
    private Map<Identifier, T> loadedData;
    private boolean finished = false;

    public NucleusResourceReloader(Class<T> type, @Nullable Function<GsonBuilder, GsonBuilder> gsonBuilderInject, Collection<Identifier> fabricDependencies) {
        this.TYPE = type;
        STARTING_DIRECTORY = type.getSimpleName().toLowerCase() + "s";
        GsonBuilder gsonBuilder = (new GsonBuilder())
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .registerTypeAdapter(Element.class, new Element.Deserializer())
                .registerTypeAdapter(Compound.class, new Compound.Deserializer());
        if(gsonBuilderInject != null) {
            gsonBuilder = gsonBuilderInject.apply(gsonBuilder);
        }
        GSON = gsonBuilder.create();
        FABRIC_DEPENDENCIES = fabricDependencies;
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(NucleusLib.MODID, STARTING_DIRECTORY);
    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return FABRIC_DEPENDENCIES != null ? FABRIC_DEPENDENCIES : IdentifiableResourceReloadListener.super.getFabricDependencies();
    }

    @Override
    protected Map<Identifier, T> prepare(ResourceManager manager, Profiler profiler) {
        Map<Identifier, T> dataMap = new HashMap<>();
        Map<Identifier, List<Resource>> resources = manager.findAllResources(STARTING_DIRECTORY, id -> true);

        for(Map.Entry<Identifier, List<Resource>> entry : resources.entrySet()) {
            Identifier identifier = entry.getKey();
            try{
                for(Resource res : entry.getValue()) {
                    try(InputStream stream = res.getInputStream()) {
                        try{
                            Scanner s = new Scanner(stream).useDelimiter("\\A");
                            String jsonString = s.hasNext() ? s.next() : "";
                            unfreezeItemRegistry();
                            T data = GSON.fromJson(jsonString, TYPE);
                            dataMap.put(identifier, data);
                        }
                        catch(Exception e) {
                            NucleusLib.LOGGER.error("Error occurred while parsing " + res + ".json! ", e);
                            e.printStackTrace();
                        }
                    } catch(IOException e) {
                        NucleusLib.LOGGER.error("IOException while loading " + identifier.getPath(), e);
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e) {
                NucleusLib.LOGGER.error("Error while loading data with identifier: " + identifier);
                e.printStackTrace();
            }
        }
        loadedData = dataMap;
        finished = true;
        NucleusLib.LOGGER.info("Loaded " + dataMap.size() + " " + STARTING_DIRECTORY);
        return dataMap;
    }

    @Override
    protected void apply(Map<Identifier, T> prepared, ResourceManager manager, Profiler profiler) {
        unfreezeItemRegistry();
        for(Map.Entry<Identifier, T> entry : prepared.entrySet()) {
            CreateAndRegisterContent(entry.getValue(), FilenameUtils.getBaseName(entry.getKey().getPath()));
        }
        refreezeItemRegistry();
    }

    public boolean finished() {
        return finished;
    }

    public Map<Identifier, T> getLoadedData() {
        return ImmutableMap.copyOf(loadedData);
    }

    private void CreateAndRegisterContent(T data, String name) {
        Registry.register(Registries.ITEM, new Identifier(NucleusLib.MODID, name), data.getItem());
        ItemGroup itemGroup = data instanceof Element ? NucleusLib.ELEMENTS_GROUP : NucleusLib.COMPOUNDS_GROUP;
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> content.add(data.getItem()));
    }

    private void unfreezeItemRegistry() {
        if(((SimpleRegistryAccessor<?>) Registries.ITEM).getFrozen()) {
            try {
                ((SimpleRegistryAccessor<?>)Registries.ITEM).setFrozen(false);
            } catch(Exception e) {
                NucleusLib.LOGGER.error("Error while unfreezing Registry!", e);
                e.printStackTrace();
            }
        }
    }

    private void refreezeItemRegistry() {
        ((SimpleRegistryAccessor<?>)Registries.ITEM).invokeFreeze();
    }
}
