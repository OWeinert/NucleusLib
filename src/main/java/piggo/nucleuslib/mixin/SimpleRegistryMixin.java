package piggo.nucleuslib.mixin;

import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.IdentityHashMap;

@Mixin(SimpleRegistry.class)
public class SimpleRegistryMixin<T>{


    @SuppressWarnings("all")
    @Inject(method = "createEntry", at = @At("HEAD"))
    void createEntryInject(T value, CallbackInfoReturnable<RegistryEntry.Reference<T>> cir) {

        if(((SimpleRegistryAccessor<T>)((SimpleRegistry<T>)(Object)this)).getIntrusiveValueToEntry() == null) {
            ((SimpleRegistryAccessor<T>)((SimpleRegistry<T>)(Object)this)).setIntrusiveValueToEntry(new IdentityHashMap<>());
        }
    }
}
