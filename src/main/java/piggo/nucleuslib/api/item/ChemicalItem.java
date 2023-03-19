package piggo.nucleuslib.api.item;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import piggo.nucleuslib.NucleusLib;
import piggo.nucleuslib.api.chem.Chemical;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ChemicalItem extends Item {

    private final Chemical CHEMICAL;
    private final ImmutableList<Text> EXTENDED_TOOLTIP;

    public ChemicalItem(Chemical chemical) {
        super(new FabricItemSettings());
        this.CHEMICAL = chemical;
        EXTENDED_TOOLTIP = createTooltip();
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if(InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.addAll(EXTENDED_TOOLTIP);
        }
        else {
            tooltip.add(Text.translatable("internal.nucleuslib.tooltip_base", "L Shift"));
        }
    }

    public Chemical getChemical() {
        return CHEMICAL;
    }

    private ImmutableList<Text> createTooltip() {
        Field[] chemicalClassFields = CHEMICAL.getClass().getFields();
        List<Text> tooltip = new ArrayList<>();
        for (Field field : chemicalClassFields) {
            Text translatableFieldName = Text.translatable("internal.nucleuslib.tooltip_field." + field.getName().toLowerCase());
            try {
                if(field.getType().equals(boolean.class) && field.getBoolean(CHEMICAL)) {
                    tooltip.add(translatableFieldName);
                }
                else if(!field.getName().equalsIgnoreCase("name")){
                    String fieldStr = field.get(CHEMICAL) != null ? field.get(CHEMICAL).toString() : "";
                    tooltip.add(Text.literal(translatableFieldName.getString() + " : " + fieldStr));
                }
            } catch (IllegalAccessException e) {
                NucleusLib.LOGGER.error("Error while accessing field", e);
                throw new RuntimeException(e);
            }
        }
        return ImmutableList.copyOf(tooltip);
    }
}
