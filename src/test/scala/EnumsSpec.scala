import testing.BaseSpec

object EnumsSpec {

  enum Size:
    case Small, Medium, Large

  enum Color(val rgba: Int):
    case Red extends Color(0xff0000ff)
    case Green extends Color(0x00ff00ff)
    case Blue extends Color(0x0000ffff)
    case Alpha extends Color(0x00000000)
    def r: Int = (rgba >> 24) & 0x000000ff
    def g: Int = (rgba >> 16) & 0x000000ff
    def b: Int = (rgba >> 8) & 0x000000ff
    def a: Int = rgba & 0x000000ff

  enum Option[+T]:
    case Some(x: T)
    case None

    def isEmpty: Boolean = this match {
      case None    => true
      case Some(_) => false
    }

  object Option:
    def apply[T](x: T): Option[T] =
      if x == null then None else Some(x)

}

/** [[https://dotty.epfl.ch/docs/reference/enums/enums.html Enumerations]]
  */
final class EnumsSpec extends BaseSpec {
  import EnumsSpec.*

  "basic" in {

    val size: Size = Size.Small
    assert(size == Size.Small)

    val sizeString: String = size match
      case Size.Small  => "small"
      case Size.Medium => "medium"
      case Size.Large  => "large"

    assert(sizeString == "small")

    assert(Size.Small.ordinal == 0)
    assert(Size.Medium.ordinal == 1)
    assert(Size.Large.ordinal == 2)

    assert(Size.valueOf("Small") == Size.Small)
    assert(Size.valueOf("Medium") == Size.Medium)
    assert(Size.valueOf("Large") == Size.Large)
    assertThrows[IllegalArgumentException] {
      Size.valueOf("Unknown")
    }

    assert(Size.values.toSeq == Seq(Size.Small, Size.Medium, Size.Large))

    assert(Size.fromOrdinal(0) == Size.Small)
    assert(Size.fromOrdinal(1) == Size.Medium)
    assert(Size.fromOrdinal(2) == Size.Large)
    assertThrows[NoSuchElementException] {
      Size.fromOrdinal(3)
    }

  }

  "paremeterized & members" in {

    val red = Color.Red
    assert(red.r == 255)
    assert(red.g == 0)
    assert(red.b == 0)
    assert(red.a == 255)

  }

  "Algebraic Data Types" in {

    val someOrNone = if true then Option.Some(1) else Option.None
    assert(someOrNone == Option(1))
    assert(Option.Some(1).isEmpty == false)
    assert(None.isEmpty)

  }

}
