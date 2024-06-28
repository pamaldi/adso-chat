package cloud.isaura.adso.chat.utils;

import java.util.Collection;

public class ValidationUtils
{

    public static String ensureNotBlank(String string, String name) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException(name+" cannot be null or blank");
        }

        return string;
    }

    public static Object ensureNotNull(Object obj, String name) {
        if (obj == null ) {
            throw new IllegalArgumentException(name+" cannot be null or blank");
        }

        return obj;
    }

    public static <T extends Collection<?>> T ensureNotEmpty(T collection, String name) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(name+" cannot be null or blank");
        }

        return collection;
    }

}
