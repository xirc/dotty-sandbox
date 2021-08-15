package macros

import testing.BaseSpec

final class PowerSpec extends BaseSpec {

  import Power.*

  "power with constant value" in {

    assert(power(2, 10) == 1024)

    val _3: Int = 3
    assert(power(_3, 10) == math.pow(_3, 10))

  }

}
