object OpenClassesSpec {
  import OpenClassesSpecHelper.*

  object UsingOpenClass:
    class SuffixFormatter[T](suffix: String) extends Formatter[T]:
      override def format(x: T): String = super.format(x) + suffix

  object UsingNonOpenClass:
    import scala.language.adhocExtensions
    class FragileSuffixFormatter[T](suffix: String) extends NonOpenFormatter[T]:
      override def format(x: T): String = super.format(x) + suffix

}

/** [[https://dotty.epfl.ch/docs/reference/other-new-features/open-classes.html Open Classes]] */
final class OpenClassesSpec extends BaseSpec {

  "SuffixFormatter" in {

    import OpenClassesSpec.UsingOpenClass.*
    val f = SuffixFormatter[String]("!")
    assert(f.format("abc") == "abc!")

  }

  "FragileSuffixFormatter" in {

    import OpenClassesSpec.UsingNonOpenClass.*
    val f = FragileSuffixFormatter[String]("?")
    assert(f.format("abc") == "abc?")

  }

}
