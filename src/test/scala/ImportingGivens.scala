object ImportingGivens {

  trait Show[A]:
    def show(value: A): String

  def show[A: Show](value: A): String =
    summon[Show[A]].show(value)

  object MyShow:
    given Show[Int] with
      def show(value: Int): String =
        value.toString

    given Show[String] with
      def show(value: String): String =
        value

}

/** [[https://dotty.epfl.ch/docs/reference/contextual/given-imports.html Importing Givens]] */
final class ImportingGivens extends BaseSpec {
  import ImportingGivens.*

  "Import using given" in {

    // "*" does not import given instance, use "given" instead.
    // import MyShow.*
    import MyShow.given
    assert(show(1) == "1")
    assert(show("abc") == "abc")

  }

  "Import by type" in {

    import MyShow.given Show[Int]
    assert(show(1) == "1")
    assertDoesNotCompile(
      """
        |show("abc") == "abc"
        |""".stripMargin,
    )

  }

  "Import by type with wildcard arguments" in {

    import MyShow.given Show[?]
    assert(show(1) == "1")
    assert(show("abc") == "abc")

  }

}
