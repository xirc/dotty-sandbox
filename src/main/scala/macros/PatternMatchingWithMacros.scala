package macros

// format: off
object PatternMatchingWithMacros {

  import scala.quoted.*

  inline def sum(inline args: Int*): Int = ${ sumExpr('args) }

  private def sumExpr(argsExpr: Expr[Seq[Int]])(using Quotes): Expr[Int] =
    argsExpr match
      case Varargs(args@Exprs(argValues)) =>
        Expr(argValues.sum)
      case Varargs(argExprs) =>
        val staticSum: Int = argExprs.map(_.value.getOrElse(0)).sum
        val dynamicSum: Seq[Expr[Int]] = argExprs.filter(_.value.isEmpty)
        dynamicSum.foldLeft(Expr(staticSum))((acc, arg) => '{ $acc + $arg })
      case _ =>
        '{ $argsExpr.sum }

  object QuotedPatterns {

    def sum(args: Int*): Int = args.sum

    inline def optimize(inline arg: Int): Int = ${optimizeFor('arg)}

    private def optimizeFor(body: Expr[Int])(using Quotes): Expr[Int] =
      body match
        case '{ sum() } => Expr(0)
        case '{ sum($n) } => n
        case '{ sum(${Varargs(args)}*) } => sumExpr(args)
        case body => body

    private def sumExpr(args: Seq[Expr[Int]])(using Quotes): Expr[Int] =
      def flatSumArgs(arg: Expr[Int]): Seq[Expr[Int]] = arg match
        case '{ sum(${Varargs(subArgs)} *) } => subArgs.flatMap(flatSumArgs)
        case _ => Seq(arg)
      val args2 = args.flatMap(flatSumArgs)
      val staticSum: Int = args2.map(_.value.getOrElse(0)).sum
      val dynamicSum: Seq[Expr[Int]] = args2.filter(_.value.isEmpty)
      dynamicSum.foldLeft(Expr(staticSum))((acc, arg) => '{ $acc + $arg })
  }

  object RecoveringPreciseTypesUsingPatterns {

    extension (inline sc: StringContext)
      inline def showMe(inline args: Any*): String = ${ showMeExpr('sc, 'args) }

    private def showMeExpr(sc: Expr[StringContext], argsExpr: Expr[Seq[Any]])(using Quotes): Expr[String] =
      import quotes.reflect.report
      argsExpr match
        case Varargs(argExprs) =>
          val argShowedExpr = argExprs.map {
            case '{ $arg: tpe } =>
              Expr.summon[Show[tpe]] match
                case Some(showExpr) =>
                  '{ $showExpr.show($arg) }
                case None =>
                  report.error(s"Could not find implicit for ${ Type.show[Show[tpe]]}", arg)
                  '{ ??? }
          }
          val newArgsExpr = Varargs(argShowedExpr)
          '{ $sc.s($newArgsExpr*) }
        case _ =>
          report.error("Args must be explicit", argsExpr)
          '{ ??? }
          
    trait Show[-T]:
      def show(x: T): String

  }
  
  object OpenCodePattern {
    
    inline transparent def eval(inline e: Int): Int = ${ evalExpr('e) }
    
    private def evalExpr(e: Expr[Int])(using Quotes): Expr[Int] = e match
      case '{ val y: Int = $x; $body(y): Int } =>
        evalExpr(Expr.betaReduce(
          '{ $body(${evalExpr(x)}) }
        ))
      case '{ ($x: Int) * ($y: Int) } =>
        (x.value, y.value) match
          case (Some(a), Some(b)) => Expr(a * b)
          case _ => e
      case _ => e
    
  }

}
