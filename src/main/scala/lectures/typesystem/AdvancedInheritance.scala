package lectures.typesystem

object AdvancedInheritance extends App {

  // 1.- convenience
  trait Writer[T] {
    def write(value: T): Unit
  }

  trait Closeable {
    def close(status: Int): Unit
  }

  trait GenericStream[T] {
    def forEach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
    stream.forEach(println)
    stream.close(0)
    // ............
  }

  // 2.- diamond problem --> In Scala, last override gets picked

  // 3.- In the context of "type linearization",
  // the "super" keyword refers to the type immediately to the left.

  trait Color
  trait Red extends Color
  // Red = AnyRef with <Color>




}
