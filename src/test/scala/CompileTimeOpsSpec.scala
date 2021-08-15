import testing.BaseSpec

object CompileTimeOpsSpec {

  import scala.compiletime.{constValue, constValueOpt, erasedValue}
  import scala.compiletime.ops.int.S

  transparent inline def toIntC[N <: Int]: Int =
    inline constValue[N] match
      case 0       => 0
      case _: S[m] => 1 + toIntC[m]

  transparent inline def toIntOpt[N <: Int]: Option[Int] =
    inline constValueOpt[N] match
      case None       => None
      case _: Some[m] => Some(toIntC[m])

  object DefaultValueUsingTypeClass {

    trait DefaultValueContext[T]:
      transparent inline def defaultValue: T

    object DefaultValueContext:
      given DefaultValueContext[Int] with
        override transparent inline def defaultValue: Int = 0

      given DefaultValueContext[Boolean] with
        override transparent inline def defaultValue: Boolean = false

      given DefaultValueContext[String] with
        override transparent inline def defaultValue: String = ""

      given DefaultValueContext[Double] with
        override transparent inline def defaultValue: Double = 0.0

    transparent inline def defaultValue[T](using context: DefaultValueContext[T]): T =
      context.defaultValue

  }

  object DefaultValueUsingErasedValue {

    // Type Class implementation seems better than this
    transparent inline def defaultValue[T <: Matchable] =
      inline erasedValue[T] match {
        case _: Int     => 0
        case _: Boolean => false
        case _: String  => ""
        case _: Double  => 0.0
      }

  }

  object Peano {

    sealed trait Nat extends Matchable

    case object Zero extends Nat

    case class Succ[N <: Nat](predecessor: N) extends Nat

    // erasedValue seems better with ADTs
    transparent inline def toIntT[N <: Nat]: Int =
      inline erasedValue[N] match
        case _: Zero.type => 0
        case _: Succ[m]   => 1 + toIntT[m]

  }

  inline def failOnCompileTime(inline message: String) = {
    import scala.compiletime.error
    error(message)
  }

  //
  // Suummoning Implicits Selectively
  //
  object SummoningImplicitsSelectively {

    import scala.compiletime.{summonFrom, summonInline}

    trait DefaultValueContext[T]:
      transparent inline def defaultValue: T

    transparent inline def defaultValue[T](using context: DefaultValueContext[T]): T =
      context.defaultValue

    transparent inline def defaultValueOption[T]: Option[T] = summonFrom {
      case context: DefaultValueContext[T] => Some(context.defaultValue)
      case _                               => None
    }

    // Could we make this `transparent`?
    inline def defaultOf[T]: T =
      summonInline[DefaultValueContext[T]].defaultValue

  }

}

/** [[https://docs.scala-lang.org/scala3/reference/metaprogramming/compiletime-ops.html COMPILE-TIME OPERATIONS]]
  */
final class CompileTimeOpsSpec extends BaseSpec {

  import CompileTimeOpsSpec.*

  "constValue" in {

    assert(toIntC[10] == 10)

  }

  "constValueOpt" in {

    assert(toIntOpt[8] == Some(8))

    val x: Int = 5
    assert(toIntOpt[x.type] == None)

    val y: 10 = 10
    assert(toIntOpt[y.type] == Some(10))

  }

  "defaultValue using Type Class" in {

    import DefaultValueUsingTypeClass.*

    val _zero: 0 = defaultValue[Int]
    assert(_zero == 0)

    val _false: false = defaultValue[Boolean]
    assert(_false == false)

    val _emptyString: "" = defaultValue[String]
    assert(_emptyString == "")

    val _zz: 0.0 = defaultValue[Double]
    assert(_zz == 0.0)

  }

  "defaultValue using erasedValue" in {

    import DefaultValueUsingErasedValue.*

    val _zero: 0 = defaultValue[Int]
    assert(_zero == 0)

    val _false: false = defaultValue[Boolean]
    assert(_false == false)

    val _emptyString: "" = defaultValue[String]
    assert(_emptyString == "")

    val _zz: 0.0 = defaultValue[Double]
    assert(_zz == 0.0)

  }

  "erasedValue" in {

    import Peano.*

    val _2: Int = toIntT[Succ[Succ[Zero.type]]]
    assert(_2 == 2)

  }

  "error" ignore {

    // Not working at this point
    /** assertDoesNotCompile( """
      * |failOnCompileTime("should be failure")
      * |""".stripMargin, )
      */

  }

  "ops.int" in {

    import scala.compiletime.ops.int.*

    val succ0: S[0] = 1
    val succ1: S[1] = 2

    val plus: 1 + 2 = 3
    val minus: 1 - 2 = -1
    val mult: 2 * 3 = 6
    val div: 6 / 3 = 2
    val mod: 6 % 5 = 1
    val expr: 1 + 2 * 3 - 6 / 2 + 9 % 7 = 1 + 6 - 3 + 2

    val bitshiftL: 1 << 10 = 1024
    val bitShiftR: 1024 >>> 8 = 4
    val bitShiftAR: -8 >> 1 = -4

    val bitwiseXOR: 10 ^ 9 = 3
    val bitwiseAND: BitwiseAnd[10, 9] = 8
    val bitwiseOR: BitwiseOr[10, 9] = 11

    val abs: Abs[-1] = 1
    val negate1: Negate[-1] = 1
    val negate2: Negate[1] = -1

    val min: Min[2, 3] = 2
    val max: Max[2, 3] = 3

    val tostring: ToString[123] = "123"

  }

  "ops.boolean" in {

    import scala.compiletime.ops.boolean.*

    val negateFalse: ![false] = true
    val negateTrue: ![true] = false

    val andFF: false && false = false
    val andFT: false && true = false
    val andTT: true && true = true

    val orFF: false || false = false
    val orFT: false || true = true
    val orTT: true || true = true

    val xorFF: false ^ false = false
    val xorFT: false ^ true = true
    val xorTT: true ^ true = false

  }

  "ops.string" in {

    import scala.compiletime.ops.string.*

    val plus: "a" + "bc" = "abc"

  }

  "any" in {

    import scala.compiletime.ops.any.*

    val eq: 1 == 1 = true
    val neq1: 1 == 2 = false
    val neq2: 1 == "a" = false

  }

  "summonFrom" in {

    import SummoningImplicitsSelectively.*

    given DefaultValueContext[Int] with
      override transparent inline def defaultValue: Int = 0

    assert(defaultValueOption[Int] == Option(0))
    assert(defaultValueOption[Boolean] == None)

    assert(defaultValue[Int] == 0)
    assertDoesNotCompile("""
        |defaultValue[Boolean]
        |""".stripMargin)

  }

  "summonInline" in {

    import SummoningImplicitsSelectively.*

    given DefaultValueContext[Int] with
      override transparent inline def defaultValue: Int = 0

    assert(defaultOf[Int] == 0)

  }

}
