package lectures.typesystem

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  //  what is variance?
  // "inheritance" - type substitution of generics

  class Cage[T]
  // yes - covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance
  class ICage[T]
  // val icage: ICage[Animal] = new ICage[Cat]  // compiler error

  // no - the opposite is true = contravariance
  class XCage[-T]
  val xCage: XCage[Cat] = new XCage[Animal]

  /*
    Big rule
    - method arguments are in CONTRAVARIANT position
    - return types are in COVARIANT position
   */
}



















