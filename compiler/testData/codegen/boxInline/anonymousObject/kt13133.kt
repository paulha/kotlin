// WITH_REFLECT
// FILE: 1.kt

package test

inline fun inf(crossinline cif: Any.() -> Unit): () -> Unit {
    return {
        object : () -> Unit {
            override fun invoke() = cif()
        }
    }()
}
// FILE: 2.kt

import test.*

fun box(): String {
    inf {
        println(javaClass.name)

        // This fails with java.lang.InternalError: Malformed class name
        // the name is: ReproKt$main$$inlined$inf$lambda$1
        println(javaClass.simpleName)
    }()

    return "OK"
}

