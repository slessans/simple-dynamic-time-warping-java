package com.scottlessans.simpledtw.dtw;

/**
 * This is used to compute the difference between two feature vectors
 *
 * User: slessans
 * Date: 2/16/14
 * Time: 7:07 PM
 */
public interface FeatureVectorDifferenceStrategy {

    /**
     * The vectors passed are guaranteed to be same dimensionality, no need to check.
     * This should be as efficient as possible, will be called once for each pair of
     * feature vectors.
     *
     * @param v1
     * @param v2
     * @return result of local distance metric
     */
    public double calculateDifference(final FeatureVector v1, final FeatureVector v2);

}
