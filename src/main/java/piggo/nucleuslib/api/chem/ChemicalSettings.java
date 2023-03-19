package piggo.nucleuslib.api.chem;

public class ChemicalSettings {

    private String name = "";
    private String description = "";
    private MatterState matterState = MatterState.SOLID;
    private float molecularWeight = 0;
    private boolean isRadioactive = false;
    private boolean isMetallic = false;

    public ChemicalSettings(String name) {
        this.name = name;
    }

    public ChemicalSettings AddDescription(String description) {
        this.description = description;
        return this;
    }

    public ChemicalSettings SetMatterState(MatterState matterState) {
        this.matterState = matterState;
        return this;
    }

    public ChemicalSettings SetMolecularWeight(float molecularWeight) {
        this.molecularWeight = molecularWeight;
        return this;
    }

    public ChemicalSettings SetRadioactive(boolean isRadioactive) {
        this.isRadioactive = isRadioactive;
        return this;
    }

    public ChemicalSettings SetMetallic(boolean isMetallic) {
        this.isMetallic = isMetallic;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public MatterState getMatterState() {
        return matterState;
    }

    public float getMolecularWeight() {
        return molecularWeight;
    }

    public boolean isRadioactive() {
        return isRadioactive;
    }

    public boolean isMetallic() {
        return isMetallic;
    }
}
