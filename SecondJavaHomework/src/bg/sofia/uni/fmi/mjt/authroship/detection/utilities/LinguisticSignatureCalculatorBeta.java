package bg.sofia.uni.fmi.mjt.authroship.detection.utilities;

import bg.sofia.uni.fmi.mjt.authroship.detection.FeatureType;
import bg.sofia.uni.fmi.mjt.authroship.detection.LinguisticSignature;
import bg.sofia.uni.fmi.mjt.authroship.detection.exceptios.CalculatingSignatureException;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.authroship.detection.FeatureType.*;

public class LinguisticSignatureCalculatorBeta {

    private InputStream stream;

    public LinguisticSignatureCalculatorBeta(InputStream text) {
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

    public double calculateWordsAverageLength() throws IOException {
        FileInputStream fileStream = (FileInputStream) stream;
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
        fileStream.getChannel().position(0);

        int wordsCount = 0;
        int wordsLength = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\\s+");
            wordsCount += countWords(words);
            wordsLength += countLengthOfWords(words);
        }
        double averageWordsLength = (double)wordsLength / (double)wordsCount;

        return averageWordsLength;
    }

    private void saveUniqueWords(HashSet<String> uniqueWords, String[] words) {
        for (String word : words) {
            word = cleanUp(word);
            if (!word.equals("")) {
                uniqueWords.add(word);
            }
        }
    }

    public double calculateTypeTokenRatio() throws IOException {
        FileInputStream fileStream = (FileInputStream) stream;
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
        fileStream.getChannel().position(0);

        int wordsCount = 0;
        String line;
        HashSet<String> uniqueWords = new HashSet<>();
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\\s+");
            wordsCount += countWords(words);
            saveUniqueWords(uniqueWords, words);
        }
        double typeTokenRatio = (double)uniqueWords.size() / (double)wordsCount;

        return typeTokenRatio;
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

    public double calculateHapaxLegomenaRatio() throws IOException {
        FileInputStream fileStream = (FileInputStream) stream;
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
        fileStream.getChannel().position(0);

        int wordsCount = 0;
        String line;
        HashMap<String, Integer> numberOfWordsMap = new HashMap<>();
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\\s+");
            wordsCount += countWords(words);
            saveWordsCount(numberOfWordsMap, words);
        }
        int numberOfUniqueWords = getNumberOfUniqueWords(numberOfWordsMap);
        double hapaxLegomenaRatio = (double)numberOfUniqueWords / (double)wordsCount;

        return hapaxLegomenaRatio;
    }

    public int countSentences() throws IOException {
        FileInputStream fileStream = (FileInputStream) stream;
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
        fileStream.getChannel().position(0);

        int sentenceCount = 0;
        boolean isCorrectSentence = false;
        String line;
        String delimiters = "?!.";
        while ((line = reader.readLine()) != null) {
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
        }
        sentenceCount++;


        return sentenceCount;
    }

    public double calculateAverageWordsInSentence() throws IOException {
        FileInputStream fileStream = (FileInputStream) stream;
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
        fileStream.getChannel().position(0);

        int wordsCount = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\\s+");
            wordsCount += countWords(words);
        }

        int numberOfSentences = countSentences();
        double averageWordsInSentence = (double) wordsCount / (double) numberOfSentences;

        reader = null;
        return averageWordsInSentence;
    }

    public int countPhrases() throws IOException {
        FileInputStream fileStream = (FileInputStream) stream;
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
        fileStream.getChannel().position(0);

        int phrasesCount = 0;
        boolean isCorrectSentence = false;
        String line;
        String delimiters = "?!.;:,";
        while ((line = reader.readLine()) != null) {
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
        }
        phrasesCount++;

        return phrasesCount;
    }

    public double calculateAverageSentenceComplexity() throws IOException {
        int numOfPhrases = countPhrases();
        int numOfSentences = countSentences();
        double averageSentenceComplexity = (double) numOfPhrases / (double) numOfSentences;

        return averageSentenceComplexity;
    }

    public LinguisticSignature createLinguisticSignature() throws CalculatingSignatureException {

        try {
            double averageWordLength = calculateWordsAverageLength();
            double typeTokenRatio = calculateTypeTokenRatio();
            double hapaxLegomenaRatio = calculateHapaxLegomenaRatio();
            double averageSentenceLength = calculateAverageWordsInSentence();
            double averageSentenceComplexity = calculateAverageSentenceComplexity();

            Map<FeatureType, Double> featuresMap = new HashMap<>();
            featuresMap.put(AVERAGE_WORD_LENGTH, averageWordLength);
            featuresMap.put(TYPE_TOKEN_RATIO, typeTokenRatio);
            featuresMap.put(HAPAX_LEGOMENA_RATIO, hapaxLegomenaRatio);
            featuresMap.put(AVERAGE_SENTENCE_LENGTH, averageSentenceLength);
            featuresMap.put(AVERAGE_SENTENCE_COMPLEXITY, averageSentenceComplexity);
            LinguisticSignature linguisticSignature = new LinguisticSignature(featuresMap);

            return linguisticSignature;

        } catch (IOException exc) {
            throw new CalculatingSignatureException();
        }

    }
}