package macros

import scala.quoted.{Expr, Quotes, Type}

object ShowType {

  inline def showElementType[T](xs: IterableOnce[T]): String =
    showType[T]

  inline def showType[T]: String =
    ${ showTypeImpl(using Type.of[T]) }

  def showTypeImpl[T](using Type[T])(using Quotes): Expr[String] = Expr(Type.show[T])

}
