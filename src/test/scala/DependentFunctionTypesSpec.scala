import testing.BaseSpec

object DependentFunctionTypesSpec {

  //
  // Dependent Method Types
  //
  trait Key { type Value }
  final class KeyValueStore {
    import scala.collection.mutable
    private val storage = mutable.Map.empty[Key, Any]
    def get(key: Key): Option[key.Value] =
      storage.get(key).flatMap(any => Option(any.asInstanceOf[key.Value]))
    def put(key: Key, value: key.Value): Unit =
      storage.update(key, value)
  }

  //
  // Dependent Function Types
  //
  type ReadOnlyKeyValueStore = (key: Key) => Option[key.Value]
  extension (kvs: KeyValueStore) def asReadOnly: ReadOnlyKeyValueStore = kvs.get

  //
  // Case Study 1
  //
  object CaseStudy1 {

    trait Numeric:
      type Num
      def zero: Num
      def plus(x: Num, y: Num): Num
      def mult(x: Num, y: Num): Num
      def fromInt(n: Int): Num
      extension (x: Num)
        def toInt: Int
        def +(y: Num) = plus(x, y)
        def *(y: Num) = mult(x, y)

    object Numeric:
      def apply(n: Int)(using ctx: Numeric): ctx.Num = ctx.fromInt(n)

    object IntNumeric extends Numeric:
      opaque type Num = Int
      override def zero: Num = 0
      override def plus(x: Num, y: Num): Num = x + y
      override def mult(x: Num, y: Num): Num = x * y
      override def fromInt(n: Int): Num = n
      extension (n: Num) override def toInt: Int = n

    type UnaryFunction = (ctx: Numeric) => ctx.Num => ctx.Num
    type UnaryContextFunction = (ctx: Numeric) ?=> ctx.Num => ctx.Num

  }

}

/** Dependent Function Types
  *   - [[https://docs.scala-lang.org/scala3/book/types-dependent-function.html]]
  *   - [[https://docs.scala-lang.org/scala3/reference/new-types/dependent-function-types.html]]
  *   - [[https://dotty.epfl.ch/docs/reference/new-types/dependent-function-types.html]]
  *   - [[https://dotty.epfl.ch/docs/reference/new-types/dependent-function-types-spec.html]]
  *
  * Context Functions
  *   - [[https://docs.scala-lang.org/scala3/reference/contextual/context-functions.html]]
  */
final class DependentFunctionTypesSpec extends BaseSpec {
  import DependentFunctionTypesSpec.*

  "Dependent Method Types" in {

    object Name extends Key { type Value = String }
    object Age extends Key { type Value = Int }
    val kvs = KeyValueStore()

    assert(kvs.get(Name) == None)
    kvs.put(Name, "bob")
    assert(kvs.get(Name) == Option("bob"))

    assert(kvs.get(Age) == None)
    kvs.put(Age, 24)
    assert(kvs.get(Age) == Option(24))

  }

  "Dependent Function Types" in {

    object Version extends Key { type Value = String }
    val kvs = KeyValueStore()
    val rkvs = kvs.asReadOnly
    assert(rkvs(Version) == None)
    kvs.put(Version, "1.0.0")
    assert(rkvs(Version) == Option("1.0.0"))

  }

  "Case Study 1" in {
    import CaseStudy1.*

    val square: UnaryFunction = ctx => n => ctx.mult(n, n)
    assert(square(IntNumeric)(IntNumeric.fromInt(3)).toInt == 9)

  }

  "Case Study 1 (with Context Functions)" in {
    import CaseStudy1.*

    given Numeric = IntNumeric
    val square: UnaryContextFunction = n => n * n
    assert(square(Numeric(3)).toInt == 9)

  }

}
