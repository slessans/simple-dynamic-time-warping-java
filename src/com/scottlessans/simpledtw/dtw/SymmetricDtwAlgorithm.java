package com.scottlessans.simpledtw.dtw;

import java.util.ArrayList;

/**
 * User: slessans
 * Date: 2/16/14
 * Time: 6:10 PM
 */
public class SymmetricDtwAlgorithm {

    private final FeatureVectorDifferenceStrategy featureVectorDifferenceStrategy;
    private final TransitionWeightStrategy transitionWeightStrategy;
    private final InitialConditionStrategy initialConditionStrategy;

    public SymmetricDtwAlgorithm(InitialConditionStrategy initialConditionStrategy,
                                 FeatureVectorDifferenceStrategy featureVectorDifferenceStrategy,
                                 TransitionWeightStrategy transitionWeightStrategy) {

        this.featureVectorDifferenceStrategy = featureVectorDifferenceStrategy;
        this.transitionWeightStrategy = transitionWeightStrategy;
        this.initialConditionStrategy = initialConditionStrategy;
    }

    protected static boolean isValidSequence(ArrayList<FeatureVector> seq, final int dims) {
        for(FeatureVector vec : seq) {
            if (vec.getDimensions() != dims) {
                return false;
            }
        }
        return true;
    }

    protected double[][] calculateDifferenceMatrix(ArrayList<FeatureVector> sequenceA,
                                                   ArrayList<FeatureVector> sequenceB) {

        double [][] frameDifferenceMatrix = new double[sequenceA.size()][sequenceB.size()];

        for(int i = 0; i < sequenceA.size(); i++) {
            for(int j = 0; j < sequenceB.size(); j++) {
                frameDifferenceMatrix[i][j] =
                        this.featureVectorDifferenceStrategy.calculateDifference(
                                sequenceA.get(i), sequenceB.get(j)
                        );
            }
        }

        return frameDifferenceMatrix;
    }

    /**
     * Responsible for calculating and returning the value for distortion matrix (0,0)
     * @return the initial value
     */
    protected double calculateInitialValue(ArrayList<FeatureVector> sequenceA,
                                           ArrayList<FeatureVector> sequenceB,
                                           double [][] frameDifferenceMatrix) {

        assert (this.initialConditionStrategy != null);
        return this.initialConditionStrategy.calculateInitialValue(
                frameDifferenceMatrix[0][0],
                sequenceA.get(0),
                sequenceB.get(0)
        );
    }

    /**
     * Responsible for creating distortion matrix. returned value will
     * be verified and set as this.distortionMatrix. Each node should
     * be non-null.
     *
     * @return the initialized distortion matrix
     */
    protected DistortionNode[][] createDistortionMatrix(ArrayList<FeatureVector> sequenceA,
                                                        ArrayList<FeatureVector> sequenceB) {

        DistortionNode[][] nodes = new DistortionNode[sequenceA.size()][sequenceB.size()];

        for(int i = 0; i < sequenceA.size(); i++) {
            for(int j = 0; j < sequenceB.size(); j++) {
                nodes[i][j] = new DistortionNode(i, j);
            }
        }

        return nodes;
    }

    public DtwResult run(ArrayList<FeatureVector> sequenceA, ArrayList<FeatureVector> sequenceB) {

        assert(sequenceA != null && sequenceB != null);
        assert(sequenceA.size() > 0);
        assert(sequenceB.size() > 0);

        final int dims = sequenceA.get(0).getDimensions();

        if (!isValidSequence(sequenceA, dims)) {
            throw new IllegalArgumentException("all feature vectors in sequenceA must have size " + dims);
        }

        if (!isValidSequence(sequenceB, dims)) {
            throw new IllegalArgumentException("all feature vectors in sequenceB must have size " + dims);
        }

        // make sure we're ready to run
        assert (this.featureVectorDifferenceStrategy != null);
        assert (this.transitionWeightStrategy != null);

        DtwResult result = new DtwResult(sequenceA, sequenceB);

        // calculate the difference matrix
        result.frameDifferenceMatrix = this.calculateDifferenceMatrix(sequenceA, sequenceB);

        // create the distortion matrix
        result.distortionMatrix = this.createDistortionMatrix(sequenceA, sequenceB);
        assert (result.distortionMatrix.length == sequenceA.size());
        assert (result.distortionMatrix[0].length == sequenceB.size());

        // maybe putting the if statements inside the loop isn't the most efficient
        // but its the prettiest code :) -- no micro-optimization here
        for(int i = 0; i < sequenceA.size(); i++) {
            for(int j = 0; j < sequenceB.size(); j++) {

                if (i == 0 && j == 0) {
                    // set initial condition
                    result.distortionMatrix[0][0].setLeft(this.calculateInitialValue(
                            sequenceA, sequenceB, result.frameDifferenceMatrix
                    ));
                    continue;
                }

                if (i > 0) {
                    final double bottomVal =
                            result.distortionMatrix[i-1][j].getValue() +
                            (this.transitionWeightStrategy.getBottomTransitionWeight() *
                                    result.frameDifferenceMatrix[i][j]);

                    result.distortionMatrix[i][j].setBottom(bottomVal);
                }

                if (j > 0) {
                    final double leftVal =
                            result.distortionMatrix[i][j-1].getValue() +
                                    (this.transitionWeightStrategy.getLeftTransitionWeight() *
                                            result.frameDifferenceMatrix[i][j]);

                    result.distortionMatrix[i][j].setLeft(leftVal);
                }

                if (i > 0 && j > 0) {
                    final double bottomLeftVal =
                            result.distortionMatrix[i-1][j-1].getValue() +
                                    (this.transitionWeightStrategy.getBottomLeftTransitionWeight() *
                                            result.frameDifferenceMatrix[i][j]);

                    result.distortionMatrix[i][j].setBottomLeft(bottomLeftVal);
                }

            }
        }

        return result;
    }


}
