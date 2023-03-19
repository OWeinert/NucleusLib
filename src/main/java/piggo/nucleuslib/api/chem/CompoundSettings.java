package piggo.nucleuslib.api.chem;

import piggo.nucleuslib.api.nomenclature.Formula;

public class CompoundSettings extends ChemicalSettings {

    private Formula formula;

    public CompoundSettings(String name) {
        super(name);
    }

    public CompoundSettings AddFormula(Formula formula) {
        this.formula = formula;
        return this;
    }

    public Formula getFormula() {
        return formula;
    }
}
