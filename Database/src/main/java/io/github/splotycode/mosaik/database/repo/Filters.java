package io.github.splotycode.mosaik.database.repo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Builds Filters for Quarry's
 * @see TableExecutor
 */
public final class Filters {

    public enum FilterType {

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
    public static abstract class Filter {

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

    /**
     * Builds a Filter that checks if a parameter is equals to a value
     * @param field the field name
     * @param value the value for comparing
     * @return the built filter
     */
    public static Filter eq(String field, Object value) {
        return new ValueFilter(field, FilterType.EQUAL, value);
    }

    /**
     * Builds a Filter that checks if a parameter is not equals to a value
     * @param field the field name
     * @param value the value for comparing
     * @return the built filter
     */
    public static Filter notEq(String field, Object value) {
        return new ValueFilter(field, FilterType.NOTEQUAL, value);
    }


    /**
     * Builds a Filter that checks if a parameter is greater then to a value
     * @param field the field name
     * @param value the value for comparing
     * @return the built filter
     */
    public static Filter gt(String field, Object value) {
        return new ValueFilter(field, FilterType.GREATER, value);
    }

    /**
     * Builds a Filter that checks if a parameter is lower then to a value
     * @param field the field name
     * @param value the value for comparing
     * @return the built filter
     */
    public static Filter lt(String field, Object value) {
        return new ValueFilter(field, FilterType.LESS, value);
    }

    /**
     * Builds a Filter that checks if a parameter is equals or greater then to a value
     * @param field the field name
     * @param value the value for comparing
     * @return the built filter
     */
    public static Filter gte(String field, Object value) {
        return new ValueFilter(field, FilterType.GREATER_OR_EQUAL, value);
    }

    /**
     * Builds a Filter that checks if a parameter is equals or lower then to a value
     * @param field the field name
     * @param value the value for comparing
     * @return the built filter
     */
    public static Filter lte(String field, Object value) {
        return new ValueFilter(field, FilterType.LESS_OR_EQUAL, value);
    }

    /**
     * Builds a Filter out of other filters ALL of the filters must be true for a positive filter result
     * @param filters filters that will be combined
     * @return the built filter
     */
    public static Filter and(Filter... filters) {
        return new ComplexFilter(FilterType.AND, filters);
    }

    /**
     * Builds a Filter out of other filters ANY of the filters must be true for a positive filter result
     * @param filters filters that will be combined
     * @return the built filter
     */
    public static Filter or(Filter... filters) {
        return new ComplexFilter(FilterType.OR, filters);
    }

}
