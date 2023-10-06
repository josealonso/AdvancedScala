package lectures.implicits

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MagnetPattern extends App {

  // method overloading

  class P2PRequest
  class P2PResponse
  class Serializer[T]

  trait Actor {
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    def receive[T : Serializer](message: T): Int
    def receive[T : Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
    // lots of overloads
  }

  /*   PROBLEMS
    1.- type erasure
    2.- lifting doesn't work for all overloads
       val receiveFV = receive _   // the underscore refers to what type ?
    3.- code duplication
    4.- type inference and default args
   */

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic
      println("Handling P2P request")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic
      println("Handling P2P response")
      24
    }
  }

  receive(new P2PRequest)
  receive(new P2PResponse)

  // 1.- no more type erasure problems
  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply(): Int = 48
  }

  println(receive(Future(new P2PResponse)))
  println(receive(Future(new P2PRequest)))

  // 2.- lifting works
  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(s: String) extends AddMagnet {
    override def apply(): Int = s.toInt + 1
  }

  val addFV = add1 _
  println(addFV(1))
  println(addFV("3"))

  /*
    Drawbacks
    1.- verbose
    2.- harder to read
    3.- you can't name or place default arguments
    4.- call by name doesn't work correctly
   */







}







