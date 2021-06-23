object AutomaticEtaExpansionSpec {

  def plus(x: Int)(y: Int): Int = x + y

  def min(x: Int)(y: Int)(using ordering: Ordering[Int]) =
    Ordering[Int].min(x, y)

}

/**   - [[https://dotty.epfl.ch/docs/reference/changed-features/eta-expansion.html Automatic Eta Expansion]]
  *   - [[https://dotty.epfl.ch/docs/reference/changed-features/eta-expansion-spec.html Automatic Eta Expansion - More Details]]
  */
final class AutomaticEtaExpansionSpec extends BaseSpec {
  import AutomaticEtaExpansionSpec._

  "Automatic Eta Expansion" in {

    val f2 = plus
    assert(f2(1)(2) == 3)

    val f1 = plus(1)
    assert(f1(2) == 3)

  }

  "Automatic Eta Expansion and Implicit Parameters" in {

    object O:
      given Ordering[Int] = Ordering.Int.reverse
      val min0 = min(0)

    assert(O.min0(2) == 2)

    val min1 = min(1)
    assert(min1(2) == 1)

  }

  "Automatic Eta Expansion and Context Functions" in {

    val min01: Ordering[Int] ?=> Int = min(0)(1)
    def assertMin(expectedValue: Int)(using Ordering[Int]): Unit = {
      assert(min01 == expectedValue)
    }

    assertMin(0)
    assertMin(1)(using Ordering[Int].reverse)

  }

}
