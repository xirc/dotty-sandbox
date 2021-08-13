package macros

import testing.BaseSpec

final class MacroIntroductionSpec extends BaseSpec {

  import MacroIntroduction.assertP

  "test" in {

    assertP(true)

    val expectedErrorAgainstFalse = intercept[AssertionError] {
      assertP(false)
    }
    assert(expectedErrorAgainstFalse.getMessage == "failed assertion: false")

    val expectedErrorAgainstComplexExpression = intercept[AssertionError] {
      val expectedValue = 11
      assertP(1 + 2 * 7 - 4 / 2 == expectedValue - 1)
    }
    val expectedErrorMessage =
      "failed assertion: 1.+(2.*(7)).-(4./(2)).==(expectedValue.-(1))"
    assert(
      expectedErrorAgainstComplexExpression.getMessage == expectedErrorMessage,
    )

  }

}
