object ContextFunctionsSpec {

  object Basic {

    import scala.concurrent.ExecutionContext

    type Executable[T] = ExecutionContext ?=> T

    def f(x: Int): Executable[Int] = x
    def g[T](e: Executable[T])(using ExecutionContext): T = e

  }

  object BuilderPattern {

    import scala.collection.mutable.ArrayBuffer

    class Table:
      val rows = ArrayBuffer[Row]()
      def add(row: Row): Unit = rows += row
      def apply(index: Int): Row = rows(index)
      override def toString: String = rows.mkString("Table(", ", ", ")")

    class Row:
      val cells = ArrayBuffer[Cell]()
      def add(cell: Cell): Unit = cells += cell
      def apply(index: Int): Cell = cells(index)
      override def toString: String = cells.mkString("Row(", ", ", ")")

    case class Cell(value: String)

    def table(init: Table ?=> Unit): Table = {
      given t: Table = Table()
      init
      t
    }

    def row(init: Row ?=> Unit)(using table: Table): Row = {
      given r: Row = Row()
      init
      table.add(r)
      r
    }

    def cell(value: String)(using row: Row): Cell = {
      val c = Cell(value)
      row.add(c)
      c
    }

  }

  object PostConditions {

    opaque type WrappedResult[T] = T
    def result[T](using r: WrappedResult[T]): T = r
    extension [T](x: T)
      def ensure(condition: WrappedResult[T] ?=> Boolean): T = {
        assert(condition(using x))
        x
      }

  }

}

/** [[https://dotty.epfl.ch/docs/reference/contextual/context-functions.html Context Functions]] */
final class ContextFunctionsSpec extends BaseSpec {

  "Basic" in {

    import ContextFunctionsSpec.Basic._
    import scala.concurrent.ExecutionContext.Implicits.global

    assert(f(1) == 1)
    assert(f(2) == 2)

    assert(g(3) == 3)
    assert(g(f(4)) == 4)

  }

  "Builder Pattern" in {

    import ContextFunctionsSpec.BuilderPattern._

    val tab = table {
      row {
        cell("a")
        cell("b")
      }
      row {
        cell("c")
        cell("d")
      }
    }

    assert(tab(0)(0) == Cell("a"))
    assert(tab(0)(1) == Cell("b"))
    assert(tab(1)(0) == Cell("c"))
    assert(tab(1)(1) == Cell("d"))

  }

  "PostConditions" in {

    import ContextFunctionsSpec.PostConditions._

    val s = Seq.tabulate(10)(_ + 1).sum.ensure(result == 55)
    assert(s == 55)

  }

}
