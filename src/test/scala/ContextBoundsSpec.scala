import testing.BaseSpec

object ContextBoundsSpec {

  def maximum[T: Ordering](xs: T*): T =
    xs.reduce(summon[Ordering[T]].max)

  def sum[T: Numeric](xs: T*): T =
    xs.reduce(summon[Numeric[T]].plus)

  trait Show[T]:
    def show(value: T): String

  def show[T: Show](value: T): String =
    summon[Show[T]].show(value)

  given Show[Int] with
    def show(value: Int): String = value.toString

  given productShow[T <: Product]: Show[T] with
    def show(value: T): String =
      s"""|
            |${value.productPrefix}
          |${value.productElementNames.mkString("(", ",", ")")}
          |=
          |${value.productIterator.mkString("(", ",", ")")}
          |""".stripMargin

  final case class KeyValue[T](key: String, value: T)

}

/** [[https://dotty.epfl.ch/docs/reference/contextual/context-bounds.html Context Bounds]] */
final class ContextBoundsSpec extends BaseSpec {
  import ContextBoundsSpec.*

  "Context Bounds" in {

    assert(maximum(1, 2, 3) == 3)
    assert(sum(1, 2, 3) == 6)

    assert(show(123) == "123")

    assert(
      show(KeyValue("hello", 123)) ===
        """|
           |KeyValue
           |(key,value)
           |=
           |(hello,123)
           |""".stripMargin,
    )

  }

}
