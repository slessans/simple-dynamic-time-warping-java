package com.scottlessans.simpledtw.dtw;

import java.util.Arrays;

/**
 * Written to be flexible for subclassing. However, made
 * to make sure that number of dimensions can't be changed
 * after construction.
 *
 * User: slessans
 * Date: 2/16/14
 * Time: 6:08 PM
 */
public class FeatureVector {

    private final double [] data;

    public FeatureVector(int dimensions) {
        assert (dimensions > 0);
        this.data = new double[dimensions];
    }

    public double getValue(final int idx) {
        return this.data[idx];
    }

    public void setValue(final int idx, double val) {
        this.data[idx] = val;
    }

    public FeatureVector subtract(FeatureVector other) {
        assert (other.getDimensions() == this.getDimensions());

        FeatureVector result = new FeatureVector(this.getDimensions());
        for(int i = 0; i < this.getDimensions(); i++) {
            result.setValue(i, this.getValue(i) - other.getValue(i));
        }

        return result;
    }

    public double dot(FeatureVector other) {
        assert (other.getDimensions() == this.getDimensions());

        double value = 0d;
        for(int i = 0; i < this.getDimensions(); i++) {
            value += (this.getValue(i) * other.getValue(i));
        }

        return value;
    }

    public double squaredEuclideanDistance(FeatureVector other) {
        assert (other.getDimensions() == this.getDimensions());

        double distance = 0.0;
        for(int i = 0; i < this.data.length; i++) {
            distance += Math.pow((this.data[i] - other.data[i]), 2);
        }

        return distance;
    }

    /**
     * @return number of dimensions of this vector
     */
    public final int getDimensions() {
        return this.data.length;
    }

    @Override
    public String toString() {
        return "{Feature Vector(" + this.getDimensions() + "): " + Arrays.toString(this.data) + "}";
    }
}
