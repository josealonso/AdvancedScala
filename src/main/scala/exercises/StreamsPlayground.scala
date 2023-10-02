package exercises

import scala.annotation.tailrec

/*
  Implement a lazily evaluated, singly linked STREAM of elements.

  naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite)
  naturals.take(100)  // lazily evaluated stream of the first 100 naturals (finite stream)
  naturals.forEach(println)  // it will crash - infinite
  naturals.map(_ * 2)  // potentially infinite stream
 */
abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B >: A](element: B): MyStream[B] // prepend operator
  def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams

  def forEach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // takes the first n elements out of this stream
  def takeAsAList(n: Int): List[A] = take(n).toList()

  /*
    [1 2 3].toList([]) =
    [2 3].toList([1]) =
    [3].toList([2 1]) =
    [].toList([3 2 1]) =
    [1 2 3]
   */
  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
    if (isEmpty) acc.reverse
    else tail.toList(head :: acc)
}

object EmptyStream extends MyStream[Nothing] {
  override def isEmpty: Boolean = true
  override def head: Nothing = throw new NoSuchElementException
  override def tail: MyStream[Nothing] = throw new NoSuchElementException

  override def #::[B >: Nothing](element: B): MyStream[B] = new Cons[B](element, this)
  override def ++[B >: Nothing](anotherStream: MyStream[B]): MyStream[B] =
  // new Cons[B](anotherStream.head, anotherStream.tail[B])
    anotherStream

  override def forEach(f: Nothing => Unit): Unit = ()
  override def map[B](f: Nothing => B): MyStream[B] = this
  override def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
  override def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  override def take(n: Int): MyStream[Nothing] = this
}

  // The tail must be a type by name, since the stream has to be lazily evaluated
class Cons[+A](hd: A, tl: => MyStream[A]) extends MyStream[A] {
  override def isEmpty: Boolean = false
  override val head: A = hd
  override lazy val tail: MyStream[A] = tl   // call by need

    // val element = new Cons(1, EmptyStream)
    // val prepend = 1 #:: element = new Cons(1, element)
  override def #::[B >: A](element: B): MyStream[B] = new Cons(element, this)
  override def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] =
    new Cons(head, tail ++ anotherStream)

  override def forEach(f: A => Unit): Unit = {
    f(head)            // head(f)
    tail.forEach(f)
  }
  override def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f))  // preserves lazy evaluation
  override def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)  // preserves lazy evaluation
  override def filter(predicate: A => Boolean): MyStream[A] =
    if (predicate(head)) new Cons(head, tail.filter(predicate))
    else tail.filter(predicate)       // preserves lazy evaluation
  override def take(n: Int): MyStream[A] =
    if (n <= 0) EmptyStream
    else if (n == 1) new Cons(head, EmptyStream)
    else new Cons(head, tail.take(n-1))   // preserves lazy evaluation
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] = ???
}

object StreamsPlayground extends App {

}