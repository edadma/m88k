package io.github.edadma.m88k

trait Addressable:
  def base: Long
  def length: Int
  def readByte(addr: Long): Int
  def writeByte(addr: Long, data: Int): Unit

//class Memory extends Addressable
