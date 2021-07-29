object OverloadResolutionSpec {

  def f(x: Int)(y: String): String = y * x

  def f(x: Int)(y: Int): Int = x + y

  def g[T](xs: Iterable[T], z: T, f: (T, T) => T): T =
    xs.fold(z)(f)

}

/** [[https://docs.scala-lang.org/scala3/reference/changed-features/overload-resolution.html OVERLOAD RESOLUTION]]
  */
final class OverloadResolutionSpec extends BaseSpec {

  import OverloadResolutionSpec.*

  "overload resolution" in {

    assert(f(3)("a") == "aaa")
    assert(f(3)(2) == 5)

    assertDoesNotCompile(
      """
        |f(3)
        |""".stripMargin,
    )

    assertDoesNotCompile(
      """
        |val f3 = f(3)
        |""".stripMargin,
    )

    val f3s: String => String = f(3)
    assert(f3s("c") == "ccc")

    val f3i: Int => Int = f(3)
    assert(f3i(4) == 7)

    assert(g(Vector(1, 2, 3), 0, _ + _) == 6)

  }

}
