package piggo.nucleuslib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import piggo.nucleuslib.api.chem.Chemical;
import piggo.nucleuslib.api.chem.Compound;
import piggo.nucleuslib.api.chem.Element;
import piggo.nucleuslib.api.events.NucleusResourceReloaderEvents;
import piggo.nucleuslib.api.resource.NucleusResourceReloader;
import piggo.nucleuslib.util.ItemUtils;
import piggo.nucleuslib.util.RegistryUtils;

import java.util.Map;

public class NucleusLib implements ModInitializer {

	public static final String MODID = "nucleuslib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final ItemGroup ELEMENTS_GROUP = FabricItemGroup.builder(new Identifier(NucleusLib.MODID, "elements"))
			.icon(() -> new ItemStack(ItemUtils.getItem(new Identifier(NucleusLib.MODID, "hydrogen"))))
			.build();

	public static final ItemGroup COMPOUNDS_GROUP = FabricItemGroup.builder(new Identifier(NucleusLib.MODID, "compounds"))
			.icon(() -> new ItemStack(ItemUtils.getItem(new Identifier(NucleusLib.MODID, "sodium_chloride"))))
			.build();

	public static final ItemGroup MISC_GROUP = FabricItemGroup.builder(new Identifier(NucleusLib.MODID, "misc"))
			.icon(() -> new ItemStack(Items.CLOCK))
			.build();

	@Override
	public void onInitialize() {
		NucleusResourceReloaderEvents.ALL_RESOURCES_LOADED.register(() -> {
			RegistryUtils.freezeRegistry(Registries.ITEM);
		});
		NucleusResourceReloaderEvents.RESOURCE_TYPE_LOADED.register((resourceReloader, type) -> {
			if(NucleusResourceReloader.allFinished()) {
				NucleusResourceReloaderEvents.ALL_RESOURCES_LOADED.invoker().onAllResourcesLoaded();
			}
		});
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(NucleusResourceReloader.ELEMENTS_INSTANCE);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(NucleusResourceReloader.COMPOUNDS_INSTANCE);
	}
}
