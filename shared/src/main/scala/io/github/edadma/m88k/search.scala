package io.github.edadma.m88k

import math.Ordering.Implicits.infixOrderingOps

def search[E: Ordering](arr: IndexedSeq[E], elem: E): Either[Int, Int] =
  def search[E: Ordering](arr: IndexedSeq[E], elem: E, low: Int, high: Int): Either[Int, Int] =
    // If element not found
    if (low > high) Left(low) // return the insertion point
    else
      // Getting the middle element
      var middle = low + (high - low) / 2

      // If element found
      if (arr(middle) == elem) {
        return Right(middle)
        // If element is before middle element
      } else if (elem < arr(middle))
        // Searching in the left half
        search(arr, elem, low, middle - 1)
      else
        // Searching in the right half
        search(arr, elem, middle + 1, high)

  search(arr, elem, 0, arr.length)
end search
