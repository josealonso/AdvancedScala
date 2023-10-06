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

  /*
    Write an API for a parking:
      Parking[T](things: List[T]) {
        park(vehicle: T)
        impound(vehicles: List[T])
        checkVehicles(conditions: String): List[T]
      }
    2.- Use someone else's API: IList[T]
    3.- Declare a flatMap function (monads)
  */

  class Vehicle
  class Car extends Vehicle
  class Bike extends Vehicle

  // Invariant version
  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???
    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  // Covariant version
  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ???   // widening our type
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???
    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  // Contravariant version
  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???
    def flatMap[R <: T, S](f: R => XParking[S]): XParking[S] = ???
  }

  /*
    Rule of thumb
    - use covariance for a COLLECTION OF THINGS
    - use contravariance for a GROUP OF ACTIONS
   */

}



















