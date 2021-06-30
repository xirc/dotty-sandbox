object ImplicitConversionsSpec {

  final case class Box[T](value: T):
    def show: String = s"Box($value)"

  given Conversion[Int, Box[Int]] with
    def apply(value: Int): Box[Int] = Box(value)

  given Conversion[String, Box[String]] = Box(_)

  object MagnetPattern:
    enum ShowArg:
      case IntValue(value: Int)
      case StringValue(value: String)
      case ListIntValue[T](value: List[Int])
      case ListStringValue[T](value: List[String])

    object ShowArg:
      given fromInt: Conversion[Int, IntValue] = IntValue(_)
      given fromString: Conversion[String, StringValue] = StringValue(_)
      given fromListInt: Conversion[List[Int], ListIntValue[Int]] = ListIntValue(_)
      given fromListString: Conversion[List[String], ListStringValue[String]] = ListStringValue(_)

    def showAsInt(arg: ShowArg): Int = arg match
      case ShowArg.IntValue(i)         => i
      case ShowArg.StringValue(s)      => s.length
      case ShowArg.ListIntValue(xs)    => xs.sum
      case ShowArg.ListStringValue(xs) => xs.map(_.length).sum

}

/** [[https://dotty.epfl.ch/docs/reference/contextual/conversions.html Implicit Conversions]] */
final class ImplicitConversionsSpec extends BaseSpec {
  import ImplicitConversionsSpec.*
  import scala.language.implicitConversions

  "Box[Int]" in {

    val box: Box[Int] = 1
    assert(box.show == "Box(1)")

  }

  "Box[String]" in {

    val box: Box[String] = "abc"
    assert(box.show == "Box(abc)")

  }

  "Magnet Pattern" in {
    import MagnetPattern.*

    assert(showAsInt(1) == 1)
    assert(showAsInt("abc") == 3)
    assert(showAsInt(List(1, 2, 3, 4, 5)) == 15)
    assert(showAsInt(List("ab", "cd", "ef")) == 6)

  }

}
