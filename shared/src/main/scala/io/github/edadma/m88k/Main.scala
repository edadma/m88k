package io.github.edadma.m88k

@main def run(): Unit =
  println(search(IndexedSeq(3, 5), 4, _ < _, _ == _))
  println(search(IndexedSeq(C(3), C(5)), 4, _ < _.a, _ == _.a))

case class C(a: Int)
