import testing.BaseSpec

/** [[https://docs.scala-lang.org/scala3/reference/changed-features/match-syntax.html Match Expressions]]
  */
final class MatchExpressionsSpec extends BaseSpec {

  "match can be chained" in {

    val xs = Seq(1, 2, 3)
    val v = xs match {
      case Nil => "empty"
      case _   => "nonempty"
    } match {
      case "empty" => 0
      case _       => 1
    }
    assert(v == 1)

  }

  "match can fllow a period" in {

    val xs = Seq(1, 2, 3)
    val s =
      if xs.match
          case Nil => false
          case _   => true
      then "nonempty"
      else "empty"

    assert(s == "nonempty")

  }

}
