package dev.cobblesword.libraries.common;

import java.lang.reflect.Field;

public class Reflection {
    /**
     * Gets a Field object for a given class and field name.
     *
     * @param clazz the Class object
     * @param fieldName the name of the field
     * @return a Field object for the given class and field name
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true); // Make the field accessible if it's private
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}
