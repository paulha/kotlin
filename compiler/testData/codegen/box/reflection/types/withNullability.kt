// WITH_REFLECT

import kotlin.reflect.withNullability
import kotlin.test.assertEquals

fun nonNull(): String = ""
fun nullable(): String? = ""

fun box(): String {
    val nonNull = ::nonNull.returnType
    val nullable = ::nullable.returnType

    assertEquals(nonNull, nullable.withNullability(false))
    assertEquals(nullable, nullable.withNullability(true))
    assertEquals(nonNull, nonNull.withNullability(false))
    assertEquals(nullable, nonNull.withNullability(true))

    return "OK"
}
