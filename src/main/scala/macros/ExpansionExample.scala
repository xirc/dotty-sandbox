package macros

// format: off
object ExpansionExample {

  import scala.quoted.*

  private def map[T](arr: Expr[Array[T]], f: Expr[T] => Expr[Unit])
            (using Type[T], Quotes): Expr[Unit] = '{
    var i: Int = 0
    while i < $arr.length do
      val element = $arr(i)
      ${ f('element) }
      i += 1
  }
  
  private def sum(arr: Expr[Array[Int]])(using Quotes): Expr[Int] = '{
    var sum = 0
    ${ map(arr, x => '{ sum += $x }) }
    sum
  }
  
  inline def sum(arr: Array[Int]): Int = ${ sum('arr) }

}
