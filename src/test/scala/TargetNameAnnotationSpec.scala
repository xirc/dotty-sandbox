object TargetNameAnnotationSpec {
  import scala.annotation.targetName

  @targetName("f_string")
  def f(x: => String): Int = x.length
  @targetName("f_int")
  def f(x: => Int): Int = x

  class A:
    def f(): Int = 1
  class B extends A:
    // does not allow
    // @targetName("g")
    override def f(): Int = 2

}

/** [[https://dotty.epfl.ch/docs/reference/other-new-features/targetName.html @targetName]] */
final class TargetNameAnnotationSpec extends BaseSpec {
  import TargetNameAnnotationSpec.*

  "with target name" in {

    assert(f(1) == 1)
    assert(f("abc") == 3)

  }

  "override with targetName" in {

    assert(A().f() == 1)
    assert(B().f() == 2)

  }

}
