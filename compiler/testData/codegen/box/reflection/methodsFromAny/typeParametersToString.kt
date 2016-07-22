// WITH_REFLECT

import kotlin.test.assertEquals

interface Variance<A, in B, out C, D>
class OneBound<T : Enum<T>>
class SeveralBounds<T : Cloneable> where T : Enum<T>, T : Variance<String, Int?, Double?, Number>

fun box(): String {
    assertEquals("[A : kotlin.Any?, in B : kotlin.Any?, out C : kotlin.Any?, D : kotlin.Any?]", Variance::class.typeParameters.toString())
    assertEquals("[T : kotlin.Enum<T>]", OneBound::class.typeParameters.toString())
    assertEquals("[T : kotlin.Cloneable & kotlin.Enum<T> & Variance<kotlin.String, kotlin.Int?, kotlin.Double?, kotlin.Number>]", SeveralBounds::class.typeParameters.toString())

    return "OK"
}
