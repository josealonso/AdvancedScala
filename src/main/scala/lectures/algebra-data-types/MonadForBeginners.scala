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
}