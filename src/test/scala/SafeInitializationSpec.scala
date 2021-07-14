/** [[https://dotty.epfl.ch/docs/reference/other-new-features/safe-initialization.html Safe Initialization]]
  */
object SafeInitializationSpec {
  //
  // Parent-Child Interaction
  //
  abstract class AbstractFile:
    def name: String
    // The next line will raise a warning.
    // val extension2: String = name.substring(4).nn
    def extension: String = name.substring(4).nn
  final class RemoteFile(url: String) extends AbstractFile:
    val localFile: String = s"${url.##}.tmp"
    def name: String = localFile

  //
  // Inner-Outer Interaction
  //
  object Trees:
    class ValDef { counter += 1 }
    class EmptyValDef extends ValDef
    // The next line will raise a warning.
    // val theEmptyValDef = new EmptyValDef()
    private var counter: Int = 0
    val theEmptyValDef = new EmptyValDef()

  //
  // Functions
  //
  abstract class Parent:
    val f: () => String = () => this.message
    def message: String
  final class Child extends Parent:
    // The next line will raise a warning.
    // val a: String = f()
    val b: String = "hello"
    val c: String = f()
    def message: String = b

}
