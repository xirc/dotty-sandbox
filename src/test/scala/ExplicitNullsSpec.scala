/** [[https://dotty.epfl.ch/docs/reference/other-new-features/explicit-nulls.html Explicit Nulls]]
  */
final class ExplicitNullsSpec extends BaseSpec {

  "working with nulls" in {

    val x: String | Null = null
    assert(x == null)

    val y: String = "abc"
    assert(y == "abc")
    assertDoesNotCompile(
      """
        |assert(y != null)
        |""".stripMargin,
    )
    // we have to provide a type hint if we really want to compare null with non-null value
    assert((y: String | Null) != null)
    assert((y: Any) != null)

    assertDoesNotCompile(
      """
        |val z: String = null
        |""".stripMargin,
    )

  }

  "Flow Typing" in {

    val s1: String | Null = null
    if s1 != null then assert(s1 == "") else assert(s1 == null)

    val s2: String | Null = ""
    if s2 != null then assert(s2 == "") else assert(s2 == null)

  }

  "Logical Operators (&&)" in {

    val s: String | Null = "abc"
    val t: String = if s != null && s.length > 0 then "nonEmpty" else "isEmpty"
    assert(t == "nonEmpty")

  }

  "Logical Operators (||)" in {

    val s: String | Null = ""
    val t: String = if s == null || s.length == 0 then "isEmpty" else "nonEmpty"
    assert(t == "isEmpty")

  }

  "Match Case" in {

    val s: String | Null = "abc"
    val t = s match
      case _: String => "not null"
      case _         => "null"
    assert(t == "not null")

  }

}
