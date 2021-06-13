object ExtensionMethodsSpec {

  //
  // Basic Extension Methods
  //
  final case class Circle(x: Double, y: Double, radius: Double)
  extension (c: Circle)
    def circumference: Double =
      math.Pi * 2 * c.radius
  extension (c: Circle)
    def area: Double =
      math.Pi * c.radius * c.radius

  //
  // Operators
  //
  extension (x: String)
    def |+|(y: String): String =
      x + y

  extension (b: Int)
    infix def pow(e: Int): Int = {
      require(e >= 0)
      if e == 0 then 1
      else b * (b pow (e - 1))
    }

  //
  // Generic Extensions
  //
  extension [T](xs: List[T])
    def secondOption: Option[T] =
      xs.tail.headOption
  extension [T](xs: List[T])
    def penultimateOption: Option[T] =
      xs.init.lastOption

  extension [T: Numeric](x: T)
    def sum(xs: T*): T = {
      val num = summon[Numeric[T]]
      xs.foldLeft(x)(num.plus)
    }

  extension [T](x: List[T])
    def sumBy[U: Numeric](f: T => U): U = {
      x.map(f).sum
    }

  //
  // Collective Extensions
  //
  extension [T](xs: List[T])(using Ordering[T])
    def minimumOption: Option[T] =
      xs.minOption
    def minimum: T =
      minimumOption.getOrElse { throw new NoSuchElementException() }

}

/** [[https://dotty.epfl.ch/docs/reference/contextual/extension-methods.html Extension Methods]] */
final class ExtensionMethodsSpec extends BaseSpec {
  import ExtensionMethodsSpec._

  "Extension Methods" in {

    val c = Circle(0, 0, 3)
    assert(c.circumference == 6 * math.Pi)
    assert(c.area == 9 * math.Pi)

  }

  "Extension Operators" in {

    assert(("abc" |+| "def") == "abcdef")

    assert((2 pow 10) == 1024)

  }

  "Generic Extensions" in {

    assert(List(1, 2, 3, 4).secondOption == Option(2))
    assert(List(1, 2, 3, 4).penultimateOption == Option(3))

    assert(10.sum(1, 2, 3, 4, 5) == 25)
    assert(List("abc", "hello").sumBy(_.length) == 8)

  }

}
