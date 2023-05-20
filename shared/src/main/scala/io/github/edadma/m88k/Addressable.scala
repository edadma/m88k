package io.github.edadma.m88k

import scala.collection.{immutable, mutable}

trait Addressable:
  def base: Long
  def length: Long
  def readByte(addr: Long): Int
  def writeByte(addr: Long, data: Byte): Unit

class Memory(blocks: Addressable*) extends Addressable:
  require(blocks.nonEmpty, "memory must contain at least one Addressable block")

  val mem = blocks.sortBy(_.base).to(immutable.ArraySeq)
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

  def writeByte(addr: Long, data: Byte): Unit = block(addr) getOrElse badAddress writeByte (addr, data)

class IndexedSeqAddressable(seq: mutable.IndexedSeq[Byte], val base: Long) extends Addressable:
  require(base >= 0, "base is negative")
  require(seq.nonEmpty, "Addressable is empty")

  val length = seq.length

  def readByte(addr: Long): Int =
    require(base <= addr && addr < base + length, "address out of range")
    seq((addr - base).toInt) & 0xff

  def writeByte(addr: Long, data: Byte): Unit =
    require(base <= addr && addr < base + length, "address out of range")
    seq((addr - base).toInt) = data

def RAM(base: Long, length: Long): Addressable =
  require(0 < length && length <= Int.MaxValue, "length out of range")
  new IndexedSeqAddressable(mutable.ArraySeq.fill(length.toInt)(0.asInstanceOf[Byte]), base)
