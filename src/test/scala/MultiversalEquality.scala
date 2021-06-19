object MultiversalEquality {

  final case class ClassA(value: Int)
  final case class ClassB(value: String)

  final case class ClassC(value: Int) derives CanEqual
  final case class ClassD(value: String) derives CanEqual

  final case class ClassE(value: Int) derives CanEqual
  given CanEqual[ClassC, ClassE] = CanEqual.derived
  given CanEqual[ClassE, ClassC] = CanEqual.derived

}

/** [[https://dotty.epfl.ch/docs/reference/contextual/multiversal-equality.html Multiversal Equality]]
  */
final class MultiversalEquality extends BaseSpec {
  import MultiversalEquality._

  "non-strict" in {

    assert(ClassA(1) != ClassB("abc"))

  }

  "strict" in {

    import scala.language.strictEquality

    assertDoesNotCompile(
      """
        |assert(ClassA(1) != ClassB("abc"))
        |""".stripMargin,
    )

    assertDoesNotCompile(
      """
        |assert(ClassC(1) != ClassD("abc"))
        |""".stripMargin,
    )

    // ClassC
    assert(ClassC(1) == ClassC(1))
    assert(ClassC(2) != ClassC(1))

    // ClassD
    assert(ClassD("a") == ClassD("a"))
    assert(ClassD("a") != ClassD("b"))

    // ClassE
    assert(ClassE(1) == ClassE(1))
    assert(ClassE(1) != ClassE(2))

    // ClassC & ClassE
    assert(ClassC(1) != ClassE(1))
    assert(ClassC(1) != ClassE(2))

    // ClassE & ClassC
    assert(ClassE(1) != ClassC(1))
    assert(ClassE(1) != ClassC(2))

  }

}
