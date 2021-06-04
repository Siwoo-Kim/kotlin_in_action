package com.siwoo.c2

import java.io.Serializable
import java.time.Instant

/**
 * internal modifier - 같은 모듈 안 scope.
 * protected - 오직 클래스를 확장하는 경우에만의 scope.
 * init -> 생성자 구현
 * 
 * static memeber -> package level 에 정의
 * 
 * singleton -> object
 */

val MAX = 10

object SystemEnv {
    
    fun getEnv(name: String): String = System.getenv(name)
    
}

data class Person(val name: String, val registered: Instant = Instant.now())
    : Serializable, Comparable<Person> {
    
    constructor(name: Name): this(name.toString()) {
        println(name)
    }
    
    override fun compareTo(other: Person): Int = name.compareTo(other.name)
    
    companion object {
        fun parse(xml: String): Person {
            TODO("Do implement")
        }
    }
}

data class Name(val name: String)

fun show(persons: List<Person>) {
    for ((name, date) in persons)
        println("${name} ${date}")
}

fun main(args: Array<String>) {
    val person1 = Person("Bob", Instant.now())
    val person2 = Person(Name("Jay"))
    
    show(listOf(person1, person2))
    
}