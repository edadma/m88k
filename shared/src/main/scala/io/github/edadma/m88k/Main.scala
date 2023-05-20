package io.github.edadma.m88k

@main def run(): Unit =
  val a = new Memory("Data address space", new RAM(10, 10), new RAM(20, 10))

  a.writeByte(25, 123)
  println(a.readByte(25))
