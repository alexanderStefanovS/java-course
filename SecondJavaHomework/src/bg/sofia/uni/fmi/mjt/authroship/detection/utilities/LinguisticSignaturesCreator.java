package bg.sofia.uni.fmi.mjt.authroship.detection.utilities;

import bg.sofia.uni.fmi.mjt.authroship.detection.FeatureType;
import bg.sofia.uni.fmi.mjt.authroship.detection.LinguisticSignature;
import bg.sofia.uni.fmi.mjt.authroship.detection.structures.AuthorsSignaturesStruct;

import java.io.InputStream;
import java.util.*;

public class LinguisticSignaturesCreator {

    private List<AuthorsSignaturesStruct> linguisticSignatures;

    public LinguisticSignaturesCreator() {
        linguisticSignatures = new ArrayList<>();
    }

    public List<AuthorsSignaturesStruct> getLinguisticSignatures() {
        return linguisticSignatures;
    }

    private Map<FeatureType, Double> createFeaturesMap(String[] features) {
        int i = 1;
        Map<FeatureType, Double> featuresMap = new HashMap<>();
        FeatureType[] featureTypes = FeatureType.values();
        for (FeatureType featureType : featureTypes) {
            features[i] = features[i].replaceAll("\\s+", "");
            double feature = Double.parseDouble(features[i]);
            featuresMap.put(featureType, feature);
            i++;
        }
        return featuresMap;
    }

    private void createSignature(String line) {
        String[] features = line.split(",");

        String authorName = features[0];
        Map<FeatureType, Double> featuresMap = createFeaturesMap(features);

        LinguisticSignature linguisticSignature = new LinguisticSignature(featuresMap);
        AuthorsSignaturesStruct authorAndSignaturePair = new AuthorsSignaturesStruct(authorName, linguisticSignature);
        linguisticSignatures.add(authorAndSignaturePair);
    }

    public void readSignatureFile(InputStream knownSignatures) {
        Scanner sc = new Scanner(knownSignatures);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            createSignature(line);
        }
        sc.close();
    }
}
