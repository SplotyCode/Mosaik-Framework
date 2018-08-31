package me.david.davidlib.database.repo;

import lombok.AllArgsConstructor;
import lombok.Data;

public final class Filters {

    private enum FilterType {

        EQUAL,
        NOTEQUAL,
        GREATER,
        LESS,
        LESS_OR_EQUAL,
        GREATOR_OR_EQUAL

    }

    @Data
    @AllArgsConstructor
    public static class Filter {

        private String field;
        private FilterType type;
        private Object value;

    }

    public static Filter eq(String field, Object value) {
        return new Filter(field, FilterType.EQUAL, value);
    }

    public static Filter gt(String field, Object value) {
        return new Filter(field, FilterType.GREATER, value);
    }

    public static Filter lt(String field, Object value) {
        return new Filter(field, FilterType.LESS, value);
    }

    public static Filter gte(String field, Object value) {
        return new Filter(field, FilterType.GREATOR_OR_EQUAL, value);
    }

    public static Filter lte(String field, Object value) {
        return new Filter(field, FilterType.LESS_OR_EQUAL, value);
    }


}
