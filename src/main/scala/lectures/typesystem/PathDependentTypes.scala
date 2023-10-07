package lectures.typesystem

object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner): Unit = println(i)
    def printGeneral(i: Outer#Inner): Unit = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    2
  }

  // per-instance
  val o = new Outer
  val inner = new o.Inner   // o.Inner is a type, a path-dependent type

  val o2 = new Outer
  // val otherInner: o2.Inner = new o.Inner   // not valid

  o.print(inner)

  // Outer#Inner is a common supertype for all the Inner types.
  o.printGeneral(inner)
  o2.printGeneral(inner)
}
