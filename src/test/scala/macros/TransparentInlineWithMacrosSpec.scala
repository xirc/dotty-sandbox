package macros

import testing.BaseSpec

final class TransparentInlineWithMacrosSpec extends BaseSpec {

  import TransparentInlineWithMacros.*

  "int" in {

    val x: Int = defaultOf("int")
    assert(x == 1)

  }

  "string" in {

    val y: String = defaultOf("string")
    assert(y == "a")

  }

}
