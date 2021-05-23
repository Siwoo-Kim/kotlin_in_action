package com.siwoo.lesson2

import com.siwoo.lesson2.lastChar as last  //can import function and alias it

fun add(a: Int, b: Int): Int = a + b

fun sumOfPrimes(limit: Int): Long {
    val seq: Sequence<Long> = sequenceOf(2L) + generateSequence(3L) { it + 2 }
        .takeWhile { it < limit }
    
    seq.forEach { println(it) }
    fun isPrime(n: Long): Boolean = seq
        .takeWhile { it * it <= n }
        .all { n % it != 0L }
    
    return seq.filter(::isPrime).sum()
}

fun <T> List<T>.length() = this.size
fun List<Int>.product() = fold(1) { acc, e -> acc * e }
fun List<Int>.tripe(): List<Int> = this.map { it * 3 }


fun String.lastChar(): Char? = if (this.isEmpty()) null else this[length-1]

open class View {
    open fun click() = println("View clicked.")
}

class Button: View() {
    override fun click() = println("Button clicked.")
}

fun View.showOff() = println("View")

fun Button.showOff() = println("Button")    // no-override - no-poly

//extension property
val String.firstChar: Char?
    get() = if (!isEmpty()) this.get(0) else null
val String.lastChar: Char?
    get() = if (!isEmpty()) this.get(length-1) else null

var StringBuilder.lastChar: Char?
    get() = if (!isEmpty()) get(length-1) else null
    set(value: Char?) {
        if (value != null)
            this.setCharAt(length-1, value)
    }

fun <T> args(vararg values: T) {    // var args (multi args to array)
    println(listOf(values))
    println(listOf(*values))    //spread operator
}

data class Data(val num: Int) {
    // infix operator
    infix fun plus(data: Data): Data = Data(num + data.num)
}

fun main(args: Array<String>) {
    sumOfPrimes(100)
    println(listOf(1, 2, 3).length())
    println(listOf(1, 2, 3, 4, 5).product())
    println(listOf(1, 2, 3, 4, 5).tripe())
    println("test".last())
    
    println("test".firstChar)
    println("test".last)
    
    val view: View = Button()
    view.click()
    
    view.showOff()
    
    val sb = java.lang.StringBuilder("Kotlin?")
    sb.last = '!'
    println(sb)
    
    args(1, 2, 3, 4)
    
    val pair = (1 to 3).toList()
    println(pair)
    println(Data(1) plus Data(2))
    
    val (num, name) = 1 to "one"    //destructuring
    println("$num : $name")
    
    for ((index, e) in listOf(1, 2, 3).withIndex())
        println("$index - $e")
    mapOf(1 to 2, 3 to 4)
}