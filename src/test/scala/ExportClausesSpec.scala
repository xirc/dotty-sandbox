object ExportClausesSpec {

  trait Parser[+A]:
    def parse(value: String): A
    def apply(value: String): A = parse(value)

  object Parser:
    def instance[A](f: String => A): Parser[A] =
      new Parser[A] {
        override def parse(value: String): A = f(value)
      }

  trait Formatter[-A]:
    def format(value: A): String
    def apply(value: A): String = format(value)

  object Formatter:
    def instance[A](f: A => String): Formatter[A] =
      new Formatter[A] {
        override def format(value: A): String = f(value)
      }

  class Converter[A](parser: Parser[A], formatter: Formatter[A]):
    export formatter.{apply as _, *}
    export parser.{apply as _, *}
    def convert(value: String, f: A => A): String = {
      val from = parse(value)
      val to = f(from)
      format(to)
    }
    def apply(value: String, f: A => A): String = convert(value, f)

}

/** [[https://dotty.epfl.ch/docs/reference/other-new-features/export.html Export Clauses]] */
final class ExportClausesSpec extends BaseSpec {
  import ExportClausesSpec.*

  "Parser" should {

    val parser = Parser.instance(_.toInt)

    "parse" in {
      assert(parser.parse("1") == 1)
    }

    "apply" in {
      assert(parser("1") == 1)
    }

  }

  "Formatter" should {

    val formatter = Formatter.instance[Int](_.toString)

    "format" in {
      assert(formatter.format(1) == "1")
    }

    "apply" in {
      assert(formatter(1) == "1")
    }

  }

  "Converter" should {

    val intConverter = Converter[Int](Parser.instance(_.toInt), Formatter.instance(_.toString))
    val stringConverter = Converter[String](Parser.instance(identity), Formatter.instance(identity))

    "parse" in {
      assert(intConverter.parse("1") == 1)
      assert(stringConverter.parse("1") == "1")
    }

    "format" in {
      assert(intConverter.format(1) == "1")
      assert(stringConverter.format("1") == "1")
    }

    "convert" in {
      assert(intConverter.convert("1", _ * 2) == "2")
      assert(stringConverter.convert("1", _ * 2) == "11")
    }

  }

}
