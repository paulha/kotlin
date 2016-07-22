fun box(): String {
    var encl1 = "fail";
    test {
        {
            {
                encl1 = "OK"
            }()
        }()
    }

    return encl1
}

inline fun test(crossinline s: () -> Unit) {
    {
        {
            s()
        }()
    }()
}

// 3 INNERCLASS Kt10259_3Kt\$test\$1 null
// 2 INNERCLASS Kt10259_3Kt\$test\$1\$1
// 8 INNERCLASS Kt10259_3Kt\$box\$\$inlined\$test\$1
// 6 INNERCLASS Kt10259_3Kt\$box\$\$inlined\$test\$1\$1
// inlined:
// 4 INNERCLASS Kt10259_3Kt\$box\$\$inlined\$test\$1\$1\$lambda\$1
// 2 INNERCLASS Kt10259_3Kt\$box\$\$inlined\$test\$1\$1\$lambda\$1\$1
// 13 INNERCLASS