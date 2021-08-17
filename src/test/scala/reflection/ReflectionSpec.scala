package reflection

import testing.BaseSpec

/** [[https://docs.scala-lang.org/scala3/reference/metaprogramming/reflection.html Reflection]] */
final class ReflectionSpec extends BaseSpec {

  import reflection.Reflection.*

  "using Extractors" in {

    assert(natConst(1) == 1)

    // compile error
    // assert(natConst(0) == 0)

    // compile error
    // val x = 0
    // assert(natConst(x) == 0)

  }

}
