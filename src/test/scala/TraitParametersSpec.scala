object TraitParametersSpec {

  // Trait with Parameters
  trait WithName(val name: String):
    def withName[A](value: A): (String, A) =
      name -> value

  class ClassA extends WithName("abc")
  class ClassB extends ClassA

  trait WithPrefix(val prefix: String) extends WithName:
    override def withName[A](value: A): (String, A) =
      (prefix + name) -> value

  class ClassC extends WithName("abc"), WithPrefix("hello-")

  // Trait with Context Parameters
  trait Show[A]:
    def show(a: A): String
  trait GreetingHi[A](using val instance: Show[A]):
    def message(a: A): String = s"Hi, ${instance.show(a)}"
  trait GreetingHello[A] extends GreetingHi[A]:
    override def message(a: A): String = s"Hello, ${instance.show(a)}"
  class Greeting[A](using instance: Show[A]) extends GreetingHello[A]

}

/** [[https://dotty.epfl.ch/docs/reference/other-new-features/trait-parameters.html Trait Parameters]]
  */
final class TraitParametersSpec extends BaseSpec {
  import TraitParametersSpec._

  "Trait with Parameters" in {

    val valueA = ClassA()
    assert(valueA.withName(1) == ("abc" -> 1))

    val valueB = ClassB()
    assert(valueB.withName(2) == ("abc" -> 2))

    val valueC = ClassC()
    assert(valueC.withName(3) == ("hello-abc" -> 3))

  }

  "Trait with Context Parameters" in {

    implicit val intShow = new Show[Int] {
      def show(a: Int): String = a.toString
    }
    val greeting = Greeting()
    assert(greeting.message(123) == "Hello, 123")

  }

}
