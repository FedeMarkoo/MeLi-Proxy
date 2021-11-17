package com.meli.fede.markoo.proxy.manager.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.reflections.Reflections;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.Column;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Log
public class ObjectGenerator {
    private static final HashMap<String, List> cache = new HashMap<>();
    private static final Faker faker = new Faker(new Locale("es"));
    private static final HashMap<Class, Reflections> reflectionCache = new HashMap<>();
    private static String delimiter;
    private static Properties prop;
    private static SimpleDateFormat sdf;
    private static java.util.Date dateMin;
    private static java.util.Date dateMax;
    private static int sequence = 1;

    static {
        setPropertiesPath("testfile.properties");
    }

    private static <E> E createContents(final Class<E> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        E instance = null;
        if (Modifier.isAbstract(clazz.getModifiers()) || clazz.isInterface()) {
            final Reflections reflections = getReflections(clazz);
            final Set<Class<? extends E>> subTypesOf = reflections.getSubTypesOf(clazz);
            final Iterator<Class<? extends E>> iterator = subTypesOf.iterator();
            while (iterator.hasNext() && instance == null) {
                try {
                    final Class<? extends E> next = iterator.next();
                    if (!Modifier.isAbstract(next.getModifiers()) && !next.isInterface()) {
                        final Constructor<? extends E> constructor = next.getConstructor();
                        if (constructor != null) {
                            instance = constructor.newInstance();
                        }
                    }
                } catch (final Exception e) {
                }
            }
        } else {
            Constructor<E> constructor;
            try {
                constructor = clazz.getConstructor();
                instance = constructor.newInstance();
            } catch (final Exception e) {
                try {
                    constructor = (Constructor<E>) clazz.getConstructors()[0];
                    final List<Object> params = new ArrayList<>();
                    for (final Class<?> pType : constructor.getParameterTypes()) {
                        params.add((pType.isPrimitive()) ? ClassUtils.primitiveToWrapper(pType).newInstance() : null);
                    }
                    instance = constructor.newInstance(params.toArray());
                } catch (final Exception e2) {
                    final Method build = clazz.getClasses()[0].getMethod("build");
                    final Object builder = clazz.getMethod("builder").invoke(null, null);
                    instance = (E) build.invoke(builder, null);
                }
            }
        }
        if (!cache.containsKey(clazz.getName())) {
            cache.put(clazz.getName(), new ArrayList<E>());
        }
        cache.get(clazz.getName()).add(instance);
        return instance;
    }

    private static <E> Reflections getReflections(final Class<E> clazz) {
        Reflections reflections = reflectionCache.get(clazz);
        if (reflections == null) {
            reflections = new Reflections(clazz.getPackage().getName());
            reflectionCache.put(clazz, reflections);
        }
        return reflections;
    }

    private static <E> E getFromCache(final Class<E> c) {
        final List<E> list = ObjectGenerator.cache.get(c.getName());
        if (list == null) {
            return null;
        }
        final Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    private static TreeSet<Field> getAllFields(final TreeSet<Field> fields, final Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    private static TreeSet<Field> getDeclaredFields(final Class c) {
        final TreeSet<Field> allFields = ObjectGenerator.getAllFields(new TreeSet<>(Comparator.comparing(Field::getName)), c);
        allFields.removeIf(Field::isSynthetic);
        allFields.removeIf(field -> Modifier.isFinal(field.getModifiers()));
        return allFields;
    }

    private static <E> boolean setValueByProperty(final E obj, final Field field, final Map<String, Object> valuedFields, final List<String> ignoredFields) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        if (prop == null) {
            return false;
        }
        final String fieldName = obj.getClass().getName() + "." + field.getName();
        String value = getProperty(fieldName);
        if (value != null) {
            return setValueFromProperty(obj, field, value);
        }
        value = prop.getProperty(obj.getClass().getSimpleName() + "." + field.getName());
        if (value != null) {
            return setValueFromProperty(obj, field, value);
        }
        value = getProperty(field.getName());
        if (value != null) {
            return setValueFromProperty(obj, field, value);
        }
        value = getProperty(obj.getClass().getSimpleName() + ".*");
        if (value != null) {
            return setValueFromProperty(obj, field, value);
        }
        return false;
    }

    private static <E> boolean setValueFromProperty(final E obj, final Field field, String value) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        if (value.contains(delimiter)) {
            final String[] split = value.split(delimiter);
            value = split[Math.min(split.length - 1, getCacheSize(obj))];
        }
        value = computeValueProperty(value);
        try {
            if (field.getType().isEnum()) {
                field.set(obj, Enum.valueOf((Class) field.getType(), value));
            } else {
                field.set(obj, field.getType().getConstructor(String.class).newInstance(value));
            }
            return true;
        } catch (final InvocationTargetException e) {
            log.warning(e.getTargetException().toString() + " in field " + obj.getClass().getName() + "." + field.getName());
        } catch (final Exception e) {
            log.warning(e.getMessage() + " in field " + obj.getClass().getName() + "." + field.getName());
        }
        return false;
    }

    private static <E> boolean setValueFromMap(final E obj, final Field field, final Object value) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        if (value instanceof String) {
            return setValueFromProperty(obj, field, (String) value);
        }
        try {
            field.set(obj, value);
            return true;
        } catch (final Exception e) {
            log.warning(e.getMessage() + " in field " + obj.getClass().getName() + "." + field.getName());
        }
        return false;
    }

    private static String computeValueProperty(String o) {
        final SimpleDateFormat sdf = getSimpleDateFormat();
        o = o.replace("{##}", getNumberFromSequence().toString());
        switch (o) {
            case "yesterday()":
                return sdf.format(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
            case "tomorrow()":
                return sdf.format(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));
            case "now()":
                return sdf.format(Date.from(Instant.now()));
            default:
                return o;
        }
    }

    private static Integer getNumberFromSequence() {
        return sequence++;
    }

    private static SimpleDateFormat getSimpleDateFormat() {
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        }
        return sdf;
    }

    private static <E> int getCacheSize(final E obj) {
        return cache.get(obj.getClass().getName()).size() - 1;
    }

    @SneakyThrows
    public static void setPropertiesPath(final String path) {
        prop = new Properties();
        try {
            final InputStream iStream = new ClassPathResource(path).getInputStream();
            prop.load(iStream);
        } catch (final Exception e) {
            log.warning(e.getMessage());
        }
        delimiter = prop.getProperty("delimiter", ",");
        reset();
    }

    private static <E> boolean setValueByType(final E obj, final Field field, final boolean forceValue, final boolean emptyValues, final boolean overflowedFields, final Map<String, Object> valuedFields, final List<String> ignoredFields) throws IllegalAccessException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, ParseException {
        final Class<?> type = field.getType();
        final String name = type.getName();

        int minLength = 0;
        int maxLength = 20;
        boolean nulleable = true;
        final Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
        if (declaredAnnotations != null) {
            for (final Annotation declaredAnnotation : declaredAnnotations) {
                if (declaredAnnotation instanceof Column) {
                    final Column colAnnot = (Column) declaredAnnotation;
                    maxLength = colAnnot.length();
                    if (overflowedFields) {
                        minLength = maxLength;
                        maxLength *= 1.5;
                    }
                    nulleable = nulleable && colAnnot.nullable();
                } else if (declaredAnnotation.annotationType().getName().equals("javax.validation.constraints.NotNull")) {
                    nulleable = false;
                }
            }
        }
        if (nulleable && faker.bool().bool() && !forceValue) {
            return true;
        }

        if ("java.lang.String".equals(name)) {
            field.set(obj, emptyValues ? "" : faker.lorem().characters(minLength, maxLength));
        } else if ("int".equals(name)) {
            field.setInt(obj, emptyValues ? 0 : faker.number().numberBetween(Integer.MIN_VALUE, Integer.MAX_VALUE));
        } else if ("long".equals(name)) {
            field.setLong(obj, emptyValues ? 0 : faker.number().numberBetween(Integer.MIN_VALUE, Integer.MAX_VALUE));
        } else if ("java.lang.Long".equals(name)) {
            field.set(obj, new Long(emptyValues ? 0 : faker.number().numberBetween(Integer.MIN_VALUE, Integer.MAX_VALUE)));
        } else if ("byte".equals(name)) {
            field.setByte(obj, emptyValues ? 0 : (byte) faker.number().numberBetween(Byte.MIN_VALUE, Byte.MAX_VALUE));
        } else if ("boolean".equals(name)) {
            field.setBoolean(obj, !emptyValues && faker.bool().bool());
        } else if ("java.util.Date".equals(name)) {
            field.set(obj, emptyValues ? new Date(0) : faker.date().between(getDateMin(), getDateMax()));
        } else if ("java.time.ZonedDateTime".equals(name)) {
            field.set(obj, ZonedDateTime.ofInstant(
                    (emptyValues ? Instant.ofEpochSecond(0) : faker.date().between(getDateMin(), getDateMax()).toInstant())
                    , ZoneId.systemDefault()));
        } else if ("java.time.LocalDate".equals(name)) {
            field.set(obj, java.time.LocalDate.ofEpochDay(
                    (emptyValues ? 0 : faker.date().between(getDateMin(), getDateMax()).toInstant().getEpochSecond())));
        } else if ("java.time.LocalDateTime".equals(name)) {
            field.set(obj, LocalDateTime.ofInstant(
                    (emptyValues ? Instant.ofEpochSecond(0) : faker.date().between(getDateMin(), getDateMax()).toInstant())
                    , ZoneId.systemDefault()));
        } else if ("java.util.List".equals(name)) {
            final ArrayList value = new ArrayList<>();
            if (!emptyValues) {
                final Type actualTypeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                value.add(createInstance((Class<?>) actualTypeArguments, false, forceValue, false, false, valuedFields, ignoredFields));
            }
            field.set(obj, value);
        } else if ("java.lang.Boolean".equals(name)) {
            field.set(obj, emptyValues ? false : type.getConstructor(boolean.class).newInstance(faker.bool().bool()));
        } else if ("java.lang.Integer".equals(name) || "java.lang.Double".equals(name) || "java.math.BigDecimal".equals(name)) {
            field.set(obj, emptyValues ? 0 : type.getConstructor(String.class).newInstance(faker.number().digits(3)));
        } else if (type.isEnum()) {
            field.set(obj, type.getEnumConstants()[emptyValues ? 0 : faker.random().nextInt(type.getEnumConstants().length)]);
        } else {
            return false;
        }
        return true;
    }

    private static java.util.Date getDateMin() throws ParseException {
        if (dateMin != null) {
            return dateMin;
        }
        final String prop = getProperty("dateMin");
        if (prop != null) {
            return getSimpleDateFormat().parse(computeValueProperty(prop));
        }
        dateMin = getSimpleDateFormat().parse("2000/01/01 00:00:00");
        return dateMin;
    }

    private static String getProperty(final String dateMin) {
        if (prop == null) {
            return null;
        }
        return ObjectGenerator.prop.getProperty(dateMin);
    }

    private static java.util.Date getDateMax() throws ParseException {
        if (dateMax != null) {
            return dateMax;
        }
        final String prop = getProperty("dateMax");
        if (prop != null) {
            return getSimpleDateFormat().parse(computeValueProperty(prop));
        }
        dateMax = java.util.Date.from(Instant.now());
        return dateMax;

    }

    public static <E> E createInstance(final Class<E> c) {
        final E instance = ObjectGenerator.createInstance(c, false, false, false, false, new HashMap<>(), new ArrayList<>());
        reset();
        return instance;
    }

    public static <E> E createInstance(final Class<E> c, final boolean emptyFields) {
        final E instance = ObjectGenerator.createInstance(c, false, false, emptyFields, false, new HashMap<>(), new ArrayList<>());
        reset();
        return instance;
    }

    public static <E> E createInstance(final Class<E> c, final boolean forceCreate, final boolean forceValue, final boolean emptyFields) {
        final E instance = ObjectGenerator.createInstance(c, forceCreate, forceValue, emptyFields, false, new HashMap<>(), new ArrayList<>());
        reset();
        return instance;
    }

    @SneakyThrows
    public static <E> E createInstance(final Class<E> c, final boolean forceCreate, final boolean forceValue, final boolean emptyFields,
                                       final boolean overflowedFields, final Map<String, Object> valuedFields, final List<String> ignoredFields) {
        E obj = null;
        if (!forceCreate) {
            obj = getFromCache(c);
            if (obj != null) {
                return obj;
            }
        }
        obj = ObjectGenerator.createContents(c);
        final TreeSet<Field> declaredFields = ObjectGenerator.getDeclaredFields(c);
        for (final Field field : declaredFields) {
            field.setAccessible(true);
            if (!ignoredFields.contains(field.getName()) && !ignoredFields.contains(field.getDeclaringClass().getSimpleName() + "." + field.getName())) {
                if (emptyFields || !ObjectGenerator.setValueByProperty(obj, field, valuedFields, ignoredFields)) {
                    if (!setValueByMap(obj, field, forceValue, emptyFields, overflowedFields, valuedFields, ignoredFields)) {
                        if (!setValueByName(obj, field, forceValue, emptyFields, overflowedFields, valuedFields, ignoredFields)) {
                            if (!setValueByType(obj, field, forceValue, emptyFields, overflowedFields, valuedFields, ignoredFields)) {
                                final Object instance = ObjectGenerator.createInstance(field.getType(), forceCreate, forceValue, emptyFields, overflowedFields, valuedFields, ignoredFields);
                                field.set(obj, instance);
                            }
                        }
                    }
                }
            }
        }
        return obj;
    }

    private static <E> boolean setValueByMap(final E obj, final Field field, final boolean forceValue, final boolean emptyValues, final boolean overflowedFields, final Map<String, Object> valuedFields, final List<String> ignoredFields) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final String name = field.getName();

        String fieldName = obj.getClass().getName() + "." + name;
        if (valuedFields.containsKey(fieldName)) {
            return setValueFromMap(obj, field, valuedFields.get(fieldName));
        }

        fieldName = obj.getClass().getSimpleName() + "." + name;
        if (valuedFields.containsKey(fieldName)) {
            return setValueFromMap(obj, field, valuedFields.get(fieldName));
        }

        if (valuedFields.containsKey(name)) {
            return setValueFromMap(obj, field, valuedFields.get(name));
        }

        fieldName = obj.getClass().getSimpleName() + ".*";
        if (valuedFields.containsKey(fieldName)) {
            return setValueFromMap(obj, field, valuedFields.get(fieldName));
        }
        return false;
    }

    private static <E> boolean setValueByName(final E obj, final Field field, final boolean forceValue, final boolean emptyValues, final boolean overflowedFields, final Map<String, Object> valuedFields, final List<String> ignoredFields) throws IllegalAccessException, JsonProcessingException {
        final Class<?> type = field.getType();
        final String name = type.getName();

        int minLength = 0;
        int maxLength = 20;
        boolean nulleable = true;
        final Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
        if (declaredAnnotations != null) {
            for (final Annotation declaredAnnotation : declaredAnnotations) {
                if (declaredAnnotation instanceof Column) {
                    final Column colAnnot = (Column) declaredAnnotation;
                    maxLength = colAnnot.length();
                    if (overflowedFields) {
                        minLength = maxLength;
                        maxLength *= 1.5;
                    }
                    nulleable = nulleable && colAnnot.nullable();
                } else if (declaredAnnotation.annotationType().getName().equals("javax.validation.constraints.NotNull")) {
                    nulleable = false;
                }
            }
        }
        if (nulleable && faker.bool().bool() && !forceValue) {
            return true;
        }

        if ("java.lang.String".equals(name)) {
            if (field.getName().contains("id")) {
                field.set(obj, emptyValues ? "" : UUID.randomUUID().toString());
            } else if (field.getName().contains("code") || field.getName().endsWith("Code")) {
                field.set(obj, emptyValues ? "" : RandomStringUtils.randomAlphanumeric(10).toUpperCase());
            } else if (field.getName().contains("description")) {
                field.set(obj, emptyValues ? "" : faker.lorem().sentence(1, 2));
            } else if (field.getName().endsWith("By")) {
                field.set(obj, emptyValues ? "" : faker.name().fullName());
            } else if (field.getName().endsWith("Number")) {
                field.set(obj, emptyValues ? "" : faker.number().digits(2));
            } else if (field.getName().toLowerCase().contains("json")) {
                final Map<String, Object> map = new HashMap<>();
                faker.lorem().words(new Random().nextInt(6)).forEach(a -> map.put(a, a));
                field.set(obj, emptyValues ? "" : new JsonMapper().writeValueAsString(map));
            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public static <E> E createOverflowedInstance(final Class<E> c) {
        final E instance = ObjectGenerator.createInstance(c, false, false, false, true, new HashMap<>(), new ArrayList<>());
        reset();
        return instance;
    }

    public static <E> E createInstanceFully(final Class<E> c) {
        final E instance = createInstance(c, true, true, false, false, new HashMap<>(), new ArrayList<>());
        reset();
        return instance;
    }

    public static <E> E createInstance(final Class<E> c, final Map<String, Object> valuedFields, final List<String> ignoredFields) {
        final E instance = createInstance(c, true, true, false, false, valuedFields, new ArrayList<>());
        reset();
        return instance;
    }

    public static <E> E createInstance(final Class<E> c, final Map<String, Object> valuedFields) {
        final E instance = createInstance(c, true, true, false, false, valuedFields, new ArrayList<>());
        reset();
        return instance;
    }

    public static <E> E createInstance(final Class<E> c, final List<String> ignoredFields) {
        final E instance = createInstance(c, true, true, false, false, new HashMap<>(), ignoredFields);
        reset();
        return instance;
    }

    public static void reset() {
        sequence = 1;
        cache.clear();
        dateMax = null;
        dateMin = null;
        delimiter = ",";
    }

    public static <E> E createInstance(final Class<E> c, final boolean emptyValues, final HashMap<String, Object> valuedFields) {
        final E instance = createInstance(c, false, false, emptyValues, false, valuedFields, new ArrayList<>());
        reset();
        return instance;
    }

    public static <E> List<E> createAllVariations(final Class<E> c) {
        final ArrayList<E> list = new ArrayList<>();
        final TreeSet<Field> fields = getAllFields(c);
        fields.forEach(field ->
                list.add(createInstance(c, Collections.singletonList(field.getName())))
        );
        return list;
    }

    private static <E> TreeSet<Field> getAllFields(final Class<E> c) {
        final TreeSet<Field> declaredFields = getDeclaredFields(c);
        final TreeSet<Field> declaredFieldsTemp = new TreeSet<>(Comparator.comparing(Field::getName));
        declaredFields.stream().map(field ->
        {
            Class<?> type = field.getType();
            if (Collection.class.isAssignableFrom(type)) {
                type = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            }
            return getAllFields(type);
        }).forEach(declaredFieldsTemp::addAll);
        declaredFields.addAll(declaredFieldsTemp);
        return declaredFields;
    }

    public static <E> List<E> createList(final Class<E> c, int i) {
        final ArrayList<E> list = new ArrayList<>();
        while (--i >= 0) {
            list.add(createInstance(c));
        }
        return list;
    }

    public static <E> List<E> createFullyList(final Class<E> c, int i) {
        final ArrayList<E> list = new ArrayList<>();
        while (--i >= 0) {
            list.add(createInstanceFully(c));
        }
        return list;
    }
}