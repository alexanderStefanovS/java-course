import bg.sofia.uni.fmi.mjt.authroship.detection.LinguisticSignature;
import bg.sofia.uni.fmi.mjt.authroship.detection.exceptios.CalculatingSignatureException;
import bg.sofia.uni.fmi.mjt.authroship.detection.utilities.LinguisticSignatureCalculator;
import bg.sofia.uni.fmi.mjt.authroship.detection.utilities.LinguisticSignatureCalculatorBeta;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class LinguisticSignatureCalculatorTest {

    public static final double RESULT1 = 43.0 / 11.0;
    public static final double RESULT2 = 7.0 / 16.0;
    public static final double RESULT3 = 4.0 / 5.0;
    public static final int DELTA = 3;
    public static final int RESULT4 = 4;
    private LinguisticSignatureCalculatorBeta linguisticSignatureCalculatorBeta;
    private LinguisticSignatureCalculator linguisticSignatureCalculator;

    @Before
    public void setUp() throws FileNotFoundException {
        InputStream stream = new FileInputStream("resources/try.txt");
        linguisticSignatureCalculatorBeta = new LinguisticSignatureCalculatorBeta(stream);
        linguisticSignatureCalculator = new LinguisticSignatureCalculator(stream);
    }

    @Test
    public void testCalculateSentences() throws IOException {
        int count = linguisticSignatureCalculatorBeta.countSentences();
        System.out.println(count);
    }

    @Test
    public void testCalculateWordsAverageLength() throws IOException {
        double averageWordsLength = linguisticSignatureCalculatorBeta.calculateWordsAverageLength();
        assertEquals("Check if the average words length." , averageWordsLength, RESULT1, DELTA);
    }

    @Test
    public void testCalculateTypeTokenRatio() throws IOException {
        double typeTokenRatio = linguisticSignatureCalculatorBeta.calculateTypeTokenRatio();
        System.out.println(typeTokenRatio);
    }

    @Test
    public void testHapaxLegomenaRatio() throws IOException {
        double hapaxLegomenaRatio = linguisticSignatureCalculatorBeta.calculateHapaxLegomenaRatio();
        assertEquals(RESULT2, hapaxLegomenaRatio, DELTA);
    }

    @Test
    public void testCalculateAverageWordsInSentence() throws IOException {
        double averageWordsInSentence = linguisticSignatureCalculatorBeta.calculateAverageWordsInSentence();
        assertEquals(RESULT4, averageWordsInSentence, DELTA);
    }

    @Test
    public void testCountPhrases() throws IOException {
        int numOfPhrases = linguisticSignatureCalculatorBeta.countPhrases();
        System.out.println(numOfPhrases);
    }

    @Test
    public void testCalculateAverageSentenceComplexity() throws IOException {
        double averageSentenceComplexity = linguisticSignatureCalculatorBeta.calculateAverageSentenceComplexity();
        assertEquals(RESULT3, averageSentenceComplexity, DELTA);
    }

    @Test
    public void testCreateLinguisticSignatureBeta() throws CalculatingSignatureException {
        LinguisticSignature linguisticSignature = linguisticSignatureCalculatorBeta.createLinguisticSignature();
        System.out.println(linguisticSignature);
    }

    @Test
    public void testCreateLinguisticSignature() throws CalculatingSignatureException {
        LinguisticSignature linguisticSignature = linguisticSignatureCalculator.createLinguisticSignature();
        System.out.println(linguisticSignature);
    }

}
