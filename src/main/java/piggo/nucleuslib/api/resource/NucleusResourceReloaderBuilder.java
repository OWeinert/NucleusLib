package piggo.nucleuslib.api.resource;

import com.google.gson.ExclusionStrategy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.ReflectionAccessFilter;
import net.minecraft.util.Identifier;
import piggo.nucleuslib.api.chem.Chemical;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class NucleusResourceReloaderBuilder<T extends Chemical> {
    private final Class<T> TYPE;
    private final List<Function<GsonBuilder, GsonBuilder>> GSON_BUILDER_INJECTS;
    private Collection<Identifier> fabricDependencies;

    public NucleusResourceReloaderBuilder(Class<T> type) {
        TYPE = type;
        GSON_BUILDER_INJECTS = new ArrayList<>();
    }

    /**
     * Adds a JsonDeserializer instance to the GsonBuilder allowing for proper Deserialization of the
     * given .json Files to the desired Type.
     * @param type the Type to which is deserialized
     * @param deserializer Instance of the custom JsonDeserializer
     * @return the NucleusResourceReloaderBuilder
     */
    public NucleusResourceReloaderBuilder<T> AddJsonDeserializer(Type type, JsonDeserializer<?> deserializer) {
        GSON_BUILDER_INJECTS.add(gsonBuilder -> gsonBuilder.registerTypeAdapter(type, deserializer));
        return this;
    }

    /**
     * @param filter the ReflectionAccessFilter
     * @return the NucleusResourceReloaderBuilder
     */
    public NucleusResourceReloaderBuilder<T> AddReflectionAccessFilter(ReflectionAccessFilter filter) {
        GSON_BUILDER_INJECTS.add(gsonBuilder -> gsonBuilder.addReflectionAccessFilter(filter));
        return this;
    }

    /**
     * Adds a Gson ExclusionStrategy to the GsonBuilder which allows for the exclusion of specific Fields
     * in the deserialization process.
     * @param strategy the ExclusionStrategy
     * @return the NucleusResourceReloaderBuilder
     */
    public NucleusResourceReloaderBuilder<T> AddDeserializationExclusionStrategy(ExclusionStrategy strategy) {
        GSON_BUILDER_INJECTS.add(gsonBuilder -> gsonBuilder.addDeserializationExclusionStrategy(strategy));
        return this;
    }

    /**
     * Adds Dependencies in form of Identifiers of Fabric based ResourceReloaders to the NucleusResourceReloader.
     * The NucleusResourceReloader will apply it's loaded data only after each Dependency finished it's Resource loading process.
     * @param fabricDependencies the List of Dependencies
     * @return the NucleusResourceReloaderBuilder
     */
    public NucleusResourceReloaderBuilder<T> withDependencies(Identifier... fabricDependencies) {
        this.fabricDependencies = Arrays.asList(fabricDependencies);
        return this;
    }

    /**
     * @return the NucleusResourceReloader
     */
    public NucleusResourceReloader<T> build() {
        Function<GsonBuilder, GsonBuilder> gsonBuilderInject = gsonBuilder -> {
            for(Function<GsonBuilder, GsonBuilder> inject : GSON_BUILDER_INJECTS) {
                inject.apply(gsonBuilder);
            }
            return gsonBuilder;
        };
        return new NucleusResourceReloader<>(TYPE, gsonBuilderInject, fabricDependencies);
    }
}
