package exercises

object FilesAndProcesses extends App {

  //  println(os.read.lines.stream(os.pwd))
  // println(os.walk.stream(os.pwd))

  os.read.lines.stream(os.pwd / ".gitignore").foreach(println)
  val filesWithoutTheDot = os.read.lines.stream(os.pwd / ".gitignore")
    .filter(_.startsWith("."))
    .map(_.drop(1))
    .toList
  println(filesWithoutTheDot.toString())

  // Another way to achieve the same results
  val filesWithoutTheDot_v2 = os.read.lines.stream(os.pwd / ".gitignore")
    .collect{case s".$str" => str}
    .toList
  println(filesWithoutTheDot_v2.toString)

  // SUBPROCESSES
  println("============ SUBPROCESSES ===============")
  println(os.proc("ls", "-l").call().out.lines())   // All the files in one line
  os.proc("ls", "-l").call().out.lines().foreach(println)  // One line per file

  // Interacting with a subprocess
  val sub = os.proc("python3", "-u", "-c", "while True: print(eval(input()))").spawn()
  sub.stdin.writeLine("1+2+3+4")
}