object TransparentTraitsSpec {

  object NonTransparent:
    trait ConcreteType
    sealed trait ShapeType
    object Circle extends ShapeType, ConcreteType
    object Square extends ShapeType, ConcreteType

  object Transparent:
    transparent trait ConcreteType
    sealed trait ShapeType
    object Circle extends ShapeType, ConcreteType
    object Square extends ShapeType, ConcreteType

}

/** [[https://dotty.epfl.ch/docs/reference/other-new-features/transparent-traits.html Transparent Traits]]
  */
final class TransparentTraitsSpec extends BaseSpec {
  import TransparentTraitsSpec._
  import TransparentTraitsSpecHelper._

  "Non Transparent Trait" in {

    import NonTransparent._
    val xs = Set(Circle, Square)
    val expectedElementType =
      "TransparentTraitsSpec.NonTransparent.ShapeType & TransparentTraitsSpec.NonTransparent.ConcreteType"
    assert(showElementType(xs) == expectedElementType)

  }

  "Transparent Trait" in {

    import Transparent._
    val xs = Set(Circle, Square)
    val expectedElementType =
      "TransparentTraitsSpec.Transparent.ShapeType"
    assert(showElementType(xs) == expectedElementType)

  }

}
