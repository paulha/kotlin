// WITH_REFLECT
// FULL_JDK

import java.lang.reflect.ParameterizedType
import java.lang.reflect.WildcardType
import java.lang.reflect.Type
import kotlin.reflect.createType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.jvm.javaType
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class Foo<T>

fun box(): String {
    assertEquals(String::class.java, String::class.createType().javaType)
    assertEquals(String::class.java, String::class.createType(nullable = true).javaType)

    val foo = Foo::class.createType(listOf(KTypeProjection.Star)).javaType
    if (foo !is ParameterizedType) fail("Not a parameterized type: $foo (${foo.javaClass})")
    assertEquals(Foo::class.java, foo.rawType)
    val fooArg = foo.actualTypeArguments.single()
    if (fooArg !is WildcardType) fail("Not a wildcard type: $fooArg (${fooArg.javaClass})")
    assertEquals(listOf(Any::class.java), fooArg.upperBounds.toList())
    assertEquals(listOf(), fooArg.lowerBounds.toList())

    // TODO: arrays

    return "OK"
}
