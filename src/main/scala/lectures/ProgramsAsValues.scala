package lectures

object ProgramsAsValues extends App {
  

// Imperative style, it is not compositional
var i = 0
while(i < 10) {
 Thread.sleep(1000)
 println("hello")
 i = i + 1
}

// Functional approach, using fs2.Stream

val p = IO.println("hello")
Stream.repeatEval(p)
      .take(10)
      .metered(1.second)

/*
This code is highly compositional, it is made of smaller parts, which all make sense as individual programs:
    - IO.println("hello") is the program that prints “hello”.
    - repeatEval is the program that executes another program indefinitely.
    - take(n) is the program that evaluates the first n iterations of another program.
    - metered is the program that executes another program at the given rate.

CONCLUSION:Programs as values is about removing barriers to compositionality.

*/

/*
Execution as evaluation

We can now describe the effect model used by the vast majority of languages (Java, Go, JS, C++, Python etc), starting from their evaluation strategy, 
which I will informally define as the order in which expressions are reduced.
These languages are based on strict evaluation (or call by value), in which expressions are evaluated as soon as they are bound to a name, 
and the arguments of a function are evaluated before the function is applied, which results in a natural sequential order.
For this reason, these languages treat execution as evaluation: effects are executed during the process of evaluating 
the expression in which they are defined, and if we ever want to prevent execution, we need to suspend evaluation. 
Using the language we have developed earlier, we can say that execution as evaluation is doing-first, with explicit being.
In Scala, the two aspects correspond to different language features, with val and by-value parameters (: A) used for doing, 
and def and by-name parameters (: => A) used for being.

Programs as values goes in the exact opposite direction of execution as evaluation. Its key principle is: being first, with explicit doing.
Because of this emphasis on being, programs written in this paradigm are composed entirely of values, i.e. referentially transparent expressions. 
And what’s the most straightforward kind of value? Well, just datatypes: effectful programs are represented simply as datatypes which describe 
the intention of executing a specific effect.
When everything, including effectful programs, is a value, compositional being APIs become easy, just pass datatypes around, combine them with functions, 
put them in data structures, and so on, without worrying about when evaluation happens.
But the question is, how do we represent doing? The idea is that instead of relying on evaluation, we design 
combinator functions that describe the intention of running a program after another, i.e. we represent doing explicitly through a being api.
In the end, our whole program will be represented by a single expression, which builds an instance of our effect datatype. 
This instance can then be translated into actual computational effects, once, at the so called “end of the world”:
in Scala this means the main function, whereas Haskell goes one step further by doing the translation in the runtime system, 
so that the programmer doesn’t even see it.

This snippet of pseudo code should give you an idea of how things look like in a programs as values codebase:
*/

// the type of our program
type Console

// constructs Console values
def print(s: String): Console

// simply returns a value of type Console, does not do anything
val hey: Console = print("hey ")

// nothing gets printed, but we return another value of type Console
// that explicitly describes printing one word after another
val p: Console = hey.andThenDo(hey).andThenDo(print("you"))

// Everything is RT, so p is equivalent to
val p: Console = 
  print("hey ")
   .andThenDo(print("hey "))
   .andThenDo(print("you"))

object Main {
  def main(args: Array[String]): Unit =
    p.translateToActualEffects
}

// CONCLUSION: we represent effects as datatypes (i.e. programs as values), and we use functions to assemble big programs out of smaller ones. 
// The rest comes down to discovering richer datatypes for our effects, and exploring the structure of the functions we use to combine them. 
// And that is exactly what we are going to do next.

}

