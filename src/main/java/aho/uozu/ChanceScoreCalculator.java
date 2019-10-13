package aho.uozu;

import java.util.Arrays;

public class ChanceScoreCalculator {
    public int calculateScore(Roll roll) {
        return Arrays.stream(roll.getValues()).sum();
    }
}
