import scala.language.implicitConversions

object UnionTypeSpec {

  type Option[T] = Some[T] | None.type
  final case class Some[T](value: T)
  case object None
  def Option[T](value: T): Option[T] = {
    if (value == null) None
    else Some(value)
  }

}

/** See
  *  - https://dotty.epfl.ch/docs/reference/new-types/union-types.html
  */
final class UnionTypeSpec extends BaseSpec {

  "UnionType can represent Option[T]" in {

    Option("J") shouldBe Some("J")
    Option(null) shouldBe None

  }

}
