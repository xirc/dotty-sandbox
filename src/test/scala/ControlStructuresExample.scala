private final class ControlStructuresExample extends BaseSpec {

  "if/else" in {

    val x = 1
    val s1 =
      if x < 0 then
        "negative"
      else if x == 0 then
        "zero"
      else
        "positive"
    assert(s1 == "positive")

    val s2 =
      if (x < 0) then
        "negative"
      else if (x == 0) then
        "zero"
      else
        "positive"
    assert(s2 == "positive")

    val s3 =
      if x < 0 then "negative"
      else if x == 0 then "zero"
      else "positive"
    assert(s3 == "positive")

  }

  "for loop" in {

    val ints = List(1, 2, 3, 4, 5)

    // Example 1
    for i <- ints do println(i)

    // Example 2
    for (i <- ints) do println(i)

    // Example 3
    for i <- ints do
      println(i)

  }

  "for with guards" in {

    val ints = List(1, 2, 3, 4, 5)

    // Exmaple 1
    for i <- ints if i > 2 do println(i)

    // Example 2
    for
      i <- ints if i % 2 == 0
      j <- ints if j % 2 == 1
    do
      println(s"$i $j")

    // Example 3
    for {
      i <- ints if i % 2 == 0
      j <- ints if j % 2 == 1
    } do
      println(s"$i $j")

  }

  "for expression" in {

    val ints = List(1, 2, 3, 4, 5)
    val expected = List("1", "2", "3", "4", "5")

    // Example 1
    val result1 =
      for i <- ints yield i.toString
    assert(result1 == expected)

    // Example 2
    val result2 =
      for (i <- ints) yield (i.toString)
    assert(result2 == expected)

    // Example 3
    val result3 =
      for { i <- ints } yield { i.toString }
    assert(result3 == expected)

  }

  "match" in {

    val x = 1
    val s = x match
      case 1 => "one"
      case 2 => "two"
      case _ => "other"
    assert(s == "one")

  }

  "match with if guard" in {

    val x = Option(1)
    val s = x match
      case None => "???"
      case Some(v) if v == 0 => "zero"
      case Some(v) if v > 0 => "positive"
      case Some(v) => "negative"
    assert(s == "positive")

  }

  "try/catch/finally" in {

    try
      ???
    catch
      case arithmeticException: ArithmeticException =>
        arithmeticException.printStackTrace()
      case others: Throwable =>
        others.printStackTrace()
    finally
      println("cleanup")

  }

  "while" in {
    var x = 1

    while x < 3
    do
      println(x)
      x += 1

    while (x < 3) {
      println(x)
      x += 1
    }

  }

}
