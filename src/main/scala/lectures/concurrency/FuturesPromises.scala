package lectures.concurrency

import scala.concurrent.Future
import scala.util.{Failure, Success}

// important for futures
import scala.concurrent.ExecutionContext.Implicits.global

object FuturesPromises extends App {

  def computation: Int = {
    Thread.sleep(2000)
    23
  }

  val aFuture = Future {
    computation
  } // (global) is injected by the compiler

  println(aFuture.value)   // returns None

  println("Waiting on the future")
  aFuture.onComplete {
    case Success(value) => println(s"The result is $value")
    case Failure(exception) => println(s"I have failed with $exception")
  }  // onComplete is called by some thread, we do not know which one

  Thread.sleep(3000)
}
