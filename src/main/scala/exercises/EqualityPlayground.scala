package exercises

object EqualityPlayground extends App {

  case class User(name: String, age: Int, email: String)

  /*
    Equality
   */
  trait Equal[T] { // This is the type class
    def apply(a: T, b: T): Boolean
  }

  implicit object NameEquality extends Equal[User] { // This is a type class instance
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] { // This is another type class instance
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

//  class Equality extends Equal[User] {
//    override def apply(a: User, b: User): Boolean = super.apply(a, b)
//  }

  val alice = User("Alice", 25, "alice@email.com")
  val anotherAlice = User("Alice", 37, "anotherAlice@email.com")
  println(Equal(alice, anotherAlice))
  // Ad-hoc polymorphism

  /*
     Exercise: improve the Equal TC with an implicit conversion class
     ===(anotherValue: T)
     !==(anotherValue: T)
   */

//  implicit class RichUser(val user: User) extends AnyVal {
//    def ===(anotherUser: User): Boolean = user.name == anotherUser.name &&
//      user.age == anotherUser.age && user.email == anotherUser.email
//
//    def !==(anotherUser: User): Boolean = user.name != anotherUser.name ||
//      user.age != anotherUser.age || user.email != anotherUser.email
//  }

  /********************************************/
  /******* A better way (using generics) ******/
  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, other)

    def !==(other: T)(implicit equalizer: Equal[T]): Boolean = ! equalizer.apply(value, other)
  }


  println(alice === anotherAlice)
  /*  Converted to --->
    alice.=== anotherAlice
    new TypeSafeEqual[User](alice).===(anotherAlice)
    new TypeSafeEqual[User](alice).===(anotherAlice)(NameEquality)
   */
  println(alice !== anotherAlice)
}


















