package com.siwoo.c1

import java.io.BufferedReader
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.util.*

enum class Color(val r: Int, val g: Int, val b: Int) {
    RED(2550, 0, 0), 
    ORANGE(255, 265, 0), 
    YELLOW(0, 255, 0), 
    GREEN(0, 255, 0), 
    BLUE(0, 0, 255), 
    INDIGO(75, 0, 130), 
    VIOLET(238, 130, 238);
    
    fun rgb() = (r * 256 + g) * 256 + b
    
    companion object {
        fun getMnemonic(color: Color): String = 
            when (color) {
                RED -> "Richard"
                ORANGE -> "Of"
                YELLOW -> "York"
                GREEN -> "Gave"
                BLUE -> "Battle"
                INDIGO -> "In"
                VIOLET -> "Vain"
            }
        
        fun getWarmth(color: Color) = 
            when(color) {
                RED, ORANGE, YELLOW -> "warm"
                GREEN -> "neutral"
                BLUE, INDIGO, VIOLET -> "cold"
            }
        
        fun mix(c1: Color, c2: Color) = 
            when (setOf(c1, c2)) {
                setOf(RED, YELLOW) -> ORANGE
                setOf(YELLOW, BLUE) -> GREEN
                setOf(BLUE, VIOLET) -> INDIGO
                else -> throw IllegalArgumentException("Dirty color")
            }
    }
}

interface Expr {
    
    companion object {
        fun eval(e: Expr): Int = 
            when (e) {
                is Num -> e.value
                is Sum -> eval(e.left) + eval(e.right)
                else -> throw IllegalArgumentException()
            }
    }
}
data class Num(val value: Int): Expr
data class Sum(val left: Expr, val right: Expr): Expr

fun main(args: Array<String>) {
    println(Color.getMnemonic(Color.BLUE))
    println(Color.mix(Color.BLUE, Color.YELLOW))
    
    println(Expr.eval(Sum(Sum(Num(1), Num(2)), Num(4))))
    
    fun fizzBuzz(i: Int) = when {
        i % 15 == 0 -> "FizzBuzz "
        i % 3 == 0 -> "Fizz "
        i % 5 == 0 -> "Buzz "
        else -> "$i "
    }
    
    for (i in (1..100))
        print(fizzBuzz(i))


    val range = 1..10
    range.forEach(::println)
    var x = 3
    val rangeR = 100 downTo 1 step x
    rangeR.forEach(::println)
    
    val binaries = TreeMap<Char, String>()
    for (c in 'A'..'F') {
        val binary = Integer.toBinaryString(c.toInt())
        binaries[c] = binary
    }
    for ((key, value) in binaries)
        println("($key - $value)")
    
    val list = arrayListOf("10", "11", "1001")
    for ((index, e) in list.withIndex())
        println("$index - $e")
    
    fun isLetter(c: Char): Boolean = c in 'a' .. 'z' || c in 'A' .. 'Z'
    fun isNotDigit(c: Char): Boolean = c !in '0' .. '9'
    
    println(isLetter('q'))
    println(isNotDigit('x'))
    
    fun categorized(c: Char): String = when (c) {
        in '0'..'9' -> "digit"
        in 'a'..'z', in 'A'..'Z' -> "letter"
        else -> "unknown"
    }
    
    println(categorized('9'))
    
    println("Kotlin" in "Java".."Scala")
    println("Kotlin" in setOf("Java", "Scala"))
    
    fun readNum(reader: BufferedReader): Int? {
        return try {
            reader.readLine().toInt()
        } catch (e: NumberFormatException) {
            return null
        }
    }
}