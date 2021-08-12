import testing.BaseSpec

/** [[https://docs.scala-lang.org/scala3/reference/changed-features/vararg-splices.html Vararg Splices]]
  */
final class VarArgsSplicesSpec extends BaseSpec {

  "vararg splices" in {

    val xs = Array(0, 1, 2, 3)
    val ys = List(xs*)
    val zs = ys match
      case List(0, 1, ws*) => ws
      case _               => ys

    assert(ys == List(0, 1, 2, 3))
    assert(zs == List(2, 3))

  }

}
