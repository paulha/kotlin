// WITH_REFLECT
// FILE: J.java

import java.util.List;

public class J {
    public static String string() {
        return "";
    }

    public static List<Object> list() {
        return null;
    }
}

// FILE: K.kt

import kotlin.test.assertEquals

fun box(): String {
    assertEquals(String::class, J::string.returnType.classifier)
    assertEquals(List::class, J::list.returnType.classifier)

    return "OK"
}
