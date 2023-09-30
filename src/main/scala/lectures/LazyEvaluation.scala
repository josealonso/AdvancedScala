package lectures

object LazyEvaluation extends App {

  lazy val aLazyVal: Int = throw new RuntimeException

  // A lazy expression in ONLY evaluated when it is used for the first time
  lazy val x = {
    println("hello")
    42
  }
  println(x)
  println(x)

  // examples of implications:
  // 1.- side effects
  def sideEffectCondition = {
    println(".....")
    true
  }
  def simpleCondition = false

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no")
  println(lazyCondition)

  // 2.- in conjunction with call by name
  def byNameMethod(n: => Int): Int = n + n + 1
  def retrieveMagicValue = {
    // side effect or long computation
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMagicValue))
  // Problem: the byNameMethod is evaluated twice
  // Solution: use lazy vals, so the byNameMethod is evaluated only once
  // This technique is called CALL BY NEED
  def byNameMethod_v2(n: => Int): Int = {
    lazy val t = n
    t + t + 1
  }

  println(byNameMethod_v2(retrieveMagicValue))

  // filtering with lazy vals
  def lessthan30(i: Int) = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterthan20(i: Int) = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessthan30)
  val gt20 = lt30.filter(greaterthan20)
  println(gt20)

  // "withFilter" uses lazy vals under the hood
  val lt30lazy = numbers.withFilter(lessthan30)
  val gt20lazy = lt30lazy.withFilter(greaterthan20)
//  println(gt20lazy)  // Not in order
  gt20lazy.foreach(println)  // Printing it in order

  // for-comprehensions use withFilter with guards
  for {
    a <- List(1,2,3) if a % 2 == 0
  } yield a + 1
  // The compiler translates it to
  // List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1)

}
























