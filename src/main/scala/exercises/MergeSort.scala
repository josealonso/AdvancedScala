package exercises

object MergeSort extends App {

  def mergeSort(items: Array[Int]): Array[Int] = {
    if (items.length <= 1) items
    else {
      val (left, right) = items.splitAt(items.length / 2)
      val (sortedLeft, sortedRight) = (mergeSort(left), mergeSort(right))
      // maintain indices into the left and right arrays
      var (leftIdx, rightIdx) = (0, 0)
      val finalArray = Array.newBuilder[Int]
      while (leftIdx < sortedLeft.length || rightIdx < sortedRight.length) {
        val takeLeft = (leftIdx < sortedLeft.length, rightIdx < sortedRight.length) match {
          case (true, false) => true
          case (false, true) => false
          case (true, true) => sortedLeft(leftIdx) < sortedRight(rightIdx)
//          case (false, false) => false
        }
        if (takeLeft) {
          finalArray += sortedLeft(leftIdx)
          leftIdx += 1
        } else {
          finalArray += sortedRight(rightIdx)
          rightIdx += 1
        }
      }
      finalArray.result()
    }
  }

  import math.Ordering.Implicits.infixOrderingOps
  def genericMergeSort[T: Ordering](items: IndexedSeq[T]): IndexedSeq[T] = {
    if (items.length <= 1) items
    else {
      val (left, right) = items.splitAt(items.length / 2)
      val (sortedLeft, sortedRight) = (genericMergeSort(left), genericMergeSort(right))
      // maintain indices into the left and right arrays
      var (leftIdx, rightIdx) = (0, 0)
      val finalArray = IndexedSeq.newBuilder[T]
      while (leftIdx < sortedLeft.length || rightIdx < sortedRight.length) {
        val takeLeft = (leftIdx < sortedLeft.length, rightIdx < sortedRight.length) match {
          case (true, false) => true
          case (false, true) => false
          case (true, true) => sortedLeft(leftIdx) < sortedRight(rightIdx)  // these two lines are equivalent
//          case (true, true) => Ordering[T].lt(sortedLeft(leftIdx), sortedRight(rightIdx))
        }
        if (takeLeft) {
          finalArray += sortedLeft(leftIdx)
          leftIdx += 1
        } else {
          finalArray += sortedRight(rightIdx)
          rightIdx += 1
        }
      }
      finalArray.result()
    }
  }

  println(mergeSort(Array(4, 0, 1, 5, 2, 3)).mkString("Array(", ", ", ")"))

  println(genericMergeSort(Vector(4, 0, 1, 5, 2, 3)).mkString("Array(", ", ", ")"))
  println(genericMergeSort(Vector("Bob", "Alice", "Daniel", "Ces")).mkString("Array(", ", ", ")"))
}






