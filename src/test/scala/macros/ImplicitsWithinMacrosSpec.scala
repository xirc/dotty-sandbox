package macros

import testing.BaseSpec

final class ImplicitsWithinMacrosSpec extends BaseSpec {

  import ImplicitsWithinMacros.*
  import scala.collection.immutable.*

  class Box[T](val value: T)

  "TreeSet" in {

    assert(setFor[Int].isInstanceOf[TreeSet[Int]])

  }

  "HashSet" in {

    assert(setFor[Box[Int]].isInstanceOf[HashSet[Box[Int]]])

  }

}
