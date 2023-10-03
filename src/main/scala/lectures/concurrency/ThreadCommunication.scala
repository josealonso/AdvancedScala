package lectures.concurrency

import scala.collection.mutable
import scala.util.Random

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
//  smartProdCons()

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

  def prodConsBuffer(): Unit = {
    val BUFFER_CAPACITY = 3
    val buffer = new mutable.Queue[Int]
    val capacity = BUFFER_CAPACITY

    val consumer = new Thread(() => {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
          }

          // there must be at least one value in the buffer
          val value = buffer.dequeue()
          println("[consumer] I have consumed " + value)

          buffer.notify()
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least one empty space in the buffer
          println("[producer] I have produced " + i)
          buffer.enqueue(i)

          buffer.notify()
          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

  prodConsBuffer()
}

/*
  Synchronized --> only AnyRefs can have synchronized blocks.
  General principles:
  - make no assumptions about who gets the lock first.
  - keep locking to a minimum
  - maintain thread safety at all times
 */
