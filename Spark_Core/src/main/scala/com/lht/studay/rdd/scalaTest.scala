package com.lht.studay.rdd

/**
 * scala语法练习
 */

/*// trait练习  类似java中的接口，不能被实例化。
trait Iterator[A] {
  def hasNext: Boolean

  def next(): A
}

class IntItertor(to: Int) extends Iterator[Int] {
  private var current = 0

  override def hasNext: Boolean = current < to

  override def next(): Int = {
    if (hasNext) {
      val t = current
      current += 1
      t
    } else 0
  }
}*/
/*//泛型类
class scalaTest[A] {
  private var elements: List[A] = Nil

  def push(x: A) = elements = x :: elements

  def peek: A = elements.head

  def pop(): A = {
    val currentTop = peek
    elements = elements.tail
    currentTop
  }
}*/
/*// 协变:cat是animal的子类，List[cat]也是List[animal]的子类。
abstract class Animal {
  def name: String
}

case class Cat(name: String) extends Animal

case class Dog(name: String) extends Animal*/
/*// 逆变:泛型类Printer的参数为-A,如果A是B的子类，那么Printer[B]是Printer[A]的子类。
abstract class Animal {
  def name: String
}

case class Cat(name: String) extends Animal

abstract class Printer[-A] {
  def print(value: A): Unit
}
class AnimalPriter extends Printer[Animal]{
  override def print(animal: Animal): Unit = {
    println("name is:" + animal.name)
  }
}*/
/*// 类型上界: T<:A  参数变量T应该是类型A的子类。
abstract class Animal {
  def name: String
}

abstract class Pet extends Animal {}

class Cat extends Pet {
  override def name: String = "Cat"
}

class Dog extends Pet {
  override def name: String = "Dog"
}

class Lion extends Animal {
  override def name: String = "Lion"
}

// 类型上界，P只能是Pet的子类。
class PetContainer[P <: Pet](p: P) {
  def pet: P = p
}*/
/*// 抽象类
trait Buffer {
  type T
  val element: T
}

abstract class SeqBuffer extends Buffer {
  type U
  type T <: Seq[U]

  def length = element.length
}

abstract class IntBuffer extends SeqBuffer {
  type U = Int
}*/


object scalaTest extends App {
  /*// trait练习
  var iterator = new IntItertor(10)
  print(iterator.next())*/
  /*// 高阶函数:函数可以作为方法的参数
  // 1. 定义一个函数：将传入的参数进行*2操作   val 函数名 = (参数列表) => 函数体
  val f = (x: Int) => x * 2
  var seq = Seq(1, 2, 3, 4)
  seq.map(f)

  // 2.方法强制转换成函数
  def func(x: Int): Int = x * 2

  seq.map(func)

  // 3.函数作为函数的参数
  def toDoubles(x: List[Int], fun: Int => Double): List[Double] = x.map(fun)
  val fun1 = (x: Int) => x.toDouble
  toDoubles(List(1, 2, 3, 4), fun1)*/
  /*// 泛型类
  val test = new scalaTest[Int]
  test.push(1)
  println(test.peek)*/
  /*// 协变练习
  def printAnimalNames(animal: List[Animal]): Unit = {
    val f = (animal: Animal) => println(animal.name)
    animal.foreach(f)
  }

  def printAnimalName(animal: Animal): Unit = {
    print(animal.name)
  }

  val cats: List[Cat] = List(Cat("cat_tom"), Cat("cat_jim"))
  val dogs: List[Dog] = List(Dog("dog_tom"), Dog("dog_jim"))
  printAnimalNames(cats)
  printAnimalNames(dogs)*/
  /*//逆变
  val cat = new Cat("tom")

  def printMyCat(printer: Printer[Cat]): Unit = {
    printer.print(cat)
  }*/
  /*// 类型上界 PetContainer的参数类型只能是Pet的子类，如果不是Pet的子类，那么编译不通过，下面就是编译失败，因为Lion不是Pet的子类
  val lionContainer = new PetContainer[Lion](new Lion)
  val catContainer = new PetContainer[Cat](new Cat)*/
  /*// 抽象类
  def newIntSeqBuffer(x: Int, y: Int): IntBuffer = {
    new IntBuffer {
      type T = List[U]
      val element = List(x, y)
    }
  }
  val buf = newIntSeqBuffer(7,8)
  print(buf.length)
  print(buf.element)*/



}
