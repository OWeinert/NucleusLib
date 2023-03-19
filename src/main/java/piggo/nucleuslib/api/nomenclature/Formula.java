package piggo.nucleuslib.api.nomenclature;

import piggo.nucleuslib.NucleusLib;
import piggo.nucleuslib.api.chem.Element;
import piggo.nucleuslib.api.resource.NucleusResourceReloader;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formula {

    private final Map<Element, Integer> ELEMENTS;

    public Formula(Map<Element, Integer> elements) {
        ELEMENTS = elements;
    }


    public static Formula of(String formulaString) {
        if(NucleusResourceReloader.ELEMENTS_INSTANCE.finished()) {
            Map<Element, Integer> elements = new HashMap<Element, Integer>();

            Pattern pattern = Pattern.compile("[a-z]{1,2}[1-9]*", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(formulaString);

            for(int i = 0; i < matcher.groupCount(); i++) {
                String group = matcher.group(i);
                String[] splitGroup = group.split("(?<=\\D)(?=\\d)");

                if(NucleusResourceReloader.ELEMENTS_INSTANCE.getLoadedData().entrySet().stream()
                        .anyMatch(entry -> entry.getValue().SYMBOL.equals(splitGroup[0]))) {
                    Element element = NucleusResourceReloader.ELEMENTS_INSTANCE.getLoadedData().entrySet().stream()
                            .filter(entry -> entry.getValue().SYMBOL.equals(splitGroup[0]))
                            .findFirst()
                            .orElse(null)
                            .getValue();
                    int count = Integer.parseInt(splitGroup[1]);
                    elements.put(element, count);
                }
                else {
                    String msg = "Found element symbol does not match any element in the registry! Found: \"" + splitGroup[0] + "\"";
                    NucleusLib.LOGGER.error(msg);
                    throw new IllegalArgumentException(msg);
                }
            }
            return new Formula(elements);
        }
        NucleusLib.LOGGER.error("Elements are not yet loaded! Formula can't be created without a full list of Elements!");
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Element, Integer> entry : this.ELEMENTS.entrySet()) {
            sb.append(entry.getKey().SYMBOL).append(entry.getValue());
        }
        return sb.toString();
    }
}
