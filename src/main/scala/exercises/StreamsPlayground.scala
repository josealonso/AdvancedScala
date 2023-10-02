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
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] // concatenate two streams

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
  override def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] =
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
  override def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] =  // call by need
    new Cons(head, tail ++ anotherStream)

  override def forEach(f: A => Unit): Unit = {
    f(head)            // head(f)
    tail.forEach(f)
  }
  override def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f))  // preserves lazy evaluation
  override def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)  // The error was in flatMap
  override def filter(predicate: A => Boolean): MyStream[A] =
    if (predicate(head)) new Cons(head, tail.filter(predicate))
    else tail.filter(predicate)       // preserves lazy evaluation
  override def take(n: Int): MyStream[A] =
    if (n <= 0) EmptyStream
    else if (n == 1) new Cons(head, EmptyStream)
    else new Cons(head, tail.take(n-1))   // preserves lazy evaluation
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] =
    new Cons(start, MyStream.from(generator(start))(generator))
}

object StreamsPlayground extends App {
  val naturals = MyStream.from(1)(_ + 1)
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)

  val startFrom0 = 0 #:: naturals
  println(startFrom0.head)

  startFrom0.take(10000).forEach(println)

  // map, flatMap
  println(startFrom0.map(_ * 2).take(100).toList())
  println(startFrom0.flatMap(x => new Cons(x, new Cons(x + 1, EmptyStream))).take(10).toList())
  println(startFrom0.filter(_ < 10).take(10).toList())
  println(startFrom0.filter(_ < 10).take(10).take(20).toList())
//  println(startFrom0.filter(_ < 10).take(11))     // Error

  // Exercises on streams
  // 1.- stream of Fibonacci numbers
  def fibonacci(first: BigInt, second: BigInt): MyStream[BigInt] = {
    new Cons(first, fibonacci(second, first + second))
  }

  println(fibonacci(1, 1).take(100).toList())

  // 2.- stream of prime numbers with Eratosthenes' sieve
  /*
    [2 3 4 ....]
    filter out all numbers divisible by 2
    [2 3 5 7 9 11 ....]
    filter out all numbers divisible by 3
    [2 3 5 7 11 13 ....]
    filter out all numbers divisible by 5
   */
  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
  if (numbers.isEmpty) numbers
  else new Cons(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0)))

  // Print the first hundred prime numbers
  println(eratosthenes(MyStream.from(2)(_ + 1)).take(100).toList())
}



















