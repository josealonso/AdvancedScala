package lectures.concurrency

import java.util.concurrent.Executors

object Intro extends App {

  /*
    interface Runnable {
      public void run();
    }
   */
  // JVM threads
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel...")
  })

  val runnable = new Runnable {
    override def run(): Unit = println("2 - Running in parallel...")
  }
  aThread.start()  // gives the signal to the JVM to start a JVM thread
  // create a JVM thread on top of a OS thread
  runnable.run()  // doesn't do anything in parallel - common pitfall
  aThread.join()  // blocks until aThread finishes running

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  threadHello.start()
  threadGoodbye.start()
  // different runs produce different results!

  // threads are managed by executors
  val NUMBER_OF_THREADS = 10
  val pool = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
  pool.execute(() => println("One of the pool threads"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(2000)
    println("done after 2 seconds")
  })

  pool.shutdown()
  // pool.execute(() => println("should not appear"))  // throws an exception in the calling thread

  // pool.shutdownNow()   // interrupts the running threads
  println(pool.isShutdown)    // true
}









