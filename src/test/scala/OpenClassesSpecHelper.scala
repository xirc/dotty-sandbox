object OpenClassesSpecHelper {

  open class Formatter[T]:
    def format(x: T): String = x.toString

  class NonOpenFormatter[T]:
    def format(x: T): String = x.toString

}
