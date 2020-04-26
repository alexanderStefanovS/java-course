import bg.sofia.uni.fmi.mjt.authroship.detection.AuthorshipDetectorImpl;
import bg.sofia.uni.fmi.mjt.authroship.detection.FeatureType;
import bg.sofia.uni.fmi.mjt.authroship.detection.LinguisticSignature;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.authroship.detection.FeatureType.*;
import static bg.sofia.uni.fmi.mjt.authroship.detection.FeatureType.AVERAGE_SENTENCE_COMPLEXITY;
import static org.junit.Assert.assertEquals;

public class AuthorshipDetectorTest {

    public static final double AVE_WORD_LENGTH1 = 4.4;
    public static final double TYPE_TOKEN_RATIO1 = 0.1;
    public static final double H_L_R1 = 0.05;
    public static final double AVE_SEN_LEN1 = 10.0;
    public static final double AVE_SEN_COMP1 = 2.0;
    public static final double AVE_WORD_LENGTH2 = 4.3;
    public static final double TYPE_TOKEN_RATIO2 = 0.1;
    public static final double H_L_R2 = 0.04;
    public static final double AVE_SEN_LEN2 = 16.0;
    public static final double AVE_SEN_COMP2 = 4.0;
    public static final int SUM = 12;
    private InputStream mysteryText;
    private InputStream signaturesDataset;

    public static final double[] WEIGHTS = new double[]{11, 33, 50, 0.4, 4};

    @Before
    public void setUp() throws FileNotFoundException {
        mysteryText = new FileInputStream("resources/mystery3.txt");
        signaturesDataset = new FileInputStream("resources/knownSignatures.txt");
    }

    @Test
    public void calculateSignature() {
        AuthorshipDetectorImpl authorshipDetector = new AuthorshipDetectorImpl(signaturesDataset, null);
        LinguisticSignature linguisticSignature = authorshipDetector.calculateSignature(mysteryText);
        System.out.println(linguisticSignature);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateSimilarityThrowingException() {
        AuthorshipDetectorImpl authorshipDetector = new AuthorshipDetectorImpl(null, WEIGHTS);
        double sum = authorshipDetector.calculateSimilarity(null, null);
    }

    @Test
    public void testCalculateSimilarity() {
        Map<FeatureType, Double> firstSignatureMap = new HashMap<>();
        firstSignatureMap.put(AVERAGE_WORD_LENGTH, AVE_WORD_LENGTH1);
        firstSignatureMap.put(TYPE_TOKEN_RATIO, TYPE_TOKEN_RATIO1);
        firstSignatureMap.put(HAPAX_LEGOMENA_RATIO, H_L_R1);
        firstSignatureMap.put(AVERAGE_SENTENCE_LENGTH, AVE_SEN_LEN1);
        firstSignatureMap.put(AVERAGE_SENTENCE_COMPLEXITY, AVE_SEN_COMP1);
        LinguisticSignature firstSignature = new LinguisticSignature(firstSignatureMap);

        Map<FeatureType, Double> secondSignatureMap = new HashMap<>();
        secondSignatureMap.put(AVERAGE_WORD_LENGTH, AVE_WORD_LENGTH2);
        secondSignatureMap.put(TYPE_TOKEN_RATIO, TYPE_TOKEN_RATIO2);
        secondSignatureMap.put(HAPAX_LEGOMENA_RATIO, H_L_R2);
        secondSignatureMap.put(AVERAGE_SENTENCE_LENGTH, AVE_SEN_LEN2);
        secondSignatureMap.put(AVERAGE_SENTENCE_COMPLEXITY, AVE_SEN_COMP2);
        LinguisticSignature secondSignature = new LinguisticSignature(secondSignatureMap);

        AuthorshipDetectorImpl authorshipDetector = new AuthorshipDetectorImpl(null, WEIGHTS);
        double sum = authorshipDetector.calculateSimilarity(firstSignature, secondSignature);

        assertEquals("Sum equals 12.", true, (sum == SUM));
    }

    @Test
    public void findAuthor() throws FileNotFoundException {
        AuthorshipDetectorImpl authorshipDetector = new AuthorshipDetectorImpl(signaturesDataset, WEIGHTS);
        String author = authorshipDetector.findAuthor(mysteryText);
        System.out.println(author);
    }

}
