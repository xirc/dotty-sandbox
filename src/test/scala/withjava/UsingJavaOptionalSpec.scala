package withjava

import testing.BaseSpec
import scala.jdk.OptionConverters.*

/** [[https://docs.scala-lang.org/scala3/book/interacting-with-java.html Interacting with Java]] */
final class UsingJavaOptionalSpec extends BaseSpec {

  "using Java Optional in Scala" in {
    import java.util.Optional

    val some: Optional[String] = UsingJavaOptional.some
    val none: Optional[String] = UsingJavaOptional.none

    val scalaSome = some.toScala
    val scalaNone = none.toScala

    assert(scalaSome == Some("abc"))
    assert(scalaNone == None)

  }

}
