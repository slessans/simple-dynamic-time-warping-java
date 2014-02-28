package com.scottlessans.simpledtw.examples;

import com.scottlessans.simpledtw.data.DataFactory;
import com.scottlessans.simpledtw.data.InMemoryCacheDataFactory;
import com.scottlessans.simpledtw.dtw.*;
import com.scottlessans.simpledtw.serialization.InvalidFeatureVectorListFileException;

import java.io.IOException;
import java.util.*;

/**
 * User: slessans
 * Date: 2/16/14
 * Time: 11:27 PM
 */
public class Problem6 {

    private static final String[] DigitNames = {
            "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"
    };


    public static class Result {

        public final int digit;
        public final int variant;
        public final double distortion;

        public Result(int digit, int variant, double distortion) {
            this.digit = digit;
            this.variant = variant;
            this.distortion = distortion;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "digit=" + digit +
                    ", variant=" + variant +
                    ", distortion=" + distortion +
                    '}';
        }
    }

    protected final SymmetricDtwAlgorithm algorithm;
    protected final DataFactory dataFactory;
    protected ArrayList<FeatureVector> featureVectors;

    public Problem6(String testingPath, String trainingPath) {
        this.algorithm = makeAlgorithm();
        this.dataFactory = new InMemoryCacheDataFactory(testingPath, trainingPath);
    }

    public String run(final int digit, final int variant) throws IOException, InvalidFeatureVectorListFileException {

        if (digit < 1 || digit > 10) {
            throw new IllegalArgumentException("Digit must be between 1 and 10.");
        }

        if (variant < 1 || variant > 5) {
            throw new IllegalArgumentException("Variant must be between 1 and 5.");
        }

        this.featureVectors = dataFactory.readTrainingFeatures(digit, variant, DataFactory.DataType.Intensity);

        ArrayList<Result> results = new ArrayList<>();

        for(int otherDigit = 1; otherDigit <= 10; otherDigit++) {
            for(int otherVariant = 1; otherVariant <= 5; otherVariant++) {

                if (otherDigit == digit && otherVariant == variant) {
                    // this is the same one, skip
                    continue;
                }

                System.out.println("Comparing to digit " + otherDigit + " variant " + otherVariant + "...");
                results.add(this.compareTo(otherDigit, otherVariant));
            }
        }

        Result [] resultArray = results.toArray(new Result[results.size()]);
        Arrays.sort(resultArray, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                if (o1.distortion < o2.distortion) {
                    return -1;
                }
                if (o1.distortion > o2.distortion) {
                    return 1;
                }
                return 0;
            }
        });

        StringBuilder sb = new StringBuilder();

        sb.append("\n\nTraining Digit ")
                .append(digit)
                .append(" Variant ")
                .append(variant)
                .append(" Closest Points:\n")
                .append("\t");

        for(int i = 0; i < 3; i++) {
            sb.append(DigitNames[resultArray[i].digit])
                    .append(resultArray[i].variant);
            if (i < 2) {
                sb.append(", ");
            }
        }

        sb.append("\n\t");

        for(int i = 0; i < 3; i++) {
            sb.append(String.format("%.2f", resultArray[i].distortion));
            if (i < 2) {
                sb.append(", ");
            }
        }
        sb.append("\n");

        return sb.toString();
    }


    protected Result compareTo(final int digit, final int variant)
            throws InvalidFeatureVectorListFileException, IOException
    {
        ArrayList<FeatureVector> otherFeatures =
                dataFactory.readTrainingFeatures(digit, variant, DataFactory.DataType.Intensity);

        DtwResult result = this.algorithm.run(this.featureVectors, otherFeatures);

        System.out.println("\tDistortion: " + result.getDistortion());

        return new Result(digit, variant, result.getDistortion());
    }

    protected static SymmetricDtwAlgorithm makeAlgorithm() {
        final InitialConditionStrategy initialConditionStrategy = new InitialConditionStrategy() {
            @Override
            public double calculateInitialValue(double diff, FeatureVector seqAVal, FeatureVector seqBVal) {
                return 2 * diff;
            }
        };

        final FeatureVectorDifferenceStrategy featureVectorDifferenceStrategy = new FeatureVectorDifferenceStrategy() {
            @Override
            public double calculateDifference(FeatureVector v1, FeatureVector v2) {
                return v1.squaredEuclideanDistance(v2);
            }
        };

        final TransitionWeightStrategy transitionWeightStrategy = new TransitionWeightStrategyImpl(
                1.0d, 1.0d, 2.0d
        );

        return new SymmetricDtwAlgorithm(
                initialConditionStrategy,
                featureVectorDifferenceStrategy,
                transitionWeightStrategy
        );
    }

    protected static String[] getPaths(String [] args) {
        String testingPath;
        String trainingPath;

        if (args.length == 2) {
            testingPath = args[0];
            trainingPath = args[1];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Where are the testing and training data folders?");
            System.out.println("Absolute or relative paths are fine.");
            System.out.println("To avoid entering this you may run program with 2 args (testing and training path)\n");
            System.out.print("Path to testing data folder: ");
            testingPath = scanner.nextLine();
            System.out.print("Path to training data folder: ");
            trainingPath = scanner.nextLine();
        }

        return new String[]{testingPath, trainingPath};
    }

    public static void main(String [] args) throws Exception {
        String [] paths = getPaths(args);
        String testingPath = paths[0];
        String trainingPath = paths[1];

        Problem6 p6 = new Problem6(testingPath, trainingPath);
        List<String> results = new LinkedList<>();

        TrainingInput [] inputs = {
                new TrainingInput(1,1),
                new TrainingInput(2,2),
                new TrainingInput(3,3),
                new TrainingInput(4,1),
                new TrainingInput(5,2),
                new TrainingInput(6,3),
                new TrainingInput(7,3),
                new TrainingInput(8,1),
                new TrainingInput(9,1),
                new TrainingInput(10,5)

        };

        for(TrainingInput input : inputs) {
            results.add(p6.run(input.digit, input.variant));
        }

        for(String result : results) {
            System.out.println(result);
        }
    }

    protected static class TrainingInput {
        public final int digit;
        public final int variant;

        public TrainingInput(int digit, int variant) {
            this.digit = digit;
            this.variant = variant;
        }
    }

}
