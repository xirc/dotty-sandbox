package macros

// format: off
object MacroIntroduction {

  import scala.quoted.*

  inline def assertP(inline predicate: Boolean): Unit = ${
    assertImpl('predicate)
  }

  private def assertImpl(predicate: Expr[Boolean])(using Quotes): Expr[Unit] = '{
    if !$predicate then
      throw AssertionError(s"failed assertion: ${ ${ showExpr(predicate) } }")
  }

  private def showExpr[T](expr: Expr[T])(using Quotes): Expr[String] = {
    val code: String = expr.show
    Expr(code)
  }

}
