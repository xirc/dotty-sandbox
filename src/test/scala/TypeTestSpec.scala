import testing.BaseSpec

import scala.reflect.{TypeTest, Typeable}

object TypeTestSpec {

  def f[X <: Matchable, Y](x: X)(using TypeTest[X, Y]): Option[Y] = x match
    case x: Y => Option(x)
    case _    => None

  def g[T: Typeable](instance: Matchable): Boolean =
    instance match
      case x: T => true
      case _    => false

  trait Peano:
    type Nat <: Matchable
    type Zero <: Nat
    type Succ <: Nat

    def safeDivmod(m: Nat, n: Succ): (Nat, Nat)
    def divmodOption(m: Nat, n: Nat): Option[(Nat, Nat)] = n match
      case Zero        => None
      case s @ Succ(_) => Some(safeDivmod(m, s))

    val Zero: Zero

    val Succ: SuccExtractor
    trait SuccExtractor:
      def apply(nat: Nat): Succ
      def unapply(succ: Succ): Option[Nat]

    given typeTestOfZero: TypeTest[Nat, Zero]
    given typeTestOfSucc: TypeTest[Nat, Succ]

  object PeanoInt extends Peano:
    override type Nat = Int
    override type Zero = Int
    override type Succ = Int

    override def safeDivmod(m: Nat, n: Succ): (Nat, Nat) = (m / n, m % n)
    override val Zero: Zero = 0

    val Succ: SuccExtractor = new:
      def apply(nat: Nat): Succ = nat + 1
      def unapply(succ: Succ): Option[Nat] = Option(succ - 1)

    override def typeTestOfZero: TypeTest[Nat, Zero] = new:
      def unapply(x: Nat): Option[x.type & Zero] =
        if x == 0 then Option(x) else None

    override def typeTestOfSucc: TypeTest[Nat, Succ] = new:
      def unapply(x: Nat): Option[x.type & Succ] =
        if x > 0 then Option(x) else None

}

/** [[https://dotty.epfl.ch/docs/reference/other-new-features/type-test.html TypeTest]] */
final class TypeTestSpec extends BaseSpec {
  import TypeTestSpec.*

  "using TypeTest[X,Y]" in {

    val x: AnyVal = 1
    assert(f[AnyVal, Int](x) == Some(1))

  }

  "using Typeable[T]" in {

    val x: AnyRef = "123"
    assert(g[String](x) == true)
    assert(g[Int](x) == false)

  }

  "Example: Peano Number using Reflection" in {
    import PeanoInt.*

    val two: Nat = Succ(Succ(Zero))
    val five: Nat = Succ(Succ(Succ(two)))

    assert(divmodOption(five, two) == Some((2, 1)))
    assert(divmodOption(two, five) == Some((0, 2)))
    assert(divmodOption(two, Zero) == None)

  }

}
