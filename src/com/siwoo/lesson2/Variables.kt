package com.siwoo.lesson2

fun main(args: Array<String>) {
    lazy()
}

fun typeInference() {
    val name1: String = "Frank"
    val name2 = "hyun"
    
    var name3 = "joyce"
    name3 = "lee"
}

fun lazy() {
    var name: String? = null    //nullable type
    name = getName()
    
    val lazyName by lazy {
        getName()
    }
    lateinit var lateName: String
    
    (0 .. 10).forEach { 
        if (it == 5) {
            println(lazyName)
            lateName = getName()
            println(lateName)
        }
        println(it) 
    }
    
}

fun getName(): String = "siwoo"