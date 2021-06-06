object UniversalApplyMethodsSpec {

  // All constructor proxies of ClassA are created.
  class ClassA(val value: Int):
    def this() = this(0)

  // Any constructor proxy of ClassB is not created
  // since a method named `ClassB` (which is not a companion object name) is already defined.
  class ClassB(val value: Int):
    def this() = this(0)
  def ClassB(value: Int): ClassB = new ClassB(value)

  // All constructor proxies of ClassC are created.
  class ClassC(val value: Int)
  object ClassC {
    def fromString(s: String): ClassC = new ClassC(s.toInt)
  }

  // Any constructor proxy of ClassD is not created
  // since ClassD has a companion object, and it already defines a member named `apply`.
  class ClassD(val value: Int):
    def this() = this(0)
  object ClassD {
    def apply(value: Int): ClassD = new ClassD(value)
  }

}

/** [[https://dotty.epfl.ch/docs/reference/other-new-features/creator-applications.html Universal Apply Methods]]
  */
final class UniversalApplyMethodsSpec extends BaseSpec {
  import UniversalApplyMethodsSpec._

  "ClassA" in {

    assert(ClassA().value == 0)
    assert(ClassA(1).value == 1)

    // Constructor proxies cannot be used as values.
    assert(List(0, 1, 2).map(ClassA.apply(_)).map(_.value) == List(0, 1, 2))
    assert(List(0, 1, 2).map(ClassA(_)).map(_.value) == List(0, 1, 2))
    assertDoesNotCompile(
      """
        |assert(List(0,1,2).map(ClassA).map(_.value) == List(0, 1, 2))
        |""".stripMargin,
    )

  }

  "ClassB" in {

    assert(ClassB(1).value == 1)
    assertDoesNotCompile(
      """
        |assert(ClassB().value == 0)
        |""".stripMargin,
    )

    assert(List(0, 1, 2).map(ClassB.apply(_)).map(_.value) == List(0, 1, 2))
    assert(List(0, 1, 2).map(ClassB(_)).map(_.value) == List(0, 1, 2))
    // `ClassB` can be used as values since `ClassB` is a function.
    assert(List(0, 1, 2).map(ClassB).map(_.value) == List(0, 1, 2))

  }

  "ClassC" in {

    assert(ClassC(2).value == 2)
    assert(ClassC.fromString("3").value == 3)

  }

  "ClassD" in {

    assert(ClassD(1).value == 1)
    assertDoesNotCompile(
      """
        |assert(ClassD().value == 0)
        |""".stripMargin,
    )

    assert(List(0, 1, 2).map(ClassD.apply(_)).map(_.value) == List(0, 1, 2))
    assert(List(0, 1, 2).map(ClassD(_)).map(_.value) == List(0, 1, 2))
    // `ClassD` cannot be used as values since `ClassD` is a companion object name.
    assertDoesNotCompile(
      """
        |assert(List(0, 1, 2).map(ClassD).map(_.value) == List(0, 1, 2))
        |""".stripMargin,
    )

  }

}
