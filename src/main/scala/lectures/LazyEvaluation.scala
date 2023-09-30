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
}
