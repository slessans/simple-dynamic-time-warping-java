package com.scottlessans.simpledtw.serialization;

import com.scottlessans.simpledtw.dtw.FeatureVector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Decent implementation -- double memory usage on a per line basis.
 *
 * User: slessans
 * Date: 2/16/14
 * Time: 9:38 PM
 */
public class FeatureVectorListReader {

    protected final File source;
    protected int currentLineNumber;

    public FeatureVectorListReader(File source) {
        assert (source != null);
        this.source = source;
    }

    public ArrayList<FeatureVector> parseFeatureVectors()
            throws IOException, InvalidFeatureVectorListFileException
    {
        this.currentLineNumber = 1;

        ArrayList<FeatureVector> vectorList = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(this.source))) {
            for(String line; (line = br.readLine()) != null; this.currentLineNumber++) {

                vectorList.add(this.parseLine(line));

                if (vectorList.get(0).getDimensions() != vectorList.get(vectorList.size() - 1).getDimensions()) {
                    String reason = "vector size does not match rest of list: should be " +
                            vectorList.get(0).getDimensions() + " but size is " +
                            vectorList.get(vectorList.size() - 1).getDimensions();

                    throw new InvalidFeatureVectorListFileException(
                            this.source, this.currentLineNumber, reason
                    );
                }
            }
        }

        return vectorList;
    }

    protected FeatureVector parseLine(String line) throws InvalidFeatureVectorListFileException {
        Scanner scanner = new Scanner(line);

        List<Double> values = new LinkedList<>();
        while (scanner.hasNextDouble()) {
            values.add(scanner.nextDouble());
        }

        if (values.size() <= 0) {
            throw new InvalidFeatureVectorListFileException(
                    this.source, this.currentLineNumber, "vector size is 0"
            );
        }

        FeatureVector featureVector = new FeatureVector(values.size());

        int idx = 0;
        for(Double d : values) {
            featureVector.setValue(idx, d);
            idx++;
        }

        return featureVector;
    }

}
