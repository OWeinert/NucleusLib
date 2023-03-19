package piggo.nucleuslib.api.chem;

import com.google.gson.*;
import piggo.nucleuslib.api.item.ChemicalItem;
import piggo.nucleuslib.api.nomenclature.Formula;

import java.lang.reflect.Type;

public class Compound extends Chemical{
    public final Formula FORMULA;

    private final ChemicalItem ITEM = new ChemicalItem(this);

    public Compound(String name, String description, String formula, MatterState matterState, Float molecularWeight, Boolean isRadioactive, Boolean isMetallic) {
        this(name, description, Formula.fromString(formula), matterState, molecularWeight, isRadioactive, isMetallic);
    }

    public Compound(String name, String description, Formula formula, MatterState matterState, Float molecularWeight, Boolean isRadioactive, Boolean isMetallic) {
        super(name, description, matterState, molecularWeight, isRadioactive, isMetallic);
        this.FORMULA = formula;
    }

    public Compound(CompoundSettings settings) {
        this(settings.getName(), settings.getDescription(), settings.getFormula(), settings.getMatterState(), settings.getMolecularWeight(), settings.isRadioactive(), settings.isMetallic());
    }

    @Override
    public ChemicalItem getItem() {
        return ITEM;
    }

    public static class Deserializer implements JsonDeserializer<Compound> {

        @Override
        public Compound deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement name = jsonObject.get("name");
            JsonElement formula = jsonObject.get("formula");
            JsonElement description = jsonObject.get("description");
            JsonElement matterState = jsonObject.get("matterState");
            JsonElement molecularWeight = jsonObject.get("molecularWeight");
            JsonElement radioactive = jsonObject.get("radioactive");
            JsonElement metallic = jsonObject.get("metallic");

            return new Compound(
                    name == null ? "" : name.getAsString(),
                    description == null ? "" : description.getAsString(),
                    formula == null ? "" : formula.getAsString(),
                    matterState == null ? MatterState.SOLID : MatterState.valueOf(matterState.getAsString().toUpperCase()),
                    molecularWeight == null ? 0.0f : molecularWeight.getAsFloat(),
                    radioactive != null && radioactive.getAsBoolean(),
                    metallic != null && metallic.getAsBoolean());
        }
    }
}
