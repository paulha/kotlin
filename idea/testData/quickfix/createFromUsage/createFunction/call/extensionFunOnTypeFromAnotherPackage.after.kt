// "Create extension function 'A.foo'" "true"
// ERROR: Unresolved reference: foo

import package1.A

class X {
    init {
        val y = package2.A()
        y.foo()
    }
}

fun package2.A.foo() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
