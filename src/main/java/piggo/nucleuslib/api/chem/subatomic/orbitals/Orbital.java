package piggo.nucleuslib.api.chem.subatomic.orbitals;

public abstract class Orbital {

    private OrbitalIdentifier identifier;
    private int index;

    private int electrons;

    public Orbital(OrbitalIdentifier identifier, int index) {
        this.identifier = identifier;
        this.index = index;
    }

    public OrbitalIdentifier getIdentifier() {
        return identifier;
    }

    public int getIndex() {
        return index;
    }

    public void setElectrons(int electrons) {
        this.electrons = electrons;
    }

    public int getElectrons() {
        return electrons;
    }
}
