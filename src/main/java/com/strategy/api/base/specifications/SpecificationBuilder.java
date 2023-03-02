package com.strategy.api.base.specifications;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 4/21/18.
 */
public class SpecificationBuilder<T> {
    private Class<T> clazz;
    private Map<Condition, Predicate> predicateMap;
    private static final Map<String, Condition> SYMBOL_MAP = new HashMap<>() {{
        put("~", Condition.OR);
        put("", Condition.AND);
    }};

    private static final Map<String, Operator> OPERATOR_MAP = new HashMap<>() {{
        put("", Operator.EQUAL);
        put("<", Operator.LESS_THAN);
        put("<:", Operator.LESS_AND_EQUAL);
        put(">", Operator.GREATER_THAN);
        put(">:", Operator.GREATER_AND_EQUAL);
        put("in", Operator.IN);
        put("!:", Operator.NOT_EQUAL);
        put("!", Operator.NOT);
        put("null", Operator.IS_NULL);
        put("!null", Operator.IS_NOT_NULL);
        put("like", Operator.LIKE);
        put("eq", Operator.STRING_EQUAL);

    }};

    private static final String START_OPERATOR_CHAR = "(";
    private static final String END_OPERATOR_CHAR = ")";


    public SpecificationBuilder(final Class<T> clazz) {
        this.clazz = clazz;
        this.predicateMap = new HashMap<>();
    }

    private SpecificationBuilder() {
    }

    public Specification<T> buildFromQueryMap(final Map<String, Object> queryMap) {
        return (final Root<T> root, final CriteriaQuery<?> criteria, final CriteriaBuilder builder) -> {
            Predicate predicate;

            for (Map.Entry<String, Object> entry : queryMap.entrySet()) {

                if (entry.getKey().startsWith("_"))
                    continue;

                try {
                    Condition condition = this.getConditionFromKey(entry.getKey());
                    final String tmpEntryKey = this.clearFieldKey(entry.getKey());
                    Operator operator = this.getOperatorFromKey(tmpEntryKey);
                    final String entryKey = this.clearOperatorFromFieldKey(tmpEntryKey);
                    predicate = this.getPredicate(condition, builder);
                    Predicate tmpPredicate;
                    Path<String> path;
                    Field entityField;


                    if (entry.getKey().indexOf(".") > -1) {

                        final String[] split = entryKey.split("\\.");
                        int fieldPropertyIndex = split.length - 1;
                        final String searchFieldName = split[fieldPropertyIndex];

                        Field f = getClassField(split[0], this.clazz);
                        Class fieldClass = getFieldType(f);
                        entityField = getClassField(Arrays.copyOfRange(split, 1, split.length), fieldClass);
                        Join<T, T> joinPaths = constructJoinPath(root, Arrays.copyOfRange(split, 0, split.length - 1));

                        path = joinPaths.get(searchFieldName);

                    } else {
                        path = root.get(entryKey);
                        entityField = this.clazz.getDeclaredField(entryKey);
                    }


                    if (entityField.getType() instanceof Class && entityField.getType().isEnum()) {
                        tmpPredicate = this.constructPredicateConditionBase(condition, builder, path,
                                entry.getValue(), //Enum.valueOf((Class<Enum>) entityField.getType(), entry.getValue().toString())
                                operator,
                                entityField.getType());

                    } else
                        switch (entityField.getType().getSimpleName()) {
                            case "String":
                                operator = operator.equals(Operator.STRING_EQUAL) ? Operator.STRING_EQUAL : Operator.LIKE;
                                tmpPredicate = this.constructPredicateConditionBase(condition, builder, path,
                                        entry.getValue().toString().toLowerCase(), operator, String.class);
                                break;
                            case "int":
                            case "Integer":
                            case "long":
                            case "Long":
                                Object value = NumberUtils.isNumber(entry.getValue().toString()) ? entry.getValue() : Integer.MAX_VALUE;
                                tmpPredicate = this.constructPredicateConditionBase(condition, builder, path,
                                        value, operator, Long.class);
                                break;
                            case "Boolean":
                            case "boolean":
                                tmpPredicate = this.constructPredicateConditionBase(condition, builder, path,
                                        Boolean.parseBoolean(entry.getValue().toString()), operator, Boolean.class);
                                break;
                            case "Date":
                                tmpPredicate = this.constructPredicateConditionBase(condition, builder, path,
                                        entry.getValue(), operator, Date.class);
                                break;
                            case "Float":
                            case "float":
                                tmpPredicate = this.constructPredicateConditionBase(condition, builder, path,
                                        entry.getValue(), operator, Float.class);
                                break;
                            case "Double":
                            case "double":
                                tmpPredicate = this.constructPredicateConditionBase(condition, builder, path,
                                        entry.getValue(), operator, Double.class);
                                break;
                            default:
                                tmpPredicate = this.constructPredicateConditionBase(condition, builder, path,
                                        entry.getValue(), operator, String.class);
                        }

                    predicate.getExpressions().add(tmpPredicate);
                    this.predicateMap.put(condition, predicate);

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

            }

            return this.joinPredicates();
        };
    }

    private Field getClassField(final String fieldName, final Class fieldClass){


        try {
            return fieldClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e){
            if(fieldClass.getSuperclass() != null)
               return getClassField(fieldName, fieldClass.getSuperclass());

            throw new RuntimeException();
        }

    }

    private Field getClassField(final String[] fieldNames, final Class fieldClass) {
        Field f2 = null;
        Class fieldClassHolder = fieldClass;

        for (String s : fieldNames) {
            boolean hasSuperClass = true;
            while (hasSuperClass) {
                try {
                    f2 = fieldClassHolder.getDeclaredField(s);
                    hasSuperClass = false;
                } catch (NoSuchFieldException e) {
                    hasSuperClass = true;
                    fieldClassHolder = fieldClassHolder.getSuperclass();
                }
            }

            fieldClassHolder = getFieldType(f2);
        }

        return f2;
    }

    private Class getFieldType(final Field field) {
        if (field.getGenericType() != null && field.getGenericType() instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) field.getGenericType();
            try {
                return Class.forName(p.getActualTypeArguments()[0].getTypeName());
            } catch (ClassNotFoundException e) {
                return null;
            }
        } else
            return field.getType();
    }

    private Join<T, T> constructJoinPath(final Root<T> root, final String[] entitiesName) {
        Join<T, T> joinPath = root.join(entitiesName[0],JoinType.LEFT);

        for (String s : Arrays.copyOfRange(entitiesName, 1, entitiesName.length)) {
            joinPath = joinPath.join(s,JoinType.LEFT);
        }

        return joinPath;
    }

    private Condition getConditionFromKey(final String fieldKey) {
        String conditionKey = fieldKey.substring(fieldKey.length() - 1);
        if (!SYMBOL_MAP.containsKey(conditionKey)) {
            conditionKey = "";
        }

        return SYMBOL_MAP.get(conditionKey);
    }

    private String clearFieldKey(final String fieldKey) {
        final String key = fieldKey.substring(fieldKey.length() - 1);

        if (SYMBOL_MAP.containsKey(key))
            return fieldKey.substring(0, fieldKey.length() - 1);

        return fieldKey;
    }

    private String getOperatorKey(final String fieldKey) {
        return (fieldKey.contains(START_OPERATOR_CHAR) && fieldKey.contains(END_OPERATOR_CHAR))
                ? fieldKey.substring(fieldKey.indexOf(START_OPERATOR_CHAR) + 1, fieldKey.indexOf(END_OPERATOR_CHAR))
                : "";
    }

    private Operator getOperatorFromKey(final String fieldKey) {
        final String operatorKey = getOperatorKey(fieldKey);

        return OPERATOR_MAP.get(operatorKey);
    }

    private String clearOperatorFromFieldKey(final String fieldKey) {
        final String replaceKey = (fieldKey.contains(START_OPERATOR_CHAR) && fieldKey.contains(END_OPERATOR_CHAR))
                ? fieldKey.substring(fieldKey.indexOf(START_OPERATOR_CHAR), fieldKey.indexOf(END_OPERATOR_CHAR) + 1)
                : "";

        return fieldKey.replace(replaceKey, "");
    }

    private Predicate getPredicate(final Condition condition, final CriteriaBuilder builder) {

        if (condition == Condition.OR) {
            if (this.predicateMap.containsKey(condition))
                return this.predicateMap.get(condition);

            this.predicateMap.put(Condition.OR, builder.disjunction());
            return builder.disjunction();
        }

        if (this.predicateMap.containsKey(condition))
            return this.predicateMap.get(condition);

        this.predicateMap.put(Condition.AND, builder.conjunction());
        return builder.conjunction();
    }

    private Predicate constructPredicateConditionBase(final Condition condition, final CriteriaBuilder builder,
                                                      final Path path, final Object fieldValue,
                                                      final Operator operator, final Class clazz) {
        Predicate predicate;
        switch (operator) {
            case IN:
                return this.constructPredicateConditionBase(condition, builder, path, this.createList(fieldValue.toString(), clazz));
            case LESS_THAN:
                predicate = builder.lessThan(path, (Comparable) parse(fieldValue.toString(), clazz));
                break;
            case LESS_AND_EQUAL:
                predicate = builder.lessThanOrEqualTo(path, (Comparable) parse(fieldValue.toString(), clazz));
                break;
            case GREATER_THAN:
                predicate = builder.greaterThan(path, (Comparable) parse(fieldValue.toString(), clazz));
                break;
            case GREATER_AND_EQUAL:
                predicate = builder.greaterThanOrEqualTo(path, (Comparable) parse(fieldValue.toString(), clazz));
                break;
            case NOT_EQUAL:
                predicate = builder.notEqual(path, parse(fieldValue.toString(), clazz));
                break;
            case NOT:
                predicate = builder.not(path);
                break;
            case IS_NULL:
                predicate = builder.isNull(path);
                break;
            case IS_NOT_NULL:
                predicate = builder.isNotNull(path);
                break;
            case LIKE:
                predicate = builder.like(builder.lower(path), "%" + fieldValue.toString().toLowerCase() + "%");
                break;
            case STRING_EQUAL:
                predicate = builder.equal(path, parse(fieldValue.toString(), clazz));
                break;
            default:
                predicate = builder.equal(path, parse(fieldValue.toString(), clazz));
                break;
        }

        if (condition == Condition.OR) {
            return builder.or(predicate);
        }

        return builder.and(predicate);
    }

    private List<Object> createList(final String value, final Class clazz) {
        List<Object> list = new ArrayList<>();
        for (String s : value.split(",")) {
            list.add(parse(s, clazz));
        }

        return list;
    }

    private Object parse(final String value, final Class clazz) {

        final String trimmedValue = value.trim();

        if (clazz.equals(String.class))
            return trimmedValue;

        if (clazz.equals(Long.class))
            return Long.parseLong(trimmedValue);

        if (clazz.equals(Boolean.class))
            return Boolean.parseBoolean(trimmedValue);

        if (clazz.equals(Date.class))
            return formatDate(trimmedValue);

        if (clazz.isEnum())
            return Enum.valueOf((Class<Enum>) clazz, trimmedValue);

        return trimmedValue;
    }

    private Date formatDate(final String value) {
        List<String> dateFormats = List.of("yyyy-MM-dd HH:mm:ss","yyyy-MM-dd");

        for (String formatString : dateFormats)
        {
            try
            {
                return new SimpleDateFormat(formatString).parse(value);
            }
            catch (ParseException e) {}
        }

        throw new RuntimeException(String.format("Invalid date format. Accepted formats are %s",String.join(", ",dateFormats)));
    }

    private Predicate constructPredicateConditionBase(final Condition condition, final CriteriaBuilder builder,
                                                      final Path path, final List<Object> fieldValue) {
        CriteriaBuilder.In<Object> in = builder.in(path);

        fieldValue.stream().forEach(in::value);

        if (condition == Condition.OR) {
            return builder.or(in);
        }

        return builder.and(in);
    }

    private Predicate joinPredicates() {
        Predicate predicate = (this.predicateMap.containsKey(Condition.AND))
                ? this.predicateMap.get(Condition.AND)
                : this.predicateMap.get(Condition.OR);

        this.predicateMap.entrySet().stream()
                .filter(e -> !e.getValue().equals(predicate))
                .forEach(e -> predicate.getExpressions().add(e.getValue()));

        return predicate;
    }

    public enum Condition {
        AND, OR
    }

    public enum Operator {
        EQUAL, GREATER_THAN, GREATER_AND_EQUAL, LESS_THAN, LESS_AND_EQUAL, IN, NOT_EQUAL, NOT, IS_NULL, IS_NOT_NULL, LIKE, STRING_EQUAL
    }
}

