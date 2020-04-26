package bg.sofia.uni.fmi.mjt.authroship.detection.utilities;

import bg.sofia.uni.fmi.mjt.authroship.detection.FeatureType;
import bg.sofia.uni.fmi.mjt.authroship.detection.LinguisticSignature;
import bg.sofia.uni.fmi.mjt.authroship.detection.structures.Pair;
import bg.sofia.uni.fmi.mjt.authroship.detection.exceptios.CalculatingSignatureException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.authroship.detection.FeatureType.*;

public class LinguisticSignatureCalculator {

    private InputStream stream;

    public LinguisticSignatureCalculator(InputStream text) {
        stream = text;
    }

    private static String cleanUp(String word) {
        return word.toLowerCase()
                .replaceAll( "^[!.,:;\\-?<>#*\'\"\\[(\\])\\n\\t\\\\]+|[!.,:;\\-?<>#*\'\"\\[(\\])\\n\\t\\\\]+$", "");
    }

    private int countLengthOfWords(String[] words) {
        int wordsLength = 0;
        for (String word : words) {
            word = cleanUp(word);
            wordsLength += word.length();
        }
        return wordsLength;
    }

    private int countWords(String[] words) {
        int wordsCount = 0;
        for (String word : words) {
            word = cleanUp(word);
            if (word.length() != 0) {
                wordsCount++;
            }
        }
        return wordsCount;
    }

    private void saveUniqueWords(HashSet<String> uniqueWords, String[] words) {
        for (String word : words) {
            word = cleanUp(word);
            if (!word.equals("")) {
                uniqueWords.add(word);
            }
        }
    }

    private int getNumberOfUniqueWords(HashMap<String, Integer> numberOfWordsMap) {
        int numberOfUniqueWords = (int)numberOfWordsMap.values().stream().filter(count -> count == 1).count();
        return numberOfUniqueWords;
    }

    private void saveWordsCount(HashMap<String, Integer> numberOfWordsMap, String[] words) {
        for (String word : words) {
            word = cleanUp(word);
            if (!word.equals("")) {
                if (!numberOfWordsMap.containsKey(word)) {
                    numberOfWordsMap.put(word, 1);
                } else {
                    int wordCount = numberOfWordsMap.get(word);
                    wordCount++;
                    numberOfWordsMap.put(word, wordCount);
                }
            }
        }
    }

    private Pair countSentences(String line, boolean isCorrectSentence) {
        int sentenceCount = 0;
        String delimiters = "?!.";
        for (int i = 0; i < line.length(); i++) {
            String symbol = String.valueOf(line.charAt(i));
            if (!symbol.matches("[?!. ]")) {
                isCorrectSentence = true;
            }
            if (isCorrectSentence && delimiters.indexOf(line.charAt(i)) != -1) {
                sentenceCount++;
                isCorrectSentence = false;
            }
        }
        return new Pair(isCorrectSentence, sentenceCount);
    }

    private Pair countPhrases(String line, boolean isCorrectSentence) {
        String delimiters = "?!.;:,";
        int phrasesCount = 0;
        for (int i = 0; i < line.length(); i++) {
            String symbol = String.valueOf(line.charAt(i));
            if (!symbol.matches("[?!. ;:,]")) {
                isCorrectSentence = true;
            }
            if (isCorrectSentence && delimiters.indexOf(line.charAt(i)) != -1) {
                phrasesCount++;
                isCorrectSentence = false;
            }
        }
        return new Pair(isCorrectSentence, phrasesCount);
    }

    public Map<FeatureType, Double> createLinguisticSignatureMap() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        int wordsCount = 0;
        int wordsLength = 0;
        HashSet<String> uniqueWords = new HashSet<>();
        HashMap<String, Integer> numberOfWordsMap = new HashMap<>();
        int sentenceCount = 0;
        boolean isCorrectSentence1 = false;
        boolean isCorrectSentence2 = false;
        int phrasesCount = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\\s+");
            wordsCount += countWords(words);
            wordsLength += countLengthOfWords(words);
            saveUniqueWords(uniqueWords, words);
            saveWordsCount(numberOfWordsMap, words);

            Pair sentenceInfoPair = countSentences(line, isCorrectSentence1);
            sentenceCount += sentenceInfoPair.getValue();
            isCorrectSentence1 = sentenceInfoPair.getKey();

            Pair phrasesInfoPair = countPhrases(line, isCorrectSentence2);
            phrasesCount += phrasesInfoPair.getValue();
            isCorrectSentence2 = phrasesInfoPair.getKey();
        }
        sentenceCount++;
        phrasesCount++;
        int numberOfUniqueWords = getNumberOfUniqueWords(numberOfWordsMap);

        double averageWordLength = (double)wordsLength / (double)wordsCount;
        double typeTokenRatio = (double)uniqueWords.size() / (double)wordsCount;
        double hapaxLegomenaRatio = (double)numberOfUniqueWords / (double)wordsCount;
        double averageSentenceLength = (double) wordsCount / (double) sentenceCount;
        double averageSentenceComplexity = (double) phrasesCount / (double) sentenceCount;

        Map<FeatureType, Double> featuresMap = new HashMap<>();
        featuresMap.put(AVERAGE_WORD_LENGTH, averageWordLength);
        featuresMap.put(TYPE_TOKEN_RATIO, typeTokenRatio);
        featuresMap.put(HAPAX_LEGOMENA_RATIO, hapaxLegomenaRatio);
        featuresMap.put(AVERAGE_SENTENCE_LENGTH, averageSentenceLength);
        featuresMap.put(AVERAGE_SENTENCE_COMPLEXITY, averageSentenceComplexity);

        return featuresMap;
    }

    public LinguisticSignature createLinguisticSignature() throws CalculatingSignatureException {
        try {
            Map<FeatureType, Double> featuresMap = createLinguisticSignatureMap();
            LinguisticSignature linguisticSignature = new LinguisticSignature(featuresMap);
            return linguisticSignature;

        } catch (IOException e) {
            throw new CalculatingSignatureException();
        }
    }
}
