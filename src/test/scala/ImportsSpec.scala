object ImportsSpec {

  object A:
    def *(a: Int, b: Int): Int = a * b
    def min(a: Int, b: Int): Int = math.min(a, b)

}

/** [[https://docs.scala-lang.org/scala3/reference/changed-features/imports.html Imports]] */
final class ImportsSpec extends BaseSpec {
  import ImportsSpec.A

  "Import everything" in {

    import A.*
    assert(*(3, 2) == 6)
    assert(min(3, 2) == 2)

  }

  "Import just '*'" in {

    import A.`*`

    assert(*(3, 2) == 6)
    assertDoesNotCompile("""
        |assert(min(3,2) == 2)
        |""".stripMargin)

  }

  "Renaming imports" in {

    import A.{min as minimum, `*` as multiply}
    assert(multiply(3, 2) == 6)
    assert(minimum(3, 2) == 2)

    import math.{max as _, *}
    assertDoesNotCompile("""
        |assert(max(3,2) == 3)
        |""".stripMargin)

  }

}
