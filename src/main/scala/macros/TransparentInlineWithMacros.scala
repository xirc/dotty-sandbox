package macros

// format: off
object TransparentInlineWithMacros {
  import scala.quoted.*
  
  transparent inline def defaultOf(inline str: String): Any =
    ${ defaultOfImpl('str) }
    
  private def defaultOfImpl(str: Expr[String])(using Quotes): Expr[Any] =
    str.valueOrError match
      case "int" => '{ 1 }
      case "string" => '{ "a" }
  
}
