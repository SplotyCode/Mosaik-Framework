package io.github.splotycode.mosaik.automatisation.generators.age;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import io.github.splotycode.mosaik.automatisation.generators.Generator;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates a random age
 */
public class AgeGenerator implements Generator<Integer> {

    /**
     * The default age Generator instance
     */
    @Getter private static AgeGenerator defaultGenerator = AgeBuilder.builder().next()
                                                                    .setRegion(AgeRegions.CHILD)
                                                                    .setProbability(4)
                                                                .back()
                                                                .next()
                                                                    .setRegion(AgeRegions.TEENAGER)
                                                                    .setProbability(14)
                                                                .back()
                                                                .next()
                                                                    .setRegion(AgeRegions.JUNG_AGED)
                                                                    .setProbability(32)
                                                                .back()
                                                                .next()
                                                                    .setRegion(AgeRegions.MIDDLE_AGED)
                                                                    .setProbability(16)
                                                                .back()
                                                                .next()
                                                                    .setRegion(AgeRegions.OLD_AGED)
                                                                    .setProbability(26)
                                                                .back()
                                                                .next()
                                                                    .setRegion(AgeRegions.SUPER_OLD_AGED)
                                                                    .setProbability(7)
                                                                .back()
                                                                .build();


    private AgeData[] wights;

    public AgeGenerator(AgeData... wights) {
        this.wights = wights;
        if (wights.length == 0) throw new IllegalArgumentException("Wights length is 0");
    }

    /**
     * Makes it easier to create an AgeGenerator
     */
    public static class AgeBuilder {

        /**
         * Creates a new AgeBuilder
         * @return the new builder
         */
        public static AgeBuilder builder() {
            return new AgeBuilder();
        }

        private Set<AgeData> wights = new HashSet<>();

        /**
         * Symbolises that you want to add a wight
         * @return a new WightBuilder
         */
        public WightBuilder next() {
            return new WightBuilder();
        }

        /**
         * Generates the AgeGenerator
         * @return te AgeGenerator
         */
        public AgeGenerator build() {
            int sum = 0;
            for (AgeData data : wights)
                sum += data.probability;
            if (sum != 100) throw new IllegalArgumentException("Wights are not 100 in sum!");
            return new AgeGenerator(wights.toArray(new AgeData[wights.size()]));
        }

        private class WightBuilder {

            private AgeData data = new AgeData();

            /**
             * Sets the Region for this wight
             * @param region the Region
             * @return the current WeightBuilder
             */
            public WightBuilder setRegion(AgeRegions region) {
                data.setRegion(region);
                return this;
            }

            /**
             * Sets an Probability for this wight
             * @param probability the probability
             * @return the current WeightBuilder
             */
            public WightBuilder setProbability(int probability) {
                data.setProbability(probability);
                return this;
            }

            /**
             * Returns the AgeBuilder that has created this weight builder
             * @return the AgeBuilder
             * @throws IllegalArgumentException when the Region/Probability is not set or the Probability is not in range(0 - 100)
             * @throws IllegalStateException when the weight already exists in the AgeBuilder
             */
            public AgeBuilder back() {
                if (data.probability == -2) throw new IllegalArgumentException("Probability not set");
                if (data.probability < 0) throw new IllegalArgumentException("Probability is negative");
                if (data.probability > 100) throw new IllegalArgumentException("Probability might not be greater then 100");

                if (data.region == null) throw new IllegalArgumentException("Region is not set");

                if (wights.stream().anyMatch(ageData -> ageData.region == data.region)) throw new IllegalStateException("Region is already used");
                wights.add(data);
                return AgeBuilder.this;
            }

        }
    }

    @Data
    public static class AgeData {

        private AgeRegions region = null;
        private int probability = -2;

    }

    /**
     * Different Age Regions(Child, Teenager etc.)
     */
    @AllArgsConstructor
    @Getter
    public enum AgeRegions {

        /**
         * From 3 to 12
         */
        CHILD(3, 12),
        /**
         * From 13 to 18
         */
        TEENAGER(13, 18),
        /**
         * From 19 to 24
         */
        JUNG_AGED(19, 24),
        /**
         * From 25 to 36
         */
        MIDDLE_AGED(25, 36),
        /**
         * From 37 to 49
         */
        OLD_AGED(37, 49),
        /**
         * From 50 to 80
         */
        SUPER_OLD_AGED(50, 80);

        private final int minAge, maxAge;

    }

    /**
     * Gets an random age depending on the wights
     * @return te age
     */
    @Override
    public Integer getRandom() {
        int random = ThreadLocalRandom.current().nextInt(0, 100);
        int sum = 0;
        for (AgeData data : wights) {
            if (random >= sum && random <= sum + data.probability) {
                return ThreadLocalRandom.current().nextInt(data.region.minAge, data.region.maxAge);
            }
            sum += data.probability;
        }

        /* Fall back */
        AgeData data = wights.length == 1 ? wights[0] : wights[ThreadLocalRandom.current().nextInt(0, wights.length - 1)];
        return ThreadLocalRandom.current().nextInt(data.region.minAge, data.region.maxAge);
    }
}
