package com.scottlessans.simpledtw.dtw;

/**
 * This defines strategy for setting the initial condition, that is,
 * the distortion of frame[0][0]. It can be arbitrarily complex or simple.
 *
 * User: slessans
 * Date: 2/16/14
 * Time: 7:15 PM
 */
public interface InitialConditionStrategy {

    /**
     * Strategies may or may not use the values passed. For more complex initial conditions,
     * override the calculateInitialValue method in the SymmetricDtwAlgorithm class.
     *
     * @param diff the difference between seqAVal and seqBVal as defined by difference strategy
     * @param seqAVal frame 0 of sequence a
     * @param seqBVal frame 0 of sequence b
     * @return initial value
     */
    public double calculateInitialValue(final double diff, final FeatureVector seqAVal, final FeatureVector seqBVal);

}
