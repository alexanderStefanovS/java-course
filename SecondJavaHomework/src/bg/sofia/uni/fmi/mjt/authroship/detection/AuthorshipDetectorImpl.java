package bg.sofia.uni.fmi.mjt.authroship.detection;

import bg.sofia.uni.fmi.mjt.authroship.detection.exceptios.CalculatingSignatureException;
import bg.sofia.uni.fmi.mjt.authroship.detection.structures.AuthorsSignaturesStruct;
import bg.sofia.uni.fmi.mjt.authroship.detection.utilities.LinguisticSignatureCalculator;
import bg.sofia.uni.fmi.mjt.authroship.detection.utilities.LinguisticSignaturesCreator;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class AuthorshipDetectorImpl implements AuthorshipDetector {

    private double[] weights;
    private InputStream signaturesDateset;

    public AuthorshipDetectorImpl(InputStream signaturesDataset, double[] weights) {
        this.weights = weights;
        this.signaturesDateset = signaturesDataset;
    }

    @Override
    public LinguisticSignature calculateSignature(InputStream mysteryText) {
        if (mysteryText == null) {
            throw new IllegalArgumentException();
        }

        LinguisticSignatureCalculator linguisticSignatureCalculator = new LinguisticSignatureCalculator(mysteryText);
        try {
            LinguisticSignature linguisticSignature = linguisticSignatureCalculator.createLinguisticSignature();
            return linguisticSignature;
        } catch (CalculatingSignatureException e) {
            return null;
        }
    }

    @Override
    public double calculateSimilarity(LinguisticSignature firstSignature, LinguisticSignature secondSignature) {
        if (firstSignature == null || secondSignature == null) {
            throw new IllegalArgumentException();
        }

        int i = 0;
        double featuresSum = 0;
        Map<FeatureType, Double> firstSignatureMap = firstSignature.getFeatures();
        Map<FeatureType, Double> secondSignatureMap = secondSignature.getFeatures();

        FeatureType[] featureTypes = FeatureType.values();
        for (FeatureType featureType : featureTypes) {
            double firstFeature = firstSignatureMap.get(featureType);
            double secondFeature = secondSignatureMap.get(featureType);
            featuresSum += Math.abs(firstFeature - secondFeature) * weights[i];
            i++;
        }

        return Math.round(featuresSum * 1.0) / 1.0;
    }

    private String findBestMatchingAuthor(List<AuthorsSignaturesStruct> authorsAndSignatures,
                                          LinguisticSignature linguisticSignature) {
        double bestSimilarity = -1.0;
        String bestAuthor = "";

        for (AuthorsSignaturesStruct authorsAndSignature : authorsAndSignatures) {
            String author = authorsAndSignature.getName();
            LinguisticSignature authorSignature = authorsAndSignature.getLinguisticSignature();
            double similarity = calculateSimilarity(linguisticSignature, authorSignature);
            if (bestSimilarity == -1 || similarity < bestSimilarity) {
                bestSimilarity = similarity;
                bestAuthor = author;
            }
        }

        return bestAuthor;
    }

    @Override
    public String findAuthor(InputStream mysteryText) {
        if (mysteryText == null) {
            throw new IllegalArgumentException();
        }

        LinguisticSignaturesCreator linguisticSignaturesCreator = new LinguisticSignaturesCreator();
        linguisticSignaturesCreator.readSignatureFile(signaturesDateset);
        List<AuthorsSignaturesStruct> authorsAndSignatures = linguisticSignaturesCreator.getLinguisticSignatures();
        LinguisticSignature linguisticSignature = calculateSignature(mysteryText);

        String author = findBestMatchingAuthor(authorsAndSignatures, linguisticSignature);
        return author;
    }
}
