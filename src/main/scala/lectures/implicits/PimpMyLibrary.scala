package lectures.implicits

import scala.annotation.tailrec
import scala.language.implicitConversions

object PimpMyLibrary extends App {

  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0
    def times(function: () => Unit): Unit = {
      @tailrec
      def timesAux(n: Int): Unit =
        if (n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }

      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      // @tailrec
      def concatenate(n: Int): List[T] =
        if (n <= 0) List()
        else concatenate(n - 1) ++ list

      concatenate(value)
    }
  }

  val num = 42
  new RichInt(num).isEven

  println(s"Is $num even? " + num.isEven)

  33.isEven  // converted to new RichInt(33).isEven
  // This is called type enrichment or pimping

  implicit class RicherInt(val value: Int) extends AnyVal {
    def isOdd: Boolean = value % 2 != 0
  }
    // 24.isOdd does not compiler, because
    // the compiler does NOT do multiple implicit searches.

  implicit class RichString (val str: String) extends AnyVal {
    def asInt: Int = str.toInt    // or Integer.valueOf(str)
  }

  println("560".asInt)
  3.times(() => println("I love Scala!"))
  println(4 * List(1,2))

  // "3" / 4
  implicit def stringToInt(string: String): Int = Integer.valueOf(string)
  println("6"/3)

  // discouraged
  implicit def intToBoolean(i: Int): Boolean = i == 1

  val aConditionedValue = if (3) "Fine" else "Something wrong"
  println(aConditionedValue)
}

/*
  Good practices for pimp libraries
  - keep type enrichment to implicit classes
  - avoid implicit defs
  - package implicits clearly, bring into scope only what you need
  - if you need conversions, make them specific.
 */