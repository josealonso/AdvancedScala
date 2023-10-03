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

  def main(args: Array[String]): Unit = {

    // runInParallel()
    demoBankingProblem()
  }

}
