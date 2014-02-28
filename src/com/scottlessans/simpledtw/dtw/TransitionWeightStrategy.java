package com.scottlessans.simpledtw.dtw;

/**
 * User: slessans
 * Date: 2/16/14
 * Time: 7:12 PM
 */
public interface TransitionWeightStrategy {

    public double getLeftTransitionWeight();
    public double getBottomTransitionWeight();
    public double getBottomLeftTransitionWeight();

}
