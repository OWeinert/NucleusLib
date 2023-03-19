package piggo.nucleuslib.api.chem;

import com.google.gson.annotations.SerializedName;

public enum MatterState {

    @SerializedName("solid")
    SOLID,
    @SerializedName("liquid")
    LIQUID,
    @SerializedName("gas")
    GAS,
    @SerializedName("plasma")
    PLASMA
}
