package com.siwoo.c2

fun main(args: Array<String>) {
    val payload: Any = "msg"
    
    val length = when(payload) {
        is String -> payload.length
        is Int -> payload
        else -> -1
    }
    println(length)
    
    val s = payload as String   //unsafe casting
    println(s)
    val num: Int? = payload as? Int   //safe casting
    println(num)
    
    val a = Integer(2)
    val b = Integer(2)
    println(a == b)
    println(a === b)
}