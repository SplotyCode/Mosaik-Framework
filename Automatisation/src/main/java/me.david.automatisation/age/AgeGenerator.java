package me.david.automatisation.age;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import me.david.automatisation.Generator;

import java.util.concurrent.ThreadLocalRandom;

public class AgeGenerator implements Generator<Integer> {

    private AgeData[] wights;

    public AgeGenerator(AgeData... wights) {
        this.wights = wights;
        if (wights.length == 0) throw new IllegalArgumentException("Wights length is 0");
    }

    @Data
    public class AgeData {

        private AgeRegions region;
        private int probability;

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
        for (AgeData data : wights) {
            if (ThreadLocalRandom.current().nextInt(1, data.probability) == 1) {
                return ThreadLocalRandom.current().nextInt(data.region.minAge, data.region.maxAge);
            }
        }

        /* Fall back */
        AgeData data = wights.length == 1 ? wights[0] : wights[ThreadLocalRandom.current().nextInt(0, wights.length - 1)];
        return ThreadLocalRandom.current().nextInt(data.region.minAge, data.region.maxAge);
    }
}
