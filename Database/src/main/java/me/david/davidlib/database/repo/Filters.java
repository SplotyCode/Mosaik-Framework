package me.david.davidlib.database.repo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public final class Filters {

    private enum FilterType {

        EQUAL,
        NOTEQUAL,
        GREATER,
        LESS,
        LESS_OR_EQUAL,
        GREATER_OR_EQUAL,

        /* Complex */
        AND,
        OR

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Filter {

        protected FilterType type;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ValueFilter extends Filter {

        protected String field;
        private Object object;

        public ValueFilter(String field, FilterType type, Object object) {
            super(type);
            this.field = field;
            this.object = object;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ComplexFilter extends Filter {

        private Filter[] filters;

        public ComplexFilter(FilterType type, Filter... filters) {
            super(type);
            this.filters = filters;
        }
    }

    public static Filter eq(String field, Object value) {
        return new ValueFilter(field, FilterType.EQUAL, value);
    }

    public static Filter notEq(String field, Object value) {
        return new ValueFilter(field, FilterType.NOTEQUAL, value);
    }


    public static Filter gt(String field, Object value) {
        return new ValueFilter(field, FilterType.GREATER, value);
    }

    public static Filter lt(String field, Object value) {
        return new ValueFilter(field, FilterType.LESS, value);
    }

    public static Filter gte(String field, Object value) {
        return new ValueFilter(field, FilterType.GREATER_OR_EQUAL, value);
    }

    public static Filter lte(String field, Object value) {
        return new ValueFilter(field, FilterType.LESS_OR_EQUAL, value);
    }

    public static Filter and(Filter... filters) {
        return new ComplexFilter(FilterType.AND, filters);
    }

    public static Filter or(Filter... filters) {
        return new ComplexFilter(FilterType.OR, filters);
    }

}
