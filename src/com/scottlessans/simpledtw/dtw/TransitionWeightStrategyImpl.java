package com.scottlessans.simpledtw.dtw;

/**
 * User: slessans
 * Date: 2/16/14
 * Time: 7:13 PM
 */
public class TransitionWeightStrategyImpl implements TransitionWeightStrategy {

    private final double left;
    private final double bottom;
    private final double bottomLeft;

    public TransitionWeightStrategyImpl(double left, double bottom, double bottomLeft) {
        this.left = left;
        this.bottom = bottom;
        this.bottomLeft = bottomLeft;
    }


    @Override
    public double getLeftTransitionWeight() {
        return this.left;
    }

    @Override
    public double getBottomTransitionWeight() {
        return this.bottom;
    }

    @Override
    public double getBottomLeftTransitionWeight() {
        return this.bottomLeft;
    }
}
