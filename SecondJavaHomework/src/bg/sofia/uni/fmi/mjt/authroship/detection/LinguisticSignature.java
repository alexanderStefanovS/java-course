package bg.sofia.uni.fmi.mjt.authroship.detection;

import java.util.HashMap;
import java.util.Map;

public class LinguisticSignature {

    private HashMap<FeatureType, Double> features;

    public LinguisticSignature(Map<FeatureType, Double> features) {
        this.features = (HashMap<FeatureType, Double>) features;
    }

    public Map<FeatureType, Double> getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return "LinguisticSignature{" + "features=" + features + '}';
    }
}
