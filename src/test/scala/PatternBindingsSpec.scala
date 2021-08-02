/** [[https://docs.scala-lang.org/scala3/reference/changed-features/pattern-bindings.html Pattern Bindings]]
  */
final class PatternBindingsSpec extends BaseSpec {

  "Binding in Pattern Definitions" in {

    val xs: List[Matchable] = List(1, 2, 3)
    assertThrows[MatchError] {
      // @unchecked is required. If we omit the annoation, we get a warning.
      val (x: String) :: _ = xs: @unchecked
    }

  }

  "Pattern Binding in for Expressions" in {

    val elems: List[Matchable] = List((1, 2), "hello", (3, 4))
    val zs = for case (x, y) <- elems yield (y, x)
    assert(zs == List((2, 1), (4, 3)))

  }

}
