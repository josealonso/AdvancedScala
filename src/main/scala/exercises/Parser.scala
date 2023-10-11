package exercises

object Parser extends App {

  trait StrParser[T] {
    def parse(s: String): T
  }
  object StrParser {
  // Implicits in the companion object are treated specially, and
  // do not need to be imported into scope in order to be used as an implicit parameter.
    implicit object ParseInt extends StrParser[Int] {
      override def parse(s: String): Int = s.toInt
    }
    implicit object ParseBoolean extends StrParser[Boolean] {
      override def parse(s: String): Boolean = s.toBoolean
    }
    implicit object ParseDouble extends StrParser[Double] {
      override def parse(s: String): Double = s.toDouble
    }
  }  // End of the companion object

  // without implicits
  val parserOld = StrParser.ParseInt.parse("102")
  println(parserOld.toString)

  // with implicits
  def parserFromString[T](s: String)(implicit strParser: StrParser[T]) = {
    strParser.parse(s)
  }

//  val myInt = parserFromString("102")
  val myInt = parserFromString[Int]("102")
  val myDouble = parserFromString[Double]("1.02")
  val myBoolean = parserFromString[Boolean]("true")
  println("With implicits: " + myInt.toString)

  /*
  Most of the things we have done with Typeclass Inference could also be achieved using runtime reflection.
  However, relying on runtime reflection is fragile, and it is very easy for mistakes, bugs, or
  mis-configurations to make it to production before failing catastrophically.
  In contrast, Scala's implicit feature lets you achieve the same outcome but in a safe fashion:
  mistakes are caught early at compile-time.

  Most statically typed programming languages can infer types to some degree: even if not every expression is
  annotated with an explicit type, the compiler can still figure out the types based on the program structure.
  Typeclass derivation is effectively the reverse: by providing an explicit type, the compiler can infer the
  program structure necessary to provide a value of the type we are looking for.
  */

}

























