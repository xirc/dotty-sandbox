import scala.runtime.Nothing$

object OptionLessPatternMatchingSpec {

  //
  // Boolean Match
  //
  object IsZero {
    def unapply(value: Int): Boolean =
      value == 0
  }

  //
  // Product Match
  //
  object LeftRight {
    def unapply(v: Int): (Int, Int) =
      (v * 2 + 1, v * 2 + 2)
  }

  //
  // Single Match
  //
  class Nat(val x: Int) {
    def get: Int = x

    def isEmpty: Boolean = x < 0
  }

  object Nat {
    def unapply(x: Int): Nat = new Nat(x)
  }

  //
  // Name-based Match
  //
  class HeadTail private (value: String) {
    def _1: Char = value.head

    def _2: String = value.tail

    def isEmpty: Boolean = value.isEmpty

    def get = this
  }

  object HeadTail {
    def unapply(value: String): HeadTail = new HeadTail(value)
  }

  //
  // Scala2 compatible
  //
  object Size {
    def unapply(value: String): Option[Int] =
      if (value == null) None else Option(value.length)
  }

  //
  // Sequence Match
  //
  object Until {
    def unapplySeq(value: Int): Option[Seq[Int]] =
      Option(0.until(value).toSeq)
  }

  //
  // Product-Sequence Match
  //
  object SizeWithValue {
    def unapplySeq(value: String): Option[(Int, Seq[Char])] =
      Option((value.size, value))
  }

}

/** [[https://docs.scala-lang.org/scala3/reference/changed-features/pattern-matching.html OPTION-LESS PATTERN MATCHING]]
  */
final class OptionLessPatternMatchingSpec extends BaseSpec {

  import OptionLessPatternMatchingSpec.*

  "Boolean Match" in {

    assert(0 match {
      case IsZero() => true
      case _        => false
    })

    assert(1 match {
      case IsZero() => false
      case _        => true
    })

  }

  "Product Match" in {

    assert(0 match {
      case LeftRight(1, 2) => true
      case _               => false
    })

  }

  "Single Match" in {

    assert(0 match {
      case Nat(x) => true
      case _      => false
    })

    assert(-1 match {
      case Nat(x) => false
      case _      => true
    })

  }

  "Name-based Match" in {

    assert("abc" match {
      case HeadTail(head, tail) => head == 'a' && tail == "bc"
      case _                    => false
    })

    assert("" match {
      case HeadTail(head, tail) => false
      case _                    => true
    })

  }

  "Scala2 Compatible" in {

    assert(null match {
      case Size(size) => false
      case _          => true
    })

    assert("abc" match {
      case Size(size) => size == 3
      case _          => false
    })

  }

  "Sequence Match" in {

    assert(3 match {
      case Until(0, 1, 2) => true
      case _              => false
    })

    assert(0 match {
      case Until() => true
      case _       => false
    })

    val x = 1000 match {
      case Until(xs*) => xs.size
      case _          => 0
    }
    assert(x == 1000)

  }

  "Product-Sequence Match" in {

    val x = "abc" match {
      case SizeWithValue(3, xs*) => xs.length
      case _                     => 0
    }
    assert(x == 3)

  }

}
