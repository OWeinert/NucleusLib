package piggo.nucleuslib.api.resource;

import com.google.common.collect.ImmutableList;
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
import piggo.nucleuslib.api.events.NucleusResourceReloaderEvents;
import piggo.nucleuslib.util.RegistryUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

public final class NucleusResourceReloader<T extends Chemical> extends SinglePreparationResourceReloader<Map<Identifier, T>> implements IdentifiableResourceReloadListener {

    private static List<NucleusResourceReloader<?>> nucleusResourceReloaders = new ArrayList<>();

    public static final NucleusResourceReloader<Element> ELEMENTS_INSTANCE = new NucleusResourceReloaderBuilder<>(Element.class)
            .AddJsonDeserializer(Element.class, new Element.Deserializer())
            .build();
    public static final NucleusResourceReloader<Compound> COMPOUNDS_INSTANCE = new NucleusResourceReloaderBuilder<>(Compound.class)
            .AddJsonDeserializer(Compound.class, new Compound.Deserializer())
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
        FABRIC_DEPENDENCIES = fabricDependencies;
        
        GsonBuilder gsonBuilder = (new GsonBuilder())
                .setPrettyPrinting()
                .disableHtmlEscaping();
        if(gsonBuilderInject != null) {
            gsonBuilder = gsonBuilderInject.apply(gsonBuilder);
        }
        GSON = gsonBuilder.create();
        
        if(nucleusResourceReloaders == null) {
            nucleusResourceReloaders = new ArrayList<>();
        }
        nucleusResourceReloaders.add(this);
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
        finished = false;
        Map<Identifier, T> dataMap = loadedData != null && loadedData.size() > 0 ? loadedData : new HashMap<>();
        Map<Identifier, List<Resource>> resources = manager.findAllResources(STARTING_DIRECTORY, id -> id.getPath().endsWith(".json"));
        for(Map.Entry<Identifier, List<Resource>> entry : resources.entrySet()) {
            Identifier identifier = entry.getKey();
            try{
                for(Resource res : entry.getValue()) {
                    try(InputStream stream = res.getInputStream()) {
                        try{
                            if(!dataMap.containsKey(identifier)) {
                                Scanner s = new Scanner(stream).useDelimiter("\\A");
                                String jsonString = s.hasNext() ? s.next() : "";
                                RegistryUtils.unfreezeRegistry(Registries.ITEM);
                                T data = GSON.fromJson(jsonString, TYPE);
                                dataMap.putIfAbsent(identifier, data);
                            }
                            else {
                                NucleusLib.LOGGER.warn(identifier + " is already loaded. Skipping...");
                            }
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
        NucleusLib.LOGGER.info("Found " + loadedData.size() + " " + STARTING_DIRECTORY);
        return loadedData;
    }

    @Override
    protected void apply(Map<Identifier, T> prepared, ResourceManager manager, Profiler profiler) {
        for(Map.Entry<Identifier, T> entry : prepared.entrySet()) {
            Identifier identifier = new Identifier(NucleusLib.MODID, FilenameUtils.getBaseName(entry.getKey().getPath()));
            if(!Registries.ITEM.containsId(identifier)) {
                RegistryUtils.unfreezeRegistry(Registries.ITEM);
                CreateAndRegisterContent(entry.getValue(), identifier);
            }
            else {
                NucleusLib.LOGGER.warn(identifier + " is already registered. Skipping...");
            }
        }
        finished = true;
        NucleusLib.LOGGER.info("Loaded and registered " + loadedData.size() + " " + STARTING_DIRECTORY);
        NucleusResourceReloaderEvents.RESOURCE_TYPE_LOADED.invoker().onResourceTypeLoaded(this, TYPE);
    }
    
    public static ImmutableList<NucleusResourceReloader<?>> reloadListeners() {
        return ImmutableList.copyOf(nucleusResourceReloaders);
    }

    public boolean finished() {
        return finished;
    }

    public Map<Identifier, T> getLoadedData() {
        return ImmutableMap.copyOf(loadedData);
    }

    private void CreateAndRegisterContent(T data, Identifier identifier) {
        Registry.register(Registries.ITEM, identifier, data.getItem());
        ItemGroup itemGroup;
        if(data instanceof Element) {
            itemGroup = NucleusLib.ELEMENTS_GROUP;
        }
        else if(data instanceof Compound){
            itemGroup = NucleusLib.COMPOUNDS_GROUP;
        }
        else {
            itemGroup = NucleusLib.MISC_GROUP;
        }
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> content.add(data.getItem()));
    }
}
