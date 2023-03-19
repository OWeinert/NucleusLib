package piggo.nucleuslib.api.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import piggo.nucleuslib.api.resource.NucleusResourceReloader;

import java.lang.reflect.Type;

public final class NucleusResourceReloaderEvents {

    public static final Event<ResourceTypeLoaded> RESOURCE_TYPE_LOADED = EventFactory.createArrayBacked(ResourceTypeLoaded.class, callbacks -> (resourceReloader, type) -> {
        for(ResourceTypeLoaded callback : callbacks) {
            callback.onResourceTypeLoaded(resourceReloader, type);
        }
    });

    public static final Event<AllResourcesLoaded> ALL_RESOURCES_LOADED = EventFactory.createArrayBacked(AllResourcesLoaded.class, callbacks -> () -> {
       for(AllResourcesLoaded callback : callbacks) {
           callback.onAllResourcesLoaded();
       }
    });

    @FunctionalInterface
    public interface ResourceTypeLoaded {
        void onResourceTypeLoaded(NucleusResourceReloader<?> resourceReloader, Type type);
    }

    @FunctionalInterface
    public interface AllResourcesLoaded {
        void onAllResourcesLoaded();
    }
}
