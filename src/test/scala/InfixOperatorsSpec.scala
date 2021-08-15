import InfixOperatorsSpec.Logarithm
import testing.BaseSpec

import scala.annotation.targetName

object InfixOperatorsSpec {

  opaque type Logarithm = Double
  object Logarithm:
    def apply(x: Double): Logarithm = math.log(x)
  extension (x: Logarithm)
    def toDouble: Double = math.exp(x)
    @targetName("mult")
    def *(y: Logarithm): Logarithm = x + y
    // Add `infix` modifier if we want to use it as an infix operator
    def plus(y: Logarithm): Logarithm = Logarithm(math.exp(x) + math.exp(y))
    def +(y: Logarithm): Logarithm = plus(y)

}

/** [[https://dotty.epfl.ch/docs/reference/changed-features/operators.html Rules for Operators]] */
final class InfixOperatorsSpec extends BaseSpec {
  import InfixOperatorsSpec.*

  "infix operators" in {

    val x = Logarithm(2)
    val y = Logarithm(3)

    val z1 = x * y
    val z2 = x.*(y)
    assert(z1 == Logarithm(6))
    assert(z2 == Logarithm(6))

    val w1 = x.plus(y)
    val w2 = x + y
    assert(w1 == Logarithm(5))
    assert(w2 == Logarithm(5))

  }

  "multi-line expression" in {

    val HelloWorld = "hello"
      ++ " world"
      ++ "!"
    assert(HelloWorld == "hello world!")

  }

}
