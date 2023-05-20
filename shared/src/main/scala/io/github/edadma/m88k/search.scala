package io.github.edadma.m88k

def search[T, E](seq: IndexedSeq[T], elem: E, lt: (E, T) => Boolean): Either[Int, Int] =
  def search(low: Int, high: Int): Either[Int, Int] =
    // If element not found
    if (low > high) Left(low) // return the insertion point
    else
      // Getting the middle element
      var middle = low + (high - low) / 2

      // If element found
      if (seq(middle) == elem) {
        return Right(middle)
        // If element is before middle element
      } else if (lt(elem, seq(middle)))
        // Searching in the left half
        search(low, middle - 1)
      else
        // Searching in the right half
        search(middle + 1, high)

  search(0, seq.length)
end search
