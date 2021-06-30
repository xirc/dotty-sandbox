object TypeClassDerivationSpec {
  import scala.deriving.Mirror
  import scala.compiletime.{erasedValue, summonInline}

  trait Eq[T]:
    def eqv(x: T, y: T): Boolean

  object Eq:
    def apply[T](using e: Eq[T]): Eq[T] = e

    def instance[T](f: (T, T) => Boolean): Eq[T] =
      new Eq[T] {
        def eqv(x: T, y: T) = f(x, y)
      }

    inline given derived[T](using m: Mirror.Of[T]): Eq[T] =
      val elemInstances = summonAll[m.MirroredElemTypes]
      inline m match
        case s: Mirror.SumOf[T]     => eqSum(s, elemInstances)
        case p: Mirror.ProductOf[T] => eqProduct(p, elemInstances)

    inline def summonAll[T <: Tuple]: List[Eq[?]] =
      inline erasedValue[T] match
        case _: EmptyTuple => Nil
        case _: (t *: ts)  => summonInline[Eq[t]] :: summonAll[ts]

    private def eqSum[T](s: Mirror.SumOf[T], elems: List[Eq[?]]): Eq[T] =
      instance { (x, y) =>
        val ordx = s.ordinal(x)
        val ordy = s.ordinal(y)
        (ordx == ordy) && check(elems(ordx))(x, y)
      }

    private def eqProduct[T](p: Mirror.ProductOf[T], elems: List[Eq[?]]): Eq[T] =
      instance { (x, y) =>
        iterator(x).zip(iterator(y)).zip(elems.iterator).forall { case ((ex, ey), e) =>
          check(e)(ex, ey)
        }
      }

    private def check[T](elem: Eq[?])(x: T, y: T): Boolean =
      elem.asInstanceOf[Eq[T]].eqv(x, y)

    private def iterator[T](p: T): Iterator[Any] =
      p.asInstanceOf[Product].productIterator

  given Eq[Int] = Eq.instance { (x, y) => x == y }
  given Eq[Boolean] = Eq.instance { (x, y) => x == y }

  enum Nullable[+T] derives Eq:
    case Some(t: T)
    case None

  final case class MyProduct(int: Int, boolean: Boolean)

}

/** [[https://dotty.epfl.ch/docs/reference/contextual/derivation.html Type Class Derivation]] */
final class TypeClassDerivationSpec extends BaseSpec {
  import TypeClassDerivationSpec.{*, given}

  "int" in {

    assert(Eq[Int].eqv(1, 1))
    assert(!Eq[Int].eqv(1, 2))

  }

  "boolean" in {

    assert(Eq[Boolean].eqv(true, true))
    assert(Eq[Boolean].eqv(false, false))
    assert(!Eq[Boolean].eqv(true, false))

  }

  "sum" in {

    assert(Eq[Nullable[Int]].eqv(Nullable.None, Nullable.None))
    assert(Eq[Nullable[Int]].eqv(Nullable.Some(1), Nullable.Some(1)))
    assert(!Eq[Nullable[Int]].eqv(Nullable.Some(1), Nullable.Some(2)))
    assert(Eq[Nullable[Boolean]].eqv(Nullable.Some(false), Nullable.Some(false)))
    assert(!Eq[Nullable[Boolean]].eqv(Nullable.Some(true), Nullable.Some(false)))

  }

  "product" in {

    assert(Eq[MyProduct].eqv(MyProduct(1, true), MyProduct(1, true)))
    assert(!Eq[MyProduct].eqv(MyProduct(1, true), MyProduct(1, false)))

  }

}
