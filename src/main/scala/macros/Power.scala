package macros

// format: off
object Power {
  import scala.quoted.*
  
  inline def power(x: Double, inline n: Int) = ${ powerCode('x, 'n)}
  
  private def powerCode(x: Expr[Double], n: Expr[Int])(using Quotes): Expr[Double] =
    n.value match
      case Some(m) => powerCode(x, m)
      case None => '{ Math.pow($x, $n.toDouble) }
      
  private def powerCode(x: Expr[Double], n: Int)(using Quotes): Expr[Double] =
    if n == 0 then '{ 1.0 }
    else if n == 1 then x
    else if n % 2 == 0 then '{ val y = $x * $x; ${ powerCode('y, n / 2) } }
    else '{ $x * ${ powerCode(x, n - 1) } }
  
}
