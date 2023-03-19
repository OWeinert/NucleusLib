package piggo.nucleuslib.api.chem;

import piggo.nucleuslib.api.chem.subatomic.SubAtomicConfiguration;

public class ElementSettings extends ChemicalSettings {

    private String symbol;
    private int atomicNumber;
    private int group;
    private int period;
    private SubAtomicConfiguration subAtomicConfiguration;

    public ElementSettings(String name) {
        super(name);
    }

    public String getSymbol() {
        return symbol;
    }

    public ElementSettings setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public Integer getAtomicNumber() {
        return atomicNumber;
    }

    public ElementSettings setAtomicNumber(int atomicNumber) {
        this.atomicNumber = atomicNumber;
        return this;
    }

    public Integer getGroup() {
        return group;
    }

    public ElementSettings setGroup(int group) {
        this.group = group;
        return this;
    }

    public Integer getPeriod() {
        return period;
    }

    public ElementSettings setPeriod(int period) {
        this.period = period;
        return this;
    }

    public SubAtomicConfiguration getSubAtomicConfiguration() {
        return subAtomicConfiguration;
    }

    public ElementSettings setSubAtomicConfiguration(SubAtomicConfiguration subAtomicConfiguration) {
        this.subAtomicConfiguration = subAtomicConfiguration;
        return this;
    }
}
