package com.siwoo.c3

import java.lang.IllegalArgumentException

/**
 * 순수 함수 (pure function)
 *  - domain & codomain relationship.
 *  - 외부 상태를 변이하면 안된다.
 *  - 예외나 오류를 던지지 않는다.
 *  - 같은 인자에 대해선 항상 같은 값을 반환.
 *
 */
class FunFunctions {
    var percent1 = 5
    private var percent2 = 9
    val percent3 = 13
    
    fun add(a: Int, b: Int): Int = a + b    //pure
    fun multi(a: Int, b: Int?): Int = 5 //pure
    fun div(a: Int, b: Int): Int = a / b
    fun div(a: Double, b: Double): Double = a / b //pure
    fun applyTax1(a: Int): Int = a / 100 * (100 + percent1)
    fun applyTax2(a: Int): Int = a / 100 * (100 + percent2)
    fun applyTax3(a: Int): Int = a / 100 * (100 + percent3) //pure
    fun append(i: Int, list: MutableList<Int>): List<Int> {
        list.add(i)
        return list
    }
    fun append2(i: Int, list: List<Int>) = list + i     //pure
}

fun double(x: Int): Int = x * 2
val doubleLambda: (Int) -> Int = { it * 2 } //함수
val doubleThenInc: (Int) -> Int = { doubleLambda(it) + 1 }
//val add: (Int, Int) -> Int = { a, b -> a + b }

val foo = Myclass()
val multiplyBy2: (Int) -> Int = foo::double

class Myclass {
    fun double(n: Int): Int = n * 2 
}

fun square(n: Int) = n * n
fun triple(n: Int) = n * 3

fun <T, U, R> compose(f: (U) -> R, g: (T) -> U): (T) -> R = { f(g(it)) }    //HOF


val add: (Int) -> (Int) -> Int = { x -> { y -> x + y} }
val multi: (Int) -> (Int) -> Int = { x -> { y -> x * y } }

val square: (Int) -> Int = { it * it }
val triple: (Int) -> Int = { it * 3 }

/**
 * 함수 조합.
 * 
 * 각 함수의 타입을 결정한다.
 *  (Int) -> Int
 *   이를 T 라고 하자.
 *   
 *   함수를 조합하기 위해 호출된 호출식을 생각해보자.
 *   f(g(x())) 
 *   
 *   f 은 인자가 T
 *   g 도 인자가 T
 *   x 은 Int 을 리턴
 *   
 *   (T) -> (T) -> T
 *   위를 치환한다.
 *   ((Int) -> Int) -> ((Int) -> Int) -> (Int) -> Int
 */
typealias T = (Int) -> Int

val compose: ((Int) -> Int) -> ((Int) -> Int) -> (Int) -> Int = { f -> { g -> { v -> f(g(v)) } } }

fun <T, U, R> highCompose(): ((U) -> R) -> ((T) -> U) -> (T) -> R = { f -> { g -> { v -> f(g(v)) }}}
fun <T, U, R> highAndThen(): ((T) -> U) -> ((U) -> R) -> (T) -> R = { f -> { g -> { v -> g(f(v)) }}}

fun <T, U, R> partialA(e: T, f: (T) -> (U) -> R): (U) -> R = f(e)

fun <T, U, R> partialB(u: U, f: (T) -> (U) -> R): (T) -> R = { t: T -> f(t)(u)  }

fun <A, B, C, D> func(a: A, b: B, c: C, d: D): String = "$a $b $c $d"

fun <A, B, C, D> concat(): (A) -> (B) -> (C) -> (D) -> String = { a: A -> { b: B -> { c: C -> {d: D -> "$a $b $c $d" } } } }

fun <A, B, C> curry(f: (A, B) -> C): (A) -> (B) -> C = { a -> { b -> f(a, b) } } 

fun <A, B, C> swap(f: (A) -> (B) -> C): (B) -> (A) -> C = { b -> { a -> f(a)(b) } }

data class Product(val name: String, val price: Price, val weight: Weight)

data class Price(val value: Double) {
    operator fun plus(price: Price) = Price(this.value + price.value)
    operator fun times(num: Int) = Price(this.value * num)
    
    companion object {
        val identity = Price(0.0)   // 항등원
        operator fun invoke(value: Double) = if (value > 0) Price(value) else throw IllegalArgumentException("Price must be positive or null")
    }
}

data class Weight(val value: Double) {
    operator fun plus(weight: Weight) = Weight(this.value + weight.value)
    operator fun times(num: Int) = Weight(this.value * num)
    
    companion object {
        val identity = Weight(0.0)   // 항등원
        operator fun invoke(value: Double) = if (value > 0) Weight(value) else throw IllegalArgumentException("Price must be positive or null")
    }
}

data class OrderLine(val product: Product, val count: Int) {
    fun weight() = product.weight * count
    fun amount() = product.price * count
}

object Store {
    @JvmStatic
    fun main(args: Array<String>) {
        /**
         * fold - 시작 원소를 제공, 결과 타입이 컬렌션의 원소랑 다름
         * reduce - 시작 원소를 제공하지 않음, 결과 타입이 원소랑 같음.
         */
        
        val toothPath = Product("Tooth paste", Price(1.5), Weight(0.5))
        val toothBrush = Product("Tooth brush", Price(3.5), Weight(0.3))
        val orderLines = listOf(
            OrderLine(toothPath, 2), 
            OrderLine(toothBrush, 3))
        val weight = orderLines.fold(Weight.identity) { acc, b -> acc + b.weight() }
        val price = orderLines.fold(Price.identity) { acc, b -> acc + b.amount() }
        println("Total price $price")
        println("Total weight $weight")
    }
}

fun main(args: Array<String>) {
    println(square(triple(2))) // f*g 은 g 가 먼저 평가된다.
    println(compose(::square, ::triple)(2))
    println(add(2)(3))
    
    val squareOfTripe = compose(square)(triple)     //triple 이 먼저 적용된다. f*g 에서 g 가 먼저 평가된다.
    println(squareOfTripe(2))
    
    val squareOfTripe2 = highCompose<Int, Int, Int>()(square)(triple)
    println(squareOfTripe2(2))
    val tripleOfSquare = highAndThen<Int, Int, Int>()(square)(triple)   // square 이 먼저 적용된다.
    println(tripleOfSquare(2))
    
                                    //anonymous function
    val cosValue: Double = compose({x -> Math.PI / 2 - x}, Math::sin)(2.0)
    println(cosValue)
    
    val taxRate = 0.09
    fun addTax(price: Double) = price + price * taxRate //closure - 자신을 둘러싼 영역 요소에 의해 값이 결정
    
    val addTax2 = {
        rate: Double -> {
            price: Double -> {
                price + price * taxRate
            }
        }
    }
    println(addTax2(taxRate)(12.0))
    println(concat<Int, Int, String, Char>()(1)(2)("3")('4'))
}