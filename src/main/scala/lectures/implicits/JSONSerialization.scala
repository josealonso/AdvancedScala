package lectures.implicits

import java.util.Date


object JSONSerialization extends App {

  // We want to serialize users, posts, feeds

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /*
    1.- intermediate data types: Int, String, List, Date
    2.- type classes for conversion to intermediate data types
    3.- serialize to JSON
   */

  sealed trait JSONValue {
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    override def stringify: String =
      "\"" + value + "\""
  }
  final case class JSONNumber(value: Int) extends JSONValue {
    override def stringify: String = value.toString
  }
  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    override def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
  }
  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    /*
       {
         name: "Alice"
         age: 25
         friends: [ ... ]
         latestPost: {
           content: "......"
           date: ...
         }
       }
     */

    override def stringify: String = values.map {
        case (key, value) => "\"" + key + "\":" + value.stringify
      }
      .mkString("{", ",", "}")

  }

  val data = JSONObject(Map(
    "user" -> JSONString("Alice"),
    "posts" -> JSONArray(List(
        JSONString("Scala"),
        JSONNumber(43)
    ))
  ))

  println(data.stringify)

  // type class
  /*
    1.- type class
    2- type class instances (implicit)
    3.- pimp library to use type class instances
   */

  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  // existing data types
  implicit object StringConverter extends JSONConverter[String] {
    override def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    override def convert(value: Int): JSONValue = JSONNumber(value)
  }

  // custom data types
  implicit object UserConverter extends JSONConverter[User] {
    override def convert(user: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(user.name),
      "age"  -> JSONNumber(user.age),
      "email" -> JSONString(user.email)
     ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    override def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "created" -> JSONString(post.createdAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    override def convert(feed: Feed): JSONValue = JSONObject(Map(
//      "user" -> UserConverter.convert(feed.user),  // too rigid
      "user" -> feed.user.toJSON,
//      "post" -> JSONArray(feed.posts.map(PostConverter.convert))  // too rigid
      "post" -> JSONArray(feed.posts.map(_.toJSON))
    ))
  }

  // 3.- Conversion
  implicit class JSONOps[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue =
      converter.convert(value)
  }

  val now = new Date((System.currentTimeMillis()))
  val alice = User("Alice", 25, "alice@email.com")
  val feed = Feed(alice, List(
    Post("first post", now),
    Post("second post", now)
  ))

  println(feed.toJSON.stringify)




}








