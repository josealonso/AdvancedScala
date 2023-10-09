package lectures.typesystem

object HigherKindedTypes extends App {

  trait AHigherKindedType[F[_]]

  // Generic monad
  trait Monad[F[_], A] {
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  def multiply[F[_], A, B](ma: Monad[F, A], mb: Monad[F, B]): F[(A, B)] = {
    for {
      a <- ma
      b <- mb
    } yield (a, b)
    /*
    ma.flatMap(a => mb.map(b => (a,b)))
    */
  }

  class MonadList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }

  val monadList = new MonadList(List(1,2,3))
  monadList.flatMap(x => List(x, x + 1))
  monadList.map(_ * 2)

  println(multiply(new MonadList(List(1,2)), new MonadList(List("a", "b"))))
}










