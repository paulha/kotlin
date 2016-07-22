// WITH_REFLECT

import kotlin.reflect.KTypeProjection
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultBound<T>
class NullableAnyBound<T : Any?>
class NotNullAnyBound<T : Any>

class OtherParameterBound<T : U, U : Number>

class RecursiveGeneric<T : Enum<T>>

class FunctionTypeParameter {
    fun <A : Cloneable> foo(): Cloneable = null!!
}

// helper functions to obtain KType instances
fun nullableAny(): Any? = null
fun notNullAny(): Any = null!!

fun box(): String {
    assertEquals(listOf(::nullableAny.returnType), DefaultBound::class.typeParameters.single().upperBounds)
    assertEquals(listOf(::nullableAny.returnType), NullableAnyBound::class.typeParameters.single().upperBounds)
    assertEquals(listOf(::notNullAny.returnType), NotNullAnyBound::class.typeParameters.single().upperBounds)

    OtherParameterBound::class.typeParameters.let {
        val (t, u) = it
        assertEquals(u, t.upperBounds.single().classifier)
        assertEquals(Number::class, u.upperBounds.single().classifier)
    }

    FunctionTypeParameter::class.members.single { it.name == "foo" }.let { foo ->
        assertEquals(foo.returnType, foo.typeParameters.single().upperBounds.single())
    }

    val recursiveGenericTypeParameter = RecursiveGeneric::class.typeParameters.single()
    val recursiveGenericBound = recursiveGenericTypeParameter.upperBounds.single()
    assertEquals(Enum::class, recursiveGenericBound.classifier)
    recursiveGenericBound.arguments.single().let { projection ->
        assertTrue(projection is KTypeProjection.Invariant)
        val type = projection.type!!
        assertEquals(recursiveGenericTypeParameter, type.classifier)
    }

    return "OK"
}
