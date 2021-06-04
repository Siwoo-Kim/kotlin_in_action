package com.siwoo.c2

fun main(args: Array<String>) {
    val a = 3
    val b = 5
    val c = if (a < b) b else a // if is expression
    println(c)
    
    val percent = if (b != 0) {
        val tmp = a / b.toDouble()
        tmp * 100 // no return
    } else 0.0
    
    println(percent)
    
    val country = "Korea"
    
    val capital = when {  // should take all possible answer
        country == "Korea" -> "Seoul"  //Sequentially eval
        country.toLowerCase() == "korea" -> "seoul"
        country == "Japan" -> "Tokyo"
        country == "Canada" -> "Ottawa"
        else -> "Unknown"
    }
    println(capital)
    
    for (i in (0 .. 100).map { it * it }.takeWhile { it < 100 })
        println(i)
    val range = 0 until 10 step 2
    for (i in range) println(i)
}