package lectures.concurrency

object ThreadCommunication extends App {

  /*
    The producer-consumer problem

    producer -> [ x ] -> consumer
   */
  class SimpleContainer {
    private var value = 0

    def isEmpty: Boolean = value == 0
    def set(newValue: Int): Unit = value = newValue
    def get(): Int = {
      val result = value
      value = 0
      result
    }
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting....")
      while (container.isEmpty) {
        println("[consumer] actively waiting....")
      }

      println("[consumer] I have consumed " + container.get())
    })

    val producer = new Thread(() => {
      println("[producer] computing....")
      Thread.sleep(400 )
      val value = 4
      container.set(value)
      println("[producer] I have produced the value " + value)
    })

    // This test assumes the consumer starts first and waits for the producer
    consumer.start()
    producer.start()
  }

//  naiveProdCons()
  smartProdCons()

  // wait and notify (only work in synchronized expressions)
  def smartProdCons() = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting....")
      container.synchronized {
        container.wait()
      }

      // container must have some value
      println("[consumer] I have consumed " + container.get())
    })

    val producer = new Thread(() => {
      println("[producer] computing....")
      Thread.sleep(600)
      val value = 4

      container.synchronized {
        println("[producer] I have produced the value " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }
}

/*
  Synchronized --> only AnyRefs can have synchronized blocks.
  General principles:
  - make no assumptions about who gets the lock first.
  - keep locking to a minimum
  - maintain thread safety at all times
 */
