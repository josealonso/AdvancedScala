package lectures.concurrency

import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success}
import scala.concurrent.duration.*

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

  Thread.sleep(2000)

  // mini social network

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) =
      println(s"${this.name} poking ${anotherProfile.name}")
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1" -> "Mark",
      "fb.id.2" -> "Bill",
      "fb.id.0" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1" -> "fb.id.2"
    )

    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }
  }
    // client: Mark to poke Bill
  val mark = SocialNetwork.fetchProfile("fb.id.1")
//  mark.onComplete {
//    case Success(markProfile) => {
//      val bill = SocialNetwork.fetchBestFriend(markProfile)
//      bill.onComplete {
//        case Success(billProfile) => markProfile.poke(billProfile)
//        case Failure(exception) => exception.printStackTrace()
//      }
//    }
//    case Failure(exception) => exception.printStackTrace()
//  }

  // functional composition of futures
  // map, flatMap, filter
  val nameOnTheWall = mark.map(profile => profile.name)
  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

  // for-comprehensions
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  // fallbacks

  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover {
    case exception: Throwable => Profile("fb.id.0", "All alone")
  }

  // fetch a default profile
  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith {
    case exception: Throwable => SocialNetwork.fetchProfile("fb.id.0")
  }

  val fallbackResult = SocialNetwork.fetchProfile("unknown id").fallbackTo(
    SocialNetwork.fetchProfile("fb.id.0"))

  // online banking app
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "My Bank"

    def fetchUser(name: String): Future[User] = Future {
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "SUCCESS")
    }

    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      // fetch the user from the DB
      // create a transaction
      // WAIT for the transaction to finish
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      Await.result(transactionStatusFuture, 2.seconds)  // implicit conversions -> pimp my library
    }
  }

  println(BankingApp.purchase("José", "phone", "My store", 3000))

  // promises
  val promise = Promise[Int]()  // "controller" over a future
  val future = promise.future

  /*
     The future-promise pattern separates the concern of handling futures and
     of writing to a promise, eliminating concurrency issues almost completely.
     As it gives you almost complete control over where and how you want to set a value to a future.
   */
  // thread1 - consumer
  future.onComplete {
    case Success(r) => println("[consumer] I have received " + r)
  }

  // thread1 - producer
  val producer = new Thread(() => {
    println("[producer] crunching numbers...")
    Thread.sleep(500)
    // fulfilling the promise
    promise.success(23)
    println("[producer] done")
  })

  producer.start()
  Thread.sleep(1000)
}


















