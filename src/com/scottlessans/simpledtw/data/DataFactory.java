package com.scottlessans.simpledtw.data;

import com.scottlessans.simpledtw.dtw.FeatureVector;
import com.scottlessans.simpledtw.serialization.FeatureVectorListReader;
import com.scottlessans.simpledtw.serialization.InvalidFeatureVectorListFileException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * User: slessans
 * Date: 2/16/14
 * Time: 10:14 PM
 */
public class DataFactory {

    public static enum DataType {
        Intensity("intensity"),
        MelFrequencyCepstralCoefficients("mfcc"),
        MelFrequencyBands("mfb"),
        Pitch("pitch"),
        HarmonicToNoiseRatio("hnr");

        public final String fileExtension;

        DataType(String fileExtension) {
            this.fileExtension = fileExtension;
        }
    }

    private static final String[] DigitNames = {
            "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"
    };

    private String pathToTestingFolder;
    private String pathToTrainingFolder;

    public DataFactory(String pathToTestingFolder, String pathToTrainingFolder) {
        this.pathToTestingFolder = preparePath(pathToTestingFolder);
        this.pathToTrainingFolder = preparePath(pathToTrainingFolder);
    }

    protected static String preparePath(String path) {
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }

        if (path.startsWith("~" + File.separator)) {
            path = System.getProperty("user.home") + path.substring(1);
        }

        return path;
    }

    /**
     * @param digit between 1, 10 inclusive
     * @param variant between 1, 5 inclusive
     * @param dataType data type
     * @return the features
     */
    public ArrayList<FeatureVector> readTrainingFeatures(int digit, int variant, DataType dataType)
            throws IOException, InvalidFeatureVectorListFileException
    {
        if (variant < 1 || variant > 5) {
            throw new IllegalArgumentException(
                    "Variant must be between 1 and 5 inclusive, but " + variant + "requested."
            );
        }
        return this.readFeatures(this.pathToTrainingFolder, digit, variant, dataType);
    }

    /**
     * @param digit between 1, 10 inclusive
     * @param dataType data type
     * @return the features
     */
    public ArrayList<FeatureVector> readTestingDataFeatures(int digit, DataType dataType)
            throws IOException, InvalidFeatureVectorListFileException
    {
        return this.readFeatures(this.pathToTestingFolder, digit, 6, dataType);
    }

    protected ArrayList<FeatureVector> readFeatures(String path, int digit, int variant, DataType dataType)
            throws IOException, InvalidFeatureVectorListFileException {

        if (digit < 1 || digit > 10) {
            throw new IllegalArgumentException(
                    "Digit must be between 1 and 10 inclusive, but " + digit + "requested."
            );
        }

        String filePath = path + DigitNames[digit] + variant + "." + dataType.fileExtension;
        return new FeatureVectorListReader(new File(filePath)).parseFeatureVectors();
    }

}
