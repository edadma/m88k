package io.github.edadma.m88k

import scala.collection.immutable.ArraySeq

trait Addressable:
  def base: Long
  def length: Int
  def readByte(addr: Long): Int
  def writeByte(addr: Long, data: Int): Unit

//class Memory(blocks: Addressable*) extends Addressable:
//  val mem = blocks.sorted.to(ArraySeq)
//
//  def block(addr: Long): Option[Addressable] =
//    search(mem, addr)
