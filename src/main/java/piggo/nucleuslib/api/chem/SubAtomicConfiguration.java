package piggo.nucleuslib.api.chem;

import com.google.gson.*;

import java.lang.reflect.Type;

public class SubAtomicConfiguration {

    private final int PROTONS;
    private final int NEUTRONS;
    private final int ELECTRONS;

    public SubAtomicConfiguration(int protons, int neutrons, int electrons) {
        this.PROTONS = protons;
        this.NEUTRONS = neutrons;
        this.ELECTRONS = electrons;
    }

    public int getProtons() {
        return PROTONS;
    }

    public int getNeutrons() {
        return NEUTRONS;
    }

    public int getElectrons() {
        return ELECTRONS;
    }
}
