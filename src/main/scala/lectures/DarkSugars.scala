package lectures

object DarkSugars extends App {

  // methods with single param
  def singleArgMethod(arg: Int) = s"The param is $arg"

  val description = singleArgMethod{
    // some complex code
    42
  }

  List(1,2,3).map { x =>
    x + 1
  }

  // 2.- single abstract method
  trait Action {
    def act(x: Int): Int
  }

  val anInstance : Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  val aFunkyInstance: Action = (x: Int) => x + 1  // magic

  val aThread = new Thread(() => println("sweet syntax sugar !!"))

  // 3.- the :: and #:: methods are special
  val prependedList = 2 :: List(3,4)
  // The associativity of a method is determined by the operator's last character
  // If a method ends in a colon, it's right-associative, left-associative otherwise
  1 :: 2 :: 3 :: List (4,5)
  List(4,5).::(3).::(2).::(1)     // equivalent
  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this  // actual implementation
  }
   val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

   // 4.- multi-word method naming
   class Girl(name: String) {
     def `and then said`(gossip: String) = println(s"$name said $gossip")
   }
   val alice = new Girl("Alice")
   alice `and then said` "I love Scala!"

   // 5.- infix types in generics
   class Composite[A, B]
   val composite: Int Composite String = ???

   class -->[A, B]
   val towards: Int --> String = ???

   // Used in type-level programming

   // 6.- update() is very special, much like apply()
   val anArray = Array(1,2,3)
   anArray(2) = 7  // rewritten to anArray.update(2, 7)
   // used in mutable collections

   // 7.- setters for mutable containers
   class Mutable {
     private var internalMember = 0
     def member: Int = internalMember  // "getter
     def member_=(value: Int): Unit =
       internalMember = value
   }

   val aMutableContainer = new Mutable
   aMutableContainer.member = 23  // rewritten as aMutableContainer.member_=(23)

}














