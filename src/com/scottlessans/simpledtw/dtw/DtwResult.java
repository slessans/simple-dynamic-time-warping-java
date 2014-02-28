package com.scottlessans.simpledtw.dtw;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: slessans
 * Date: 2/16/14
 * Time: 11:47 PM
 */
public class DtwResult {

    protected double [][] frameDifferenceMatrix;
    protected DistortionNode [][] distortionMatrix;
    protected final ArrayList<FeatureVector> sequenceA;
    protected final ArrayList<FeatureVector> sequenceB;

    public DtwResult(ArrayList<FeatureVector> sequenceA, ArrayList<FeatureVector> sequenceB) {
        this.sequenceA = sequenceA;
        this.sequenceB = sequenceB;
    }

    public double[][] getFrameDifferenceMatrix() {
        return frameDifferenceMatrix;
    }

    public DistortionNode[][] getDistortionMatrix() {
        return distortionMatrix;
    }

    public double getDistortion() {
        final double rawDistortion =
                this.distortionMatrix[this.sequenceA.size()-1][this.sequenceB.size()-1].getValue();

        return (rawDistortion / (this.sequenceA.size() + this.sequenceB.size()));
    }

    /**
     * not recommended for large output
     */
    public void debugPrint() {
        for(int i = this.sequenceA.size() - 1; i >= 0; i--) {
            System.out.println(Arrays.toString(this.frameDifferenceMatrix[i]));
        }

        for(int i = this.sequenceA.size() - 1; i >= 0; i--) {
            for(int j = 0; j < this.sequenceB.size(); j++) {
                System.out.print(String.format("| %-30s |", this.distortionMatrix[i][j].toString()));
            }
            System.out.println();
        }
    }
}
