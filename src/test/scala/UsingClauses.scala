import scala.util.NotGiven

object UsingClauses {

  //
  // Context Parameters
  //
  def max[T](values: T*)(using ordering: Ordering[T]): T =
    values.iterator.reduce(ordering.max)

  //
  // Anonymous Context Parameters
  //
  def maximum[T](values: T*)(using Ordering[T]): T =
    max(values*)

  //
  // Class Context Parameters
  //
  class GivenIntBox(using val givenInt: Int):
    def value: Int = givenInt
  class ExplicitGevenIntBox(using givenInt: Int):
    def value: Int = givenInt * 2
    given Int = givenInt * 2
  class NoExportGivenIntBox(using givenInt: Int):
    def value: Int = givenInt * 3

  //
  // Inferring Complex Arguments
  //
  def reverse[T](using ordering: Ordering[T]): Ordering[T] =
    Ordering.fromLessThan(ordering.gt)
  def min[T](value: T*)(using Ordering[T]): T =
    maximum(value*)(using reverse)

}

/** [[https://dotty.epfl.ch/docs/reference/contextual/using-clauses.html Using Clauses]] */
final class UsingClauses extends BaseSpec {
  import UsingClauses.*

  "Context Parameters" in {

    val reverseOrdering =
      Ordering.fromLessThan[Int](_ > _)

    assert(max(1, 2, 3) == 3)
    assert(max(1, 2, 3)(using reverseOrdering) == 1)

  }

  "Anonymous Context Parameters" in {

    val reverseOrdering: Ordering[Int] =
      Ordering.fromLessThan(_ > _)

    assert(maximum(1, 2, 3) == 3)
    assert(maximum(1, 2, 3)(using reverseOrdering) == 1)

  }

  "Class Context Parameters" in {

    // GivenIntBox
    {
      val b1 = GivenIntBox(using 1)
      assert(b1.value == 1)

      import b1.given
      assert(summon[Int] == 1)
    }

    // ExplicitGevenIntBox
    {
      val b2 = ExplicitGevenIntBox(using 2)
      assert(b2.value == 4)

      import b2.given
      assert(summon[Int] == 4)
    }

    // NoExportGivenIntBox
    {
      val b3 = NoExportGivenIntBox(using 3)
      assert(b3.value == 9)

      import b3.given
      given [NotGiven[Int]]: Int = 0
      assert(summon[Int] == 0)
    }

  }

  "Inferring Complex Arguments" in {

    given ordering: Ordering[Int] =
      Ordering.fromLessThan(_ < _)

    assert(min(1, 2, 3) == 1)
    assert(min(1, 2, 3)(using ordering) == 1)
    assert(min(1, 2, 3)(using reverse(using reverse)) == 1)

  }

}
