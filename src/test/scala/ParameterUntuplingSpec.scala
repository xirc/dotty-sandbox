import testing.BaseSpec

/** [[https://dotty.epfl.ch/docs/reference/other-new-features/parameter-untupling.html Parameter Untupling]]
  */
final class ParameterUntuplingSpec extends BaseSpec {

  "untupling" in {

    val xs = Vector("a", "b", "c", "d").zipWithIndex

    val ys = xs.map { (x, y) => x * y }
    assert(ys == Vector("", "b", "cc", "ddd"))

    val zs = xs.map(_ * _)
    assert(zs == Vector("", "b", "cc", "ddd"))

  }

}
