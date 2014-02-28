package com.scottlessans.simpledtw.serialization;

import java.io.File;

/**
 * User: slessans
 * Date: 2/16/14
 * Time: 9:56 PM
 */
public class InvalidFeatureVectorListFileException extends Exception {

    public InvalidFeatureVectorListFileException(File file, int line, String reason) {
        super("In file " + file.getName() + " on line " + line + ": " + reason);
    }
}
