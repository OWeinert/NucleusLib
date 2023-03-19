package piggo.nucleuslib.util;

import net.minecraft.registry.Registry;
import piggo.nucleuslib.NucleusLib;
import piggo.nucleuslib.mixin.SimpleRegistryAccessor;

public class RegistryUtils {

    public static <T> void unfreezeRegistry(Registry<T> registry) {
        if(((SimpleRegistryAccessor<?>)registry).getFrozen()) {
            try {
                ((SimpleRegistryAccessor<?>)registry).setFrozen(false);
            } catch(Exception e) {
                NucleusLib.LOGGER.error("Error while unfreezing Registry!", e);
                e.printStackTrace();
            }
        }
    }

    public static <T> void freezeRegistry(Registry<T> registry) {
        ((SimpleRegistryAccessor<?>)registry).invokeFreeze();
    }
}
