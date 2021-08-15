package macros

// format: off
object ImplicitsWithinMacros {
  import scala.quoted.*
  import scala.collection.immutable.*
  
  inline def setFor[T]: Set[T] = ${ setForExpr[T] }
  
  private def setForExpr[T: Type](using Quotes): Expr[Set[T]] =
    Expr.summon[Ordering[T]] match
      case Some(ordering) => '{ new TreeSet[T]()($ordering) }
      case _ => '{ new HashSet[T] }

}
