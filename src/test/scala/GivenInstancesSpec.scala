import scala.util.NotGiven

object GivenInstancesSpec {

  extension [T](value: T) def show()(using s: Show[T]): String = s.show(value)

  //
  // Type Classe & Instance
  //

  trait Show[T]:
    def show(value: T): String

  object Show:
    def instance[T](f: T => String): Show[T] = {
      new Show[T]:
        def show(value: T): String = f(value)
    }

  given intShow: Show[Int] with
    def show(value: Int): String =
      value.toString

  given listShow[T](using s: Show[T]): Show[List[T]] with
    def show(value: List[T]): String =
      value.map(s.show).mkString("[", ",", "]")

  //
  // Anonymous Givens
  //

  given Show[Double] with
    def show(value: Double): String =
      value.toString

  //
  // Alias Givens
  //
  given Show[Float] = Show.instance(_.toString)

  //
  // Negated Givens
  //
  trait Tag[A]
  case class Container[A](value: Boolean)
  given withTag[A](using Tag[A]): Container[A] = Container(true)
  given withoutTag[A](using NotGiven[Tag[A]]): Container[A] = Container(false)

}

/** [[https://dotty.epfl.ch/docs/reference/contextual/givens.html Given Instances]] */
final class GivenInstancesSpec extends BaseSpec {
  import GivenInstancesSpec.*

  "IntShow" in {

    assert(1.show() == "1")

  }

  "ListShow" in {

    assert(List(1, 2, 3).show() == "[1,2,3]")

  }

  "DoubleShow" in {

    assert(1.2.show() == "1.2")

  }

  "FloatShow" in {

    val value = 1.2f
    assert(value.show() == "1.2")

  }

  "Pattern-Bound Given Instances" in {

    val shows: Seq[Show[Int]] = Seq(
      Show.instance[Int](i => s"${i.toString}!"),
      Show.instance[Int](i => s"${i.toString}?"),
    )
    val texts =
      for given Show[Int] <- shows yield 1.show()
    assert(texts == Seq("1!", "1?"))

  }

  "Negated Given" in {

    given Tag[Int] = new Tag[Int] {}
    assert(summon[Container[Int]].value)
    assert(!summon[Container[Double]].value)

  }

}
