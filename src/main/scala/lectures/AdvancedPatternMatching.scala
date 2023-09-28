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
    def unapply(arg: Int): Option[Boolean] =
      if (arg % 2 == 0) Some(true)
      else None
  }

  object singleDigit {
    def unapply(arg: Int): Option[Boolean] =
      if (arg > -10 && arg < 10) Some(true)
      else None
  }
//  val myInteger = new Integer2(3)
  val num = 4
  val mathProperty = num match {
    case singleDigit(_) => "single digit"
    case even(_) => "an even number"
    case _ => "no property"
  }

  println(mathProperty)

}






















