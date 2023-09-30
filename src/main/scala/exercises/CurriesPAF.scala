package exercises

object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3)  // Int => Int => y = 3 + y
  println(add3(5))
  println(superAdder(3)(5))  // curried function

  // Method!
  def curriedAdder(x: Int)(y: Int): Int = x + y  // curried method

//  val add4 = curriedAdder(4)   // compiler error
  val add4: Int => Int = curriedAdder(4)   // The compiler converts a method into a function value
  println(add4(2))
  // we want to use function values in HOFs
  // lifting: transforming a method to a function. Also called ETA-EXPANSION

  //  def simpleAddFunction2 = (x: Int, y: Int) => x + y  // valid syntax

  // EXERCISE
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y
  // add7: Int => Int = y => 7 + y
  // implement add7 using the above three lines

  val add7_v1: Int => Int = curriedAddMethod(7)
  val add7_v5 = curriedAddMethod(7)(_)  // PAF
  val add7_Scala3 = curriedAddMethod(7)  // this only works in Scala 3
  println("v1: " + add7_v1(10))
  val add7_v2 = (x: Int, y: Int) => simpleAddMethod(7, x)
  println("v2: " + add7_v2(10, 30))
  val add7_v3 = (x: Int) => simpleAddFunction(7, x)
  val add7_v4 = simpleAddFunction(7, _:Int)
  println("v3: " + add7_v3(10))
  println("v4: " + add7_v4(10))
  println("v5: " + add7_v5(10))

  // underscores are powerful
  def concatenator(a: String, b: String, c:String) = a + b +c
  val insertName = concatenator("Hello, I'm ", _: String, ", how are you?")
  println(insertName("José"))
  val fillInTheBlanks = concatenator("Hello, ", _, _)
  println(fillInTheBlanks("Alice", " good morning!"))


}

/*
- Never use Array unless you are either 1) doing Java interop or 2) writing super high performance code that you know is a performance bottleneck for your app)

 */















