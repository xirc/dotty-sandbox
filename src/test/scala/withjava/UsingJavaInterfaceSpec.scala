package withjava

import testing.BaseSpec

/** [[https://docs.scala-lang.org/scala3/book/interacting-with-java.html Interacting with Java]] */
final class UsingJavaInterfaceSpec extends BaseSpec {

  import UsingJavaInterface.*

  "printer" in {

    val printer = new Printer:
      override def print(message: String): String = s"$message!"

    assert(printer.print("hello") == "hello!")

  }

  "scanner" in {

    val scanner = new Scanner {}
    assert(scanner.scan() == "hello")

  }

  "implement both" in {

    class Copier extends Printer with Scanner {
      override def print(message: String): String = s"$message!"
      def copy(): String = print(scan())
    }

    val copier = Copier()
    assert(copier.copy() == "hello!")

  }

}
