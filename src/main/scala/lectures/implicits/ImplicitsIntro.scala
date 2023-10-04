package lectures.implicits

import scala.language.implicitConversions

object ImplicitsIntro extends App {

  case class Person(name: String) {
    def greet = s"Hi, my name is $name"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("Alice".greet)  // converted to fromStringToPerson("Alice").greet by the compiler

  // implicit parameters
  def increment(x: Int)(implicit amount: Int) = x + amount

  implicit val defaultAmount: Int = 10   // adding the type is required in Scala 3

  println(increment(10))
  // This is not the same as the default arguments
}

