package io.github.splotycode.mosaik.util.io.resource;

public enum SizeComputationComplexity implements Comparable<SizeComputationComplexity> {

    PRESENT,
    RETRIEVABLE,
    COUNTABLE;

    public static void main(String[] args) {
        System.out.println(PRESENT.compareTo(RETRIEVABLE));
    }

    boolean hasReached(SizeComputationComplexity computationComplexity) {
        return false;
       // return computationComplexity.complexity <= complexity;
    }
}
