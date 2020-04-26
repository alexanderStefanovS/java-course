package bg.sofia.uni.fmi.mjt.authroship.detection.structures;

import bg.sofia.uni.fmi.mjt.authroship.detection.LinguisticSignature;

public class AuthorsSignaturesStruct {

    private String name;
    private LinguisticSignature linguisticSignature;

    public AuthorsSignaturesStruct(String name, LinguisticSignature linguisticSignature) {
        this.name = name;
        this.linguisticSignature = linguisticSignature;
    }

    public String getName() {
        return name;
    }

    public LinguisticSignature getLinguisticSignature() {
        return linguisticSignature;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLinguisticSignature(LinguisticSignature linguisticSignature) {
        this.linguisticSignature = linguisticSignature;
    }
}
