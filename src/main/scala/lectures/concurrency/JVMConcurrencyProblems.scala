package lectures.concurrency

object JVMConcurrencyProblems {

  def runInParallel(): Unit = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    println(x)   // race condition

  }

  case class BankAccount(var amount: Int)

  def buy(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    /*
      This operation is not atomic, it involves 3 steps:
      - read old value
      - compute result
      - write new value
     */
    bankAccount.amount-= price  // critical section
  }

  // Solution in imperative languages
  def buySafe(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    bankAccount.synchronized {  // does not allow multiple threads to run the critical section AT THE SAME TIME
      bankAccount.amount -= price
    }
  }

  def demoBankingProblem(): Unit = {
    (1 to 10000).foreach { _ =>
      val account = new BankAccount(50000)
      val thread1 = new Thread(() => buy(account, "shoes", 3000))
      val thread2 = new Thread(() => buy(account, "phone", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if (account.amount != 43000) println(s"I've just broken the bank: ${account.amount}")
    }
  }

  /*
    Exercises
    1.- create "inception threads"
      thread 1
        -> thread 2
            -> thread 3
              ..........
       each thread prints "hello from thread $i" in reverse order

    2.- What's the max/min value of x?
    3.- "sleep fallacy: what's the value of the message"
   */

  // 1
  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread =
    new Thread(() => {
      if (i < maxThreads)  {
        val newThread = inceptionThreads(maxThreads, i + 1)
        newThread.start()
        newThread.join()
      }
      println(s"Hello from thread $i")
    })

  // 2
  def minMaxX(): Unit = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
    Thread.sleep(200)
    println(s"x is $x")
  }

  // 3
  /*
    almost always, message = "Scala is awesome"
    but it is NOT guaranteed.
    A*
  */
  def demoSleepFallacy(): Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(200)
      message = "Scala is awesome"
    })

    message = "are you sure?"
    awesomeThread.start()
    Thread.sleep(201)
    // A* Solution to guarantee that: join the worker thread
    awesomeThread.join()
    println(message)
  }


  def main(args: Array[String]): Unit = {

    // runInParallel()
    // demoBankingProblem()
    // minMaxX()
    demoSleepFallacy()
    // inceptionThreads(10).start()
  }

}
