package piggo.nucleuslib.api.chem;

import piggo.nucleuslib.api.item.ChemicalItem;

public abstract class Chemical {

    public final String NAME;
    public final String DESCRIPTION;
    public final MatterState MATTER_STATE;
    public final float MOLECULAR_WEIGHT;
    public final boolean RADIOACTIVE;
    public final boolean METALLIC;

    public Chemical(String name, String description, MatterState matterState, float molecularWeight, boolean isRadioactive, boolean isMetallic) {
        this.NAME = name;
        this.DESCRIPTION = description;
        this.MATTER_STATE = matterState;
        this.MOLECULAR_WEIGHT = molecularWeight;
        this.RADIOACTIVE = isRadioactive;
        this.METALLIC = isMetallic;
    }

    public Chemical(ChemicalSettings settings) {
        this(settings.getName(), settings.getDescription(), settings.getMatterState(), settings.getMolecularWeight(), settings.isRadioactive(), settings.isMetallic());
    }

    public abstract ChemicalItem getItem();


}
