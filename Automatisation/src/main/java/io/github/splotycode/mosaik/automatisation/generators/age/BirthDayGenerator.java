package io.github.splotycode.mosaik.automatisation.generators.age;

import io.github.splotycode.mosaik.automatisation.generators.Generator;
import io.github.splotycode.mosaik.util.time.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Gets a random Birth date
 */
@AllArgsConstructor
public class BirthDayGenerator implements Generator<BirthDayGenerator.BirthDay> {

    private AgeGenerator ageGenerator;

    @Getter private static BirthDayGenerator defaultGenerator = new BirthDayGenerator(AgeGenerator.getDefaultGenerator());

    /**
     * Generates a random Birth Date
     * @return the Birth Date
     */
    @Override
    public BirthDay getRandom() {
        int age = ageGenerator.getRandom();
        int now = Year.now().getValue();

        int year = now - age;
        Month month = Month.values()[ThreadLocalRandom.current().nextInt(0, 11)];
        int day = ThreadLocalRandom.current().nextInt(1, month.getMaxDays(year));

        return new BirthDay(year, day, month, TimeUtil.isLeapYear(year));
    }

    @Data
    @AllArgsConstructor
    static class BirthDay {

        private int year, day;
        private Month month;
        private boolean isLeapYear;

    }

    @AllArgsConstructor
    @SuppressWarnings("unused")
    public enum Month {

        JANUARY {
            @Override
            int getMaxDays(int year) {
                return 31;
            }
        },
        FEBRUARY {
            @Override
            int getMaxDays(int year) {
                return TimeUtil.isLeapYear(year) ? 29 : 28;
            }
        },
        MARCH {
            @Override
            int getMaxDays(int year) {
                return 31;
            }
        },
        APRIL {
            @Override
            int getMaxDays(int year) {
                return 30;
            }
        },
        MAY {
            @Override
            int getMaxDays(int year) {
                return 31;
            }
        },
        JUNE {
            @Override
            int getMaxDays(int year) {
                return 30;
            }
        },
        JULY {
            @Override
            int getMaxDays(int year) {
                return 31;
            }
        },
        AUGUST {
            @Override
            int getMaxDays(int year) {
                return 31;
            }
        },
        SEPTEMBER {
            @Override
            int getMaxDays(int year) {
                return 30;
            }
        },
        OCTOBER {
            @Override
            int getMaxDays(int year) {
                return 31;
            }
        },
        NOVEMBER {
            @Override
            int getMaxDays(int year) {
                return 30;
            }
        },
        DECEMBER {
            @Override
            int getMaxDays(int year) {
                return 31;
            }
        };

        public int getNumber() {
            return ordinal() + 1;
        }

        abstract int getMaxDays(int year);

    }
}
