package lectures

object AdvancedPatternMatching extends App {

  /*
    In order to apply pattern matching to a regular class, not a case class,
    The "unapply" method of an object must be defined.
   */
   class Person(val name: String, val age: Int)

   object Person {   // The object name can be anything
     def unapply(person: Person): Option[(String, Int)] = Some(person.name, person.age)
     def unapply(age: Int): Option[String] =
       Some(if (age < 21) "minor" else "major")
   }

   val bob = new Person("Bob", 25)
   val greeting = bob match {
     case Person(n, a) => s"Hi, my name is $n and I am $a years old."
   }

   println(greeting)

   val legalStatus = bob.age match {
     case Person(status) => s"My legal status is $status"
   }
   println(legalStatus)

  /*
    Exercise: pattern matching for even numbers
   */
  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }

  val num = 4
  val mathProperty = num match {
    case singleDigit() => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }

  println(mathProperty)

  // infix patterns
  case class Or[A, B](a: A, b: B)
  val either = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s"$number is written as $string"
  }
  println(humanDescription)

  // decomposing sequences
  val numbers = List(1,2,3,4)
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyListPattern {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyListPattern(1, 2, _*) => "starting with 1, 2"
    case _ => "something else"
  }

  println(decomposed)


}






















