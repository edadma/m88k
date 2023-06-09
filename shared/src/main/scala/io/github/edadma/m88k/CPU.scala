package io.github.edadma.m88k

import scala.collection.immutable
import scala.collection.mutable.ListBuffer

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

object CPU:
  private def generate(pattern: String, width: Int) =
    case class Variable(v: Char, lower: Int, upper: Int, bits: List[Int])

    val Range = "([a-zA-Z]):([0-9]+)-([0-9]+)" r
    val p = pattern replace (" ", "") split ";"

    require(p.nonEmpty, "empty pattern")

    val bits =
      require(bits.length > 0, "pattern should comprise at least one bit")
    require(
      bits.forall(c => c == '0' || c == '1' || c.isLetter || c == '-'),
      "pattern should comprise only 0's, 1's, letters or -'s",
    )

    val ranges = Map((p drop 1 map { case Range(v, l, u) => v(0) -> (l.toInt, u.toInt) }): _*)

    require(
      ranges forall { case (_, (l, u)) => 0 <= l && l <= u },
      "first value of range must be less than or equal to second and be non-negative",
    )

    val (constant, variables) = {
      def scan(acc: Int, pos: Int, chars: List[Char], vars: Map[Char, List[Int]]): (Int, Map[Char, List[Int]]) =
        chars match {
          case Nil                       => (acc, vars)
          case '0' :: t                  => scan(acc, pos << 1, t, vars)
          case '1' :: t                  => scan(acc | pos, pos << 1, t, vars)
          case v :: t if vars contains v => scan(acc, pos << 1, t, vars + (v -> (vars(v) :+ pos)))
          case v :: t                    => scan(acc, pos << 1, t, vars + (v -> List(pos)))
        }

      scan(0, 1, bits.reverse.toList, Map())
    }

    val enumeration = new ListBuffer[(Int, Map[Char, Int])]

    def enumerate(acc: Int, vars: List[Variable], vals: Map[Char, Int]): Unit =
      vars match {
        case Nil => enumeration += ((acc, vals))
        case v :: t =>
          for (i <- v.lower to v.upper)
            enumerate(acc | int2bits(0, i, v.bits), t, vals + (v.v -> i))
      }

    def int2bits(res: Int, n: Int, bits: List[Int]): Int =
      bits match {
        case Nil                   => res
        case b :: t if (n & 1) > 0 => int2bits(res | b, n >> 1, t)
        case b :: t                => int2bits(res, n >> 1, t)
      }

    enumerate(
      constant,
      variables.toList map { case (v, b) =>
        if (ranges contains v) {
          require(ranges(v)._2 < (1 << b.length), "second value of range must be less than 2^#bits")
          Variable(v, ranges(v)._1, ranges(v)._2, b)
        } else
          Variable(v, 0, (1 << b.length) - 1, b)
      },
      Map(),
    )
    enumeration.toList
