package me.david.automatisation.age;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import me.david.automatisation.Generator;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class AgeGenerator implements Generator<Integer> {

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

    public static class AgeBuilder {

        public static AgeBuilder builder() {
            return new AgeBuilder();
        }

        private Set<AgeData> wights = new HashSet<>();

        public WightBuilder next() {
            return new WightBuilder();
        }

        public AgeGenerator build() {
            int sum = 0;
            for (AgeData data : wights)
                sum += data.probability;
            if (sum != 100) throw new IllegalArgumentException("Wights are not 100 in sum!");
            return new AgeGenerator(wights.toArray(new AgeData[wights.size()]));
        }

        private class WightBuilder {

            private AgeData data = new AgeData();

            public WightBuilder setRegion(AgeRegions region) {
                data.setRegion(region);
                return this;
            }

            public WightBuilder setProbability(int probability) {
                data.setProbability(probability);
                return this;
            }

            public AgeBuilder back() {
                if (data.probability == -2) throw new IllegalArgumentException("Probability not set");
                if (data.probability < 0) throw new IllegalArgumentException("Probability is negative");
                if (data.probability > 100) throw new IllegalArgumentException("Probability might not be greater then 100");

                if (data.region == null) throw new IllegalArgumentException("Region is not set");

                if (wights.stream().anyMatch(ageData -> ageData.region == data.region)) throw new IllegalArgumentException("Region is already used");
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

    @AllArgsConstructor
    @Getter
    public enum AgeRegions {

        CHILD(3, 12),
        TEENAGER(13, 18),
        JUNG_AGED(18, 24),
        MIDDLE_AGED(25, 36),
        OLD_AGED(37, 49),
        SUPER_OLD_AGED(50, 80);

        private final int minAge, maxAge;

    }

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
