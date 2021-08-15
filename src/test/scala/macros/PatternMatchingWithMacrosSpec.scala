package macros

import testing.BaseSpec

final class PatternMatchingWithMacrosSpec extends BaseSpec {

  "sum" in {

    import PatternMatchingWithMacros.*

    assert(sum(1, 2, 3) == 6)

    val xs = Seq(1, 2, 3)
    assert(sum(xs*) == 6)

    val x = 1
    val y = 2
    assert(sum(x, y, 3) == 6)

  }

  "quoted patterns" in {

    import PatternMatchingWithMacros.QuotedPatterns.*

    assert(optimize {
      sum()
    } == 0)
    assert(optimize {
      sum(sum(1, 2, 3), sum(4, sum(5, 6)), 7, 8, 9, 10)
    } == 55)

  }

  "recovering precise types using patterns" in {

    import PatternMatchingWithMacros.RecoveringPreciseTypesUsingPatterns.*

    given Show[Boolean] with
      def show(b: Boolean) = s"boolean($b)!"

    assert(showMe"${true}" == "boolean(true)!")

  }

  "open code pattern" in {

    import PatternMatchingWithMacros.OpenCodePattern.*

    val y: 16 = eval {
      val x: Int = 4
      x * x
    }
    assert(y == 16)

  }

}
