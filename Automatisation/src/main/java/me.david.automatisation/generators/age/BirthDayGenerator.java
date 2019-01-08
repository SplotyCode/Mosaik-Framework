package me.david.automatisation.generators.age;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import me.david.automatisation.generators.Generator;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Gets a random Birth date
 */
@AllArgsConstructor
public class BirthDayGenerator implements Generator<BirthDayGenerator.BirthDay> {

    /**
     * Checks is a year is a leap year
     * @param year the year as an number (for example 2004)
     * @return true if the year is an leap year or else false
     * TODO: Move to Utils
     */
    public static boolean isLeapYear(int year) {
        if (year % 4 != 0) {
            return false;
        } else if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else {
            return true;
        }
    }

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

        return new BirthDay(year, day, month, isLeapYear(year));
    }

    @Data
    @AllArgsConstructor
    public static class BirthDay {

        private int year, day;
        private Month month;
        private boolean isLeapYear;

    }

    @AllArgsConstructor
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
                return isLeapYear(year) ? 29 : 28;
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
        SEPTEMPER {
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
