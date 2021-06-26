object ByNameContextParametersSpec {

  trait Show[A]:
    def show(value: A): String

  extension [A](value: A) def show(using s: Show[A]): String = s.show(value)

  object Show:
    def apply[A](using show: Show[A]): Show[A] = show
    def instance[A](f: A => String): Show[A] = new Show[A] {
      override def show(value: A): String = f(value)
    }
    given Show[Int] = instance(_.toString)
    given Show[String] = instance(identity)
    given Show[Boolean] = instance(_.toString)

  sealed trait HList
  case object HNil extends HList
  case class HCons[T, H <: HList](head: T, tail: H) extends HList

  object HList:
    given Show[HNil.type] with
      override def show(value: HNil.type): String = ""
    given [T, H <: HList](using headShow: Show[T], tailShow: => Show[H]): Show[HCons[T, H]] with
      override def show(value: HCons[T, H]): String =
        value.tail match
          case HNil => headShow.show(value.head)
          case _ =>
            headShow.show(value.head) + "," + tailShow.show(value.tail)

}

/**   - [[https://dotty.epfl.ch/docs/reference/contextual/by-name-context-parameters.html By-Name Context Parameters]]
  *   - [[https://docs.scala-lang.org/sips/byname-implicits.html#motivating-examples SIP-NN - BYNAME IMPLICIT ARGUMENTS]]
  */
final class ByNameContextParametersSpec extends BaseSpec {
  import ByNameContextParametersSpec._

  "show(HNil)" in {
    import HList._

    assert(HNil.show == "")

  }

  "abc" in {
    import HList._

    val xs = HCons(1, HCons(true, HCons("a", HNil)))
    assert(xs.show == "1,true,a")

  }

}
