package piggo.nucleuslib.api.chem;

import com.google.gson.*;
import piggo.nucleuslib.api.chem.subatomic.SubAtomicConfiguration;
import piggo.nucleuslib.api.item.ChemicalItem;

import java.lang.reflect.Type;

public class Element extends Chemical {

    public final String SYMBOL;
    public final int ATOMIC_NUMBER;
    public final int GROUP;
    public final int PERIOD;

    // TODO: fully implement SubAtomicConfiguration
    //public SubAtomicConfiguration subAtomicConfiguration;

    private final ChemicalItem ITEM = new ChemicalItem(this);

    public Element(String name, String symbol, Integer atomicNumber, Integer group, Integer period, MatterState matterState,
                   Float molecularWeight, Boolean isMetallic) {
        this(name, symbol, "", atomicNumber, group, period, matterState, molecularWeight, false, isMetallic);
    }

    public Element(String name, String symbol, String description, Integer atomicNumber, Integer group, Integer period,
                   MatterState matterState, Float molecularWeight, Boolean isRadioactive, Boolean isMetallic) {
        super(name, description, matterState, molecularWeight, isRadioactive, isMetallic);
        this.SYMBOL = symbol;
        this.ATOMIC_NUMBER = atomicNumber;
        this.GROUP = group;
        this.PERIOD = period;

    }

    public Element(ElementSettings settings) {
        this(settings.getName(), settings.getSymbol(), settings.getDescription(), settings.getAtomicNumber(), settings.getGroup(), settings.getPeriod(),
                settings.getMatterState(), settings.getMolecularWeight(), settings.isRadioactive(), settings.isMetallic());
    }

    @Override
    public ChemicalItem getItem() {
        return ITEM;
    }

    public static class Deserializer implements JsonDeserializer<Element> {

        @Override
        public Element deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement name = jsonObject.get("name");
            JsonElement symbol = jsonObject.get("symbol");
            JsonElement description = jsonObject.get("description");
            JsonElement matterState = jsonObject.get("matterState");
            JsonElement atomicNumber = jsonObject.get("atomicNumber");
            JsonElement group = jsonObject.get("group");
            JsonElement period = jsonObject.get("period");
            JsonElement molecularWeight = jsonObject.get("molecularWeight");
            JsonElement radioactive = jsonObject.get("radioactive");
            JsonElement metallic = jsonObject.get("metallic");

            return new Element(
                    name == null ? "" : name.getAsString(),
                    symbol == null? "" : symbol.getAsString(),
                    description == null ? "" : description.getAsString(),
                    atomicNumber == null ? -1 : atomicNumber.getAsInt(),
                    group == null ? -1 : group.getAsInt(),
                    period == null ? -1 : period.getAsInt(),
                    matterState == null ? MatterState.SOLID : MatterState.valueOf(matterState.getAsString().toUpperCase()),
                    molecularWeight == null ? 0.0f : molecularWeight.getAsFloat(),
                    radioactive != null && radioactive.getAsBoolean(),
                    metallic != null && metallic.getAsBoolean());
        }
    }
}
