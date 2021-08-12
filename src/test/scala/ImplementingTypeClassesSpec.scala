import testing.BaseSpec

object ImplementingTypeClassesSpec {

  //
  // SemiGroups & Monoids
  //
  trait SemiGroup[T]:
    extension (x: T) def combine(y: T): T

  trait Monoid[T] extends SemiGroup[T]:
    def unit: T
  object Monoid:
    def apply[T](using m: Monoid[T]): Monoid[T] = m

  given Monoid[String] with
    extension (x: String) def combine(y: String): String = x ++ y
    def unit: String = ""

  given Monoid[Int] with
    extension (x: Int) def combine(y: Int): Int = x + y
    def unit: Int = 0

  def combineAll[T: Monoid](xs: T*): T =
    xs.foldLeft(Monoid[T].unit)(_.combine(_))

  //
  // Functors
  //
  trait Functor[F[_]]:
    extension [A](x: F[A]) def map[B](f: A => B): F[B]

  given Functor[List] with
    extension [A](xs: List[A])
      def map[B](f: A => B): List[B] =
        xs.map(f)

  def assertTransformation[F[_]: Functor, A, B](
      expected: F[B],
      original: F[A],
      mapping: A => B,
  ): Unit =
    assert(expected == original.map(mapping))

  //
  // Monads
  //
  trait Monad[F[_]] extends Functor[F]:
    def pure[A](x: A): F[A]
    extension [A](x: F[A])
      def flatMap[B](f: A => F[B]): F[B]
      def map[B](f: A => B): F[B] =
        x.flatMap(f.andThen(pure))

  given Monad[List] with
    def pure[A](x: A): List[A] =
      List(x)
    extension [A](xs: List[A])
      def flatMap[B](f: A => List[B]): List[B] =
        xs.flatMap(f)

  given Monad[Option] with
    def pure[A](x: A): Option[A] =
      Option(x)
    extension [A](x: Option[A])
      def flatMap[B](f: A => Option[B]): Option[B] =
        x.flatMap(f)

  case class Config(value: Int)
  def compute(i: Int)(config: Config): Int = config.value * i
  def show(i: Int)(config: Config): String = i.toString

  given readerMonad[Context]: Monad[[Result] =>> Context => Result] with
    def pure[A](x: A): Context => A =
      context => x
    extension [A](x: Context => A)
      def flatMap[B](f: A => Context => B): Context => B =
        context => f(x(context))(context)

}

/** [[https://dotty.epfl.ch/docs/reference/contextual/type-classes.html Implimenting Type classes]]
  */
final class ImplementingTypeClassesSpec extends BaseSpec {
  import ImplementingTypeClassesSpec.*

  "semigroups & monoids" in {

    assert(combineAll("a", "b", "c") == "abc")
    assert(combineAll(1, 2, 3) == 6)

  }

  "functors" in {

    assertTransformation(List("a1", "b1"), List("a", "b"), e => s"${e}1")

  }

  "monads" in {

    def computeAndShow(i: Int): Config => String =
      compute(i).flatMap(show)
    assert(computeAndShow(2)(Config(3)) == "6")

  }

}
