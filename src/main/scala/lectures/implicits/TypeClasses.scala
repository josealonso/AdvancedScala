package lectures.implicits

object TypeClasses {

  // First attempt
  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name $age years old <a href=$email/></div>"
  }

  val alice = User("Alice", 23, "alice@email.com")

  //////////////////////////////////////////////

  // TYPE CLASS
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  /*
    Equality
   */
  trait Equal[T] {  // This is the type class
    def apply(a: T, b: T): Boolean
  }

  object NameEquality extends Equal[User] {  // This is a type class instance
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {  // This is another type class instance
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

}
















