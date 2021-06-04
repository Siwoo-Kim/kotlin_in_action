package com.siwoo.c6

import com.siwoo.c2.opCount
import java.math.BigDecimal

private data class Point(val x: Int, val y: Int) {
    operator fun plus(p: Point) = Point(x+p.x, y+p.y)
}
        
fun main(args: Array<String>) {
    val p1 = Point(10, 20)
    val p2 = Point(30, 40)
    println(p1 + p2)
    operator fun Point.minus(p: Point) = Point(x - p.x, y - p.y)    //operator overloading, - && -=
    println(p1 - p2)
    var p3 = Point(p1.x, p1.y)
    p3 -= p2
    println(p3)
    operator fun Point.times(scale: Int) = Point(x * scale, y * scale)
    println(p1 * 2)
    
    operator fun Char.times(count: Int): String = toString().repeat(count)
    println('a' * 3)
        
    //bitwise operators
    println(0x0F and 0xF0)
    println(0x0F or 0xF0)
    println(1 ushr 4)
    
    val nums = mutableListOf<Int>()
    nums += 42
    println(nums)
    
    // try not to add both plus and plusAssign operations at the same time.
    // mutable - plusAssign +=
    // immutable - plus +
    operator fun StringBuilder.plusAssign(e: String) {  append(e); }
    val s = java.lang.StringBuilder()
    s += "a"
    println(s)
    
    val list = mutableListOf(1, 2)
    list += 3
    val newList = listOf(4, 5) + list
    println(newList)
    
    operator fun Point.unaryMinus(): Point = Point(-x, -y)
    println(-p1)
    
    operator fun BigDecimal.inc() = this + BigDecimal.ONE
    var bd = BigDecimal.ZERO
    println(bd++)
    println(++bd)
}