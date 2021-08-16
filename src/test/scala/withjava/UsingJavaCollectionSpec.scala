package withjava

import testing.BaseSpec
import scala.jdk.CollectionConverters.*

/** [[https://docs.scala-lang.org/scala3/book/interacting-with-java.html Interacting with Java]] */
final class UsingJavaCollectionSpec extends BaseSpec {

  "use Java collections in Scala" in {

    val javaList = UsingJavaCollection.getStringList
    val scalaList = javaList.asScala.map(_ + "!")

    assert(scalaList == Seq("a!", "b!", "c!"))

  }

}
