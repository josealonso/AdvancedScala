package lectures.implicits

object OrganizingImplicits extends App {

  implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  // implicit def reverseOrdering(): Ordering[Int] = Ordering.fromLessThan(_ > _)   // Not valid, since it is not an accesor method

  println(List(1,4,5,3,2).sorted)
 /*
   Implicits (used as implicit parameters):
     - val/var
     - object
     - accesor methods = defs with no parentheses
  */

  case class Person(name: String, age: Int)

  val persons = List(
    Person("Stuart", 30),
    Person("Anna", 22),
    Person("Jesus", 66)
  )

//  implicit def alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan(
//    (a, b) => a.name.compareTo(b.name) < 0)

  /*
    Implicit scope, from highest to lowest priority
    - normal scope = local scope
    - imported scope
    - companion object of all types involved in the method signature
      - List
      - Ordering
      - all the types involved = A or any supertype
   */

  object AlphabeticNameOrdering {
    implicit def alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan(
        (a, b) => a.name.compareTo(b.name) < 0)
  }

  object ageOrdering {
    implicit def ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  }

//  import ageOrdering.*
  import AlphabeticNameOrdering.*
  print(persons.sorted)

  /*
    Exercise
   */
  case class Purchase(numOfUnits: Int, unitPrice: Double)

  object Purchase {  // Good practice: define the most used ordering in the companion object
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(
      (a, b) => (a.numOfUnits * a.unitPrice) < (b.numOfUnits * b.unitPrice))
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.numOfUnits < _.numOfUnits)
  }

  val purchase1 = Purchase(10, 2000)
  val purchase2 = Purchase(8, 4000)
  val purchase3 = Purchase(2, 1000)
  val purchases = List(purchase1, purchase2, purchase3)

  import UnitPriceOrdering.*
//  import UnitCountOrdering.*
  println()
  println(purchases.sorted)
  println(purchases.sorted(unitPriceOrdering))
}













