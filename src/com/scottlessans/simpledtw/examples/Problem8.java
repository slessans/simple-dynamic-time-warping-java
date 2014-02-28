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
public class Problem8 {

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
    protected Map<DataFactory.DataType, ArrayList<FeatureVector>> featureVectors;

    public Problem8(String testingPath, String trainingPath) {
        this.algorithm = makeAlgorithm();
        this.dataFactory = new InMemoryCacheDataFactory(testingPath, trainingPath);
    }

    public String run(int testingDigit) throws IOException, InvalidFeatureVectorListFileException {

        if (testingDigit < 1 || testingDigit > 10) {
            throw new IllegalArgumentException("Digit must be between 1 and 10.");
        }

        this.featureVectors = new HashMap<>();
        for(DataFactory.DataType dataType : DataFactory.DataType.values()) {
            this.featureVectors.put(dataType, dataFactory.readTestingDataFeatures(testingDigit, dataType));
        }

        ArrayList<Result> results = new ArrayList<>();

        for(int otherDigit = 1; otherDigit <= 10; otherDigit++) {
            for(int otherVariant = 1; otherVariant <= 5; otherVariant++) {
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

        /*
        System.out.println("\n\nResults (lowest to highest distortion):");
        int rank = 1;
        for(Result result : resultArray) {
            System.out.println("Rank " + rank + ": " + result);
            rank++;
        }*/

        StringBuilder sb = new StringBuilder();

        sb.append("\n\nTesting Digit ")
                .append(testingDigit)
                .append(" Closest Labels:\n")
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

        double totalDistortion = 0.0d;

        for(DataFactory.DataType dataType : DataFactory.DataType.values()) {
            ArrayList<FeatureVector> otherFeatures =
                    dataFactory.readTrainingFeatures(digit, variant, dataType);

            DtwResult result = this.algorithm.run(this.featureVectors.get(dataType), otherFeatures);

            totalDistortion += result.getDistortion();
        }


        totalDistortion = (totalDistortion / DataFactory.DataType.values().length);


        System.out.println("\tDistortion: " + totalDistortion);

        return new Result(digit, variant, totalDistortion);
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

        Problem8 p8 = new Problem8(testingPath, trainingPath);
        List<String> results = new LinkedList<>();

        for(int i = 1; i <= 10; i++) {
            results.add(p8.run(i));
        }

        for(String result : results) {
            System.out.println(result);
        }

    }

}
