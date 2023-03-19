package piggo.nucleuslib.api.chem.subatomic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import piggo.nucleuslib.api.chem.subatomic.orbitals.Orbital;
import piggo.nucleuslib.api.chem.subatomic.orbitals.OrbitalIdentifier;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SubAtomicConfiguration {

    private int protons;
    private int neutrons;
    private ArrayList<Orbital> orbitals;

    public SubAtomicConfiguration(int protons, int neutrons, ArrayList<Orbital> orbitals) {
        this.protons = protons;
        this.neutrons = neutrons;
        this.orbitals = orbitals;
    }

    public SubAtomicConfiguration(int protons, int neutrons) {
        this(protons, neutrons, new ArrayList<Orbital>());
    }

    public void addOrbital(Orbital orbital) {
        this.orbitals.add(orbital);
    }

    public Orbital getOrbital(OrbitalIdentifier identifier, int index) {
        return orbitals.stream()
                .filter(orbital -> orbital.getIdentifier() == identifier && orbital.getIndex() == index)
                .findFirst()
                .orElse(null);
    }
}
