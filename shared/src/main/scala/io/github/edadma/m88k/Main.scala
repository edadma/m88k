package io.github.edadma.m88k

@main def run(): Unit =
  println(search(IndexedSeq(3, 5), 5, _ < _))
  println(search(IndexedSeq(C(3), C(5)), 5, (x, y) => x < y.a))

case class C(a: Int)
