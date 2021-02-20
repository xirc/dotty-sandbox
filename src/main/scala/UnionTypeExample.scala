/** See
  *  - https://dotty.epfl.ch/docs/reference/new-types/union-types.html
  */
object UnionTypeExample extends App {
  final case class Some[T](value: T)
  final case object None

  type Option[T] = Some[T] | None.type

  def Option[T](value: T): Option[T] = {
    if (value == null) None
    else Some(value)
  }

  println(Option("J"))
  println(Option(null))
}
