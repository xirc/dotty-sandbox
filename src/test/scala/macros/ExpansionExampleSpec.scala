package macros

import testing.BaseSpec

final class ExpansionExampleSpec extends BaseSpec {

  import ExpansionExample.*

  "sum" in {

    val arr = Array(1, 2, 3, 4, 5)
    (sum(arr) == 15)

  }

}
