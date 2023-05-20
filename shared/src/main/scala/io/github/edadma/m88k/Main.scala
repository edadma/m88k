package io.github.edadma.m88k

@main def run(): Unit =
  val a = RAM(0, 10)

  a.writeByte(5, 123)
  println(a.readByte(5))
