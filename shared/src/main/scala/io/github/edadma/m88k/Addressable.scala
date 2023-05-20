package io.github.edadma.m88k

import scala.collection.immutable.ArraySeq

trait Addressable:
  def base: Long
  def length: Long
  def readByte(addr: Long): Int
  def writeByte(addr: Long, data: Int): Unit

class Memory(blocks: Addressable*) extends Addressable:
  require(blocks.nonEmpty, "memory must contain at least one Addressable block")

  val mem = blocks.sortBy(_.base).to(ArraySeq)
  val base = mem.head.base
  val length = mem.last.base + mem.last.length - base

  def block(addr: Long): Option[Addressable] =
    search(mem, addr, _ < _.base, _ == _.base) match
      case Left(idx) if idx == 0 => None
      case Left(idx) =>
        val prec = mem(idx - 1)

        if addr < prec.base + prec.length then Some(prec) else None
      case Right(idx) => Some(mem(idx))

  private def badAddress: Nothing = sys.error("address not found")

  def readByte(addr: Long): Int = block(addr) getOrElse badAddress readByte addr

  def writeByte(addr: Long, data: Int): Unit = block(addr) getOrElse badAddress writeByte (addr, data)
