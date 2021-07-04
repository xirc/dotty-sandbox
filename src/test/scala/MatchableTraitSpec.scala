import MatchableTraitSpec.Second

object MatchableTraitSpec {

  class Clazz(val value: Int):
    override def equals(obj: Any): Boolean = {
      obj.asInstanceOf[Matchable] match
        case that: Clazz => this.value == that.value
        case _           => false
    }
    override def hashCode(): Int = value.hashCode()

  opaque type Meter = Double
  def Meter(x: Double): Meter = x

  opaque type Second = Double
  def Second(x: Double): Second = x

}

/** [[https://dotty.epfl.ch/docs/reference/other-new-features/matchable.html The Matchable Trait]]
  */
final class MatchableTraitSpec extends BaseSpec {
  import MatchableTraitSpec.*

  "matchable" in {

    def f[T <: Matchable](value: T): Int =
      value match {
        case i: Int    => i
        case s: String => s.length
        case other     => other.hashCode()
      }

    assert(f(1) == 1)
    assert(f("abc") == 3)
    assert(f(Vector.empty) == Vector.empty.hashCode())

  }

  "with universal equality" in {

    val x = Clazz(1)
    val y = Clazz(2)
    val z = Clazz(1)
    assert(x != y)
    assert(x == z)

  }

  "with multiversal equaltiy" in {

    val meter10 = Meter(10)
    val second10 = Second(10)

    import scala.language.strictEquality
    assert(meter10.equals(second10))
    assertDoesNotCompile(
      """
        |assert(meter10 == second10)
        |""".stripMargin,
    )

  }

}
