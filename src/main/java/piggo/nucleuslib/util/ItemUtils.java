package piggo.nucleuslib.util;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ItemUtils {

    public static Item getItem(Identifier identifier) {
        return Registries.ITEM.get(identifier);
    }
}
