import ProgrammaticStructuralTypesSpec.MyCloseable

object ProgrammaticStructuralTypesSpec {

  //
  // Using Selectable
  //
  class Record(elems: (String, Any)*) extends Selectable:
    private val fields = elems.toMap
    def selectDynamic(name: String): Any = fields(name)

  type Person = Record {
    val name: String
    val age: Int
  }
  object Person {
    def fromCSV(row: String): Person =
      row.trim.split(",") match
        case Array(name, age) =>
          Record("name" -> name.trim, "age" -> age.trim.toInt).asInstanceOf[Person]
        case _ => throw new IllegalArgumentException()
  }

  //
  // Using JavaReflection
  // (Consider alternatives before using this technique)
  //
  type MyCloseable = {
    def close(): Unit
  }
  object MyCloseable:
    import scala.reflect.Selectable.reflectiveSelectable
    def autoClose[T <: MyCloseable, U](f: T)(op: T => U): U =
      try op(f)
      finally f.close()

}

/** [[https://dotty.epfl.ch/docs/reference/changed-features/structural-types.html Programmatic Structural Types]]
  */
final class ProgrammaticStructuralTypesSpec extends BaseSpec {
  import ProgrammaticStructuralTypesSpec.*

  "using Selectable" in {

    val p = Person.fromCSV("bob, 33")
    assert(p.name == "bob")
    assert(p.age == 33)

  }

  "using Java Reflection" in {
    import MyCloseable.*
    import scala.reflect.Selectable.reflectiveSelectable

    class MyCloseableInstance:
      var closed = false
      def close(): Unit =
        closed = true
    val instance = MyCloseableInstance()

    assert(instance.closed == false)
    assert(autoClose(instance)(_.closed) == false)
    assert(instance.closed == true)

  }

  "Local Selectable Instances" in {

    trait Vehecle extends reflect.Selectable:
      def wheels: Int

    val instance = new Vehecle:
      def wheels: Int = 2
      def mirrors: Int = 2

    assert(instance.wheels == 2)
    assert(instance.mirrors == 2)

  }

}
