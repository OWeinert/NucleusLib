package piggo.nucleuslib.api.chem.subatomic;

import piggo.nucleuslib.api.chem.subatomic.orbitals.Orbital;
import piggo.nucleuslib.api.chem.subatomic.orbitals.OrbitalIdentifier;

import java.util.ArrayList;

public class SubAtomicConfiguration {

    private final int PROTONS;
    private final int NEUTRONS;
    private final ArrayList<Orbital> ORBITALS;

    public SubAtomicConfiguration(int protons, int neutrons, ArrayList<Orbital> orbitals) {
        this.PROTONS = protons;
        this.NEUTRONS = neutrons;
        this.ORBITALS = orbitals;
    }

    public SubAtomicConfiguration(int protons, int neutrons) {
        this(protons, neutrons, new ArrayList<Orbital>());
    }

    public void addOrbital(Orbital orbital) {
        this.ORBITALS.add(orbital);
    }

    public Orbital getOrbital(OrbitalIdentifier identifier, int index) {
        return ORBITALS.stream()
                .filter(orbital -> orbital.getIdentifier() == identifier && orbital.getIndex() == index)
                .findFirst()
                .orElse(null);
    }

    public int getProtons() {
        return PROTONS;
    }

    public int getNeutrons() {
        return NEUTRONS;
    }
}
