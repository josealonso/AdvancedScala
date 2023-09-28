package lectures

object PartialFunctions extends App {

  val aFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 45
    case 5 => 222
  }  // total function
  // {1,2,5} => Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 45
    case 5 => 222
  } // partial function value

  println(aPartialFunction(2))

  // PF utilities
  println(aPartialFunction.isDefinedAt(67))

  // lift
  val lifted = aPartialFunction.lift
  println(lifted(2))
  println(lifted(94))

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF extends normal functions

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1,2,3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  // NOTE: a partial function can only have ONE parameter type

  /*
   * Exercises
   *
   */

  val myFunction = new PartialFunction[Int, Int] {
    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 3

    override def apply(x: Int): Int = x match {
      case 1 => 42
      case 2 => 78
      case 3 => 1000
    }
  }

  println(myFunction(2))

  // dumb chatbot as a PF
  scala.io.Source.stdin.getLines().foreach(line => println("you said: " + line))

}











