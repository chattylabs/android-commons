package chattylabs.android.commons;

import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class UnitTestUtils {

    public static void setVersionSdk(int level) throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), level);
    }

    public static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

}
