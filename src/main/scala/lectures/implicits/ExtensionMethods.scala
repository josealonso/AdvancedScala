package lectures.implicits

object ExtensionMethods extends App {

  // Scala 3 extension methods are equivalent to Scala 2 implicit classes
  extension (value: Int) {
     def isEven: Boolean = value % 2 == 0
     def sqrt: Double = Math.sqrt(value)
  }

  // generic extensions
  extension [A](list: List[A]) {
    def ends: (A, A) = (list.head, list.last)
    def extremes(using ordering: Ordering[A]): (A, A) = list.sorted.ends // an extension method can be called here
  }
}

/*
  Extension methods do not replace type classes for all use cases.
  Type classes allow the use of a higher level of abstraction than that is possible with extension methods alone.
  With type classes, we can do this, which is not possible with extensions:
  def sendPost[A: JSONConverter](payload: A): JSONValue = payload.toJSON
 */