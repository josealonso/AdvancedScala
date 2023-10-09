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
    trait Animal[A <: Animal[A]] { self: A =>
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
    trait Animal[A <: Animal[A]] { self: A =>
      def eat: List[Animal[A]]
    }
    trait Fish extends Animal[Fish]
    class Shark extends Fish {
      override def eat: List[Animal[Fish]] = ???
    }

    class Cod extends Fish {
      override def eat: List[Animal[Fish]] = List(new Shark)  // Wrong, but compiles!
    }
  }

}
















