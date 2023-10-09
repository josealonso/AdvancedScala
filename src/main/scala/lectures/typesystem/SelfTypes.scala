package lectures.typesystem

object SelfTypes extends App {

  // requiring a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer { self: Instrumentalist =>  // SELF TYPE (any word can be used)
    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def sing(): Unit = ???
    override def play(): Unit = ???
  }

//  class Vocalist extends Singer {     // Illegal, because it doesn't extend Instrumentalist
//    override def sing(): Unit = ???
//  }

  val ericClapton = new Singer with Instrumentalist {
    override def sing(): Unit = ???
    override def play(): Unit = ???
  }

  // It is NOT the same as inheritance
  class A
  class B extends A  // B is an A

  trait T
  trait S { self: T => }   // S requires a T

  // CAKE PATTERN => "dependency injection"
  // Unlike DI in Java, the components are injected from the same file // todo
  trait ScalaComponent {
   // API
    def action(x: Int): String
  }
  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " whatever"
  }
  trait ScalaApplication { self: ScalaComponent with ScalaDependentComponent => }

  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  // layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics
}

















