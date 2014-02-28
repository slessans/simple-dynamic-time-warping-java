package com.scottlessans.simpledtw.data;

import com.scottlessans.simpledtw.dtw.FeatureVector;
import com.scottlessans.simpledtw.serialization.InvalidFeatureVectorListFileException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User: slessans
 * Date: 2/18/14
 * Time: 10:12 AM
 */
public class InMemoryCacheDataFactory extends DataFactory {

    protected Map<String, ArrayList<FeatureVector>> cache;

    public InMemoryCacheDataFactory(String pathToTestingFolder, String pathToTrainingFolder) {
        super(pathToTestingFolder, pathToTrainingFolder);
        this.cache = new HashMap<>();
    }

    @Override
    protected ArrayList<FeatureVector> readFeatures(String path, int digit, int variant, DataType dataType)
            throws IOException, InvalidFeatureVectorListFileException
    {
        String hash = path + "." + digit + "." + variant + "." + dataType.fileExtension;

        if (!cache.containsKey(hash)) {
            cache.put(hash, super.readFeatures(path, digit, variant, dataType));
        }

        return cache.get(hash);
    }
}
