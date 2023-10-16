package lectures.algebra-data-types

object MonadForBeginners extends App {

    case class SafeValue[+T](private val internalValue: T) {  // "constructor" = pure or unit
        def get: T = synchronized {
            // does sth.interesting
            internalValue
        }

        def flatMap[S](transformer: T => SafeValue[S]): SafeValue[S] = synchronized {  // bind or flatMap
            transformer(internalValue)
        }
    }

    // "external" API
    def gimmeSafeValue[T](value: T): SafeValue[T] = SafeValue(value)

    val safeString: SafeValue[String] = gimmeSafeValue("Scala")
    // extract
    val string = safeString.get
    // transform
    val upperString = string.toUpperCase()
    // wrap
    val upperSafeString = SafeValue(upperString)
    // ETW (Extract, Transform, Wrap) pattern

    // compressed:
    val upperSafeString2 = safeString.flatMap(s => SafeValue(s.toUpperCase()))

    // Examples

    // Example 1: census
    case class Person(firstName: String, lastName: String) {
        assert(firstName != null && lastName != null)
    }

    // census API
    def getPersonBetter(firstName: String, lastName: String): Option[Person] = 
        Option(firstName).flatMap { fName => 
            Option(lastName).flatMap { lName => 
                Option(Person(fName, lName))
            }
        }

    def getPersonFor(firstName: String, lastName: String): Option[Person] = for {
        fName <- Option(firstName)
        lName <- Option(lastName)
    } yield Person(fName, lName)

    // Example 2: asynchronous fetches

    case class User(id: String)
    case class Product(sku: String, price: Double)

    def getUser(url: String): Future[User] = Future {
        User("daniel")
    }

    def getLastOrder(userId: String): Future[Product] = Future {
        Product("123-456", 99.98)  // sample
    }

    val danielsUrl = "my.store.com/users/daniel"

    val vatInclPrice: Future[Double] = getUser(danielsUrl)
        .flatMap(user => getLastOrder(user.id))
        .map(_.price * 1.21)

    val vatInclPriceFor: Future[Double] = for {
        user <- getUser(danielsUrl)
        product <- getLastOrder(user.id)
    } yield product.price * 1.21

    // Example 3: double-for loops

    val numbers = List(1, 2, 3)
    val chars = List('a', 'b', 'c')
    val checkerboard: List[(Int, Char)] = numbers.flatMap(chars.map(char => (number, char)))
    val checkerboard2 = for {
        number <- umbers
        char <- chars
    } yield (number, char)

       
    // The monad properties

    // Property 1
    def twoConsecutive(x: Int) = List(x, x + 1)
    twoConsecutive(3)
    List(3).flatMap(twoConsecutive)  // List(3,4)
    // Monad(x).flatMap(f) == f(x)

    // Property 2
    List(1,2,3).flatMap(x => List(x))  // List(1,2,3)
    // Monad(v).flatMap((x) => Monad(x)) returns Monad(v)

    // Property 3 - Associativity, because monads are sequential computations
    val numbers = List(1, 2, 3)
    val incrementer = (x: Int) => List(x, x + 1)
    val doubler = (x: Int) => List(x, x * 2)
    numbers.flatMap(incrementer).flatMap(doubler) == numbers.flatMap(x => incrementer(x).flatMap(doubler))
    // List(1, 2, 2, 4    2, 4, 3, 6       3, 6, 4, 8)
    /*
      List(
        incrementer(1).flatMap(doubler) --- 1,2,2,4
        incrementer(2).flatMap(doubler) --- 2,4,3,6
        incrementer(3).flatMap(doubler) --- 3,6,4,8
      )

      Monad(v).flatMap(f).flatMap(g) == Monad(v).flatMap(x => f(x).flatMap(g))
    */
}













