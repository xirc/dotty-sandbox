package reflection

// format: off
object Reflection {
  import scala.quoted.*
  
  inline def natConst(inline x: Int): Int = ${ natConstImpl('x) }
  
  private def natConstImpl(x: Expr[Int])(using quotes: Quotes): Expr[Int] = {
    import quotes.reflect.*
    val tree = x.asTerm
    tree match
      case Inlined(_, _, Literal(IntConstant(n))) =>
        if n <= 0 then
          report.error("Parameter must be natural number")
          '{ 0 }
        else
          tree.asExprOf[Int]
      case _ =>
        report.error("Parameter must be a known constant")
        '{ 0 }

  }
  
}
