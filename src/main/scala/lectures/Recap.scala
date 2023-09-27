package lectures

object Recap {

  // object-oriented programming
  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog  // subtyping poymorphism

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Tiger extends Animal with Carnivore {
    override def eat(a: Animal): Unit = print("grgrgr")
  }

  // method notations
  val aTiger = new Tiger
  aTiger.eat(aDog)
  aTiger eat aDog

  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = ???
  }

  // generics
  abstract class MyList[A]   // variance and variance problem in this course
  // singletons and companions
  object MyList

  // case classes
  case class Person(name: String, age: Int)

  // exceptions and try/catch/finally
  val throwsException = throw new RuntimeException  // Nothing type

  // packaging and imports

  // functional programming
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  incrementer(1)

  val anonymousIncrementer = (x: Int) => x + 1
  List(1,2,3).map(anonymousIncrementer)  // HOF
  // map, flatMap, filter

  // for-comprehension
  val pairs = for {
    num <- List(1,2,3)
    char <- List('a', 'b', 'c')
  } yield s"$num-$char"

  // Scala collections: Seqs, Arrays, Lists, Vectors, Maps, Tuples
  val aMap = Map(
    "Alice" -> 234,
    "Bob" -> 734
  )

  // other "collections": Option, Try
  val anOption = Some(2)

  // pattern matching
  val list = List(1,2,3)
  val listType = list match {
    case List(1, _, _) => "firsts"
    case _ => "......"
  }

}
















