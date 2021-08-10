import java.util.concurrent.atomic.AtomicInteger

object InlineSpec {

  //
  // Recursive Inline Methods
  //
  inline def power(b: Long, e: Int): Long = {
    if e == 0 then 1
    else if e == 1 then b
    else {
      val x = power(b, e / 2)
      x * x * power(b, e % 2)
    }
  }

  //
  // by-value, by-name, and inline parameters
  //
  inline def magnitude(a: Int, b: => Int, inline c: Int): Int = {
    a * a + b * b + c * c
  }

  //
  // Transparent Inline Methods
  //
  object TransparentInlineMethod {

    transparent inline def intValue(b: Boolean): Option[Int] =
      if b then Some(1) else None

    transparent inline def zero: Int = 0

    transparent inline def one: Int = zero + 1

  }

  //
  // Inline Conditionals
  //
  object InlineConditional {

    inline def div(inline a: Int, b: Int): Int =
      inline if b == 0 then 0 else a / b

  }

  //
  // Inline Matches
  //
  object InlineMatches {

    sealed trait Nat

    case object Zero extends Nat

    case class Succ[N <: Nat](n: N) extends Nat

    transparent inline def toInt(n: Nat): Int =
      inline n match
        case Zero    => 0
        case Succ(m) => toInt(m) + 1

  }

}

/** [[https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html Inline]] */
final class InlineSpec extends BaseSpec {

  import InlineSpec.*

  "Recursive Inline Methods" in {

    assert(power(2, 10) == 1024)

  }

  "By-Value, By-Name, and Inline parameters" in {

    val a = new AtomicInteger(1)
    val b = new AtomicInteger(2)
    val c = new AtomicInteger(3)

    val z = magnitude(a.getAndIncrement(), b.getAndIncrement(), c.getAndIncrement())
    assert(z == 1 * 1 + 2 * 3 + 3 * 4)

  }

  "Transparent Inline Methods" in {

    import TransparentInlineMethod.*

    val some: Some[Int] = intValue(true)
    val none: None.type = intValue(false)

    assert(some == Some(1))
    assert(none == None)

    val z0: 0 = zero
    val z1: 1 = one
    assert(z0 == 0)
    assert(z1 == 1)

  }

  "Inline Conditionals" in {

    import InlineConditional.*

    val a = new AtomicInteger(10)
    val n5 = div(a.getAndIncrement(), 2)
    assert(n5 == 5)
    assert(a.get() == 11)

    val b = new AtomicInteger(10)
    val n0 = div(b.getAndIncrement(), 0)
    assert(n0 == 0)
    assert(b.get() == 10)

    assertDoesNotCompile("""
        |div(a.get(), b.get())
        |""".stripMargin)

  }

  "Inline Matches" in {
    import InlineMatches.*

    val z2: 2 = toInt(Succ(Succ(Zero)))
    assert(z2 == 2)

  }

}
