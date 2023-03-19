package piggo.nucleuslib.mixin;

import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(SimpleRegistry.class)
public interface SimpleRegistryAccessor<T> {

    @Accessor("frozen")
    boolean getFrozen();

    @Accessor("frozen")
    void setFrozen(boolean frozen);

    @Accessor("intrusiveValueToEntry")
    Map<T, RegistryEntry.Reference<T>> getIntrusiveValueToEntry();

    @Accessor("intrusiveValueToEntry")
    void setIntrusiveValueToEntry(Map<T, RegistryEntry.Reference<T>> intrusiveValueToEntry);

    @SuppressWarnings("UnusedReturnValue")
    @Invoker("freeze")
    Registry<T> invokeFreeze();
}
