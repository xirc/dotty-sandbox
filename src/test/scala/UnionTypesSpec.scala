object UnionTypesSpec {

  type Option[T] = Some[T] | None.type
  final case class Some[T](value: T)
  case object None
  def Option[T](value: T): Option[T] = {
    if (value == null) None
    else Some(value)
  }

  final case class Envelope(message: String)
  final case class Stamp(price: Int)

}

/** [[https://dotty.epfl.ch/docs/reference/new-types/union-types.html Union Types]]
  */
final class UnionTypesSpec extends BaseSpec {
  import UnionTypesSpec._

  "UnionType can represent Option[T]" in {

    assert(Option("J") == Some("J"))
    assert(Option(null) == None)

    val someOrNone: Option[Int] =
      if true then Some(1) else None
    assert(someOrNone == Some(1))

  }

  "abc" in {

    val envelope = Envelope("hello")
    val stamp = Stamp(50)
    val envelopeOrStamp: Envelope | Stamp =
      if true then envelope else stamp

    assert(envelopeOrStamp == envelope)

  }

}
