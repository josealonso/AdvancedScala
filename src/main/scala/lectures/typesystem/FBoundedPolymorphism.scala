package lectures.typesystem

object FBoundedPolymorphism extends App {

  object Problem {
    trait Animal {
      def eat: List[Animal]
    }

    class Cat extends Animal {
      override def eat: List[Cat] = ???
    }

    class Dog extends Animal {
      override def eat: List[Dog] = ???
    }
  }

  // How to make the compiler force type correctness

  // Solution #1
  object Solution1 {
    trait Animal[A <: Animal[A]] { // recursive type: F-Bounded Polymorphism (FBP)
      def eat: List[Animal[A]]
    }

    class Cat extends Animal[Cat] {
      override def eat: List[Animal[Cat]] = ???
    }

    class Dog extends Animal[Dog] {
      override def eat: List[Animal[Dog]] = ???
    }
  }
  // It is often used in database APIs (ORMs)

  // Solution #2 - FBP + self-types
  object Solution2 {
    trait Animal[A <: Animal[A]] {
      self: A =>
      def eat: List[Animal[A]]
    }

    class Cat extends Animal[Cat] {
      override def eat: List[Animal[Cat]] = ???
    }

    class Dog extends Animal[Dog] {
      override def eat: List[Animal[Dog]] = ???
    }
  }

  // Those solutions do not work for complex hierarchies (over one level of inheritance)
  object ProblemInComplexHierarchies {
    trait Animal[A <: Animal[A]] {
      self: A =>
      def eat: List[Animal[A]]
    }

    trait Fish extends Animal[Fish]

    class Shark extends Fish {
      override def eat: List[Animal[Fish]] = ???
    }

    class Cod extends Fish {
      override def eat: List[Animal[Fish]] = List(new Shark) // Wrong, but compiles!
    }
  }

  // Solution #3 - type classes
  object SolutionWithTypeClasses {

    trait Animal
    trait CanEat[A] {
      def eat(a: A): List[A]
    }

    class Dog extends Animal
    object Dog {
      implicit object DogsCanEat extends CanEat[Dog] {
        override def eat(a: Dog): List[Dog] = List()
      }
    }

    class Cat extends Animal
    object Cat {
      implicit object CatsCanEat extends CanEat[Dog] { // this error is detected
        override def eat(a: Dog): List[Dog] = List() // when cat.eat is executed
      }
    }

    implicit class CanEatOps[A](animal: A) {
      def eat(implicit canEat: CanEat[A]): List[A] =
        canEat.eat(animal)
    }

    val dog = new Dog
    dog.eat
    /*
      converted to CanEatOps[Dog](dog).eat(Dog.DogsCanEat)
      implicit value to pass to eat: Dog.DogsCanEat
    */
    val cat = new Cat
    // cat.eat
    // Caveat: too abstract solution, not very explicit
  }

  // Solution #4 - type classes
  object SolutionWithTypeClassesMoreReadable {

    trait Animal[A] {
      def eat(a: A): List[A]
    }

    class Dog
    object Dog {
      implicit object DogAnimal extends Animal[Dog] {
        override def eat(a: Dog): List[Dog] = List()
      }
    }

    class Cat
    object Cat {
      implicit object CatAnimal extends Animal[Dog] {
        override def eat(a: Dog): List[Dog] = List()
      }
    }

    implicit class AnimalOps[A](animal: A) {
      def eat(implicit animalTypeClassInstance: Animal[A]): List[A] =
        animalTypeClassInstance.eat(animal)
    }

    val dog = new Dog
    dog.eat
    val cat = new Cat
    cat.eat     // compile error!!
  }

}

