package io.github.edadma.m88k

import scala.collection.immutable

class CPU(codesuper: Addressable, codeuser: Addressable, datasuper: Addressable, datauser: Addressable):

  val r = immutable.ArraySeq(
    new Reg0,
    new Reg,
    new Reg,
    new Reg,
    new Reg,
    new Reg,
    new Reg,
    new Reg,
    new Reg,
    new Reg,
    new Reg,
    new Reg,
    new Reg,
    new Reg,
    new Reg,
    new Reg,
  )
  var PSR: Int = 0

class Reg:
  private var r: Int = 0

  def value: Int = r

  def value_=(v: Int): Unit = r = v

class Reg0 extends Reg:
  override def value: Int = 0

  override def value_=(v: Int): Unit = {}

object CPU
