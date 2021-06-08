package com.siwoo.c7

import java.lang.StringBuilder
import java.util.concurrent.locks.Lock

enum class Delivery { STANDARD, EXPEDITED }

data class Order(val itemCount: Int)

data class Person(val firstName: String, val lastName: String, val phoneNum: String?)

class ContractsFilter {
    var prefix: String = ""
    var onlyPhoneNumber: Boolean = false 
    
    fun getPredicate(): (Person) -> Boolean {
        val startWithPrefix = { p: Person ->
            p.firstName.startsWith(prefix) || p.lastName.startsWith(prefix)
        }
        return if (!onlyPhoneNumber) startWithPrefix else { p -> startWithPrefix(p) && p.phoneNum != null } 
    }
}

data class SiteVisit(val path: String, val duration: Double, val os: OS)

enum class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }

val log = listOf(
    SiteVisit("/", 34.0, OS.WINDOWS),
    SiteVisit("/", 22.0, OS.MAC),
    SiteVisit("/login", 12.0, OS.WINDOWS),
    SiteVisit("/singup", 8.0, OS.IOS),
    SiteVisit("/", 16.3, OS.ANDROID)
)


inline fun <T> synchronized(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    }
}

fun main(args: Array<String>) {
    //higher-order functions - take lambdas as arg or return lambda
    
    //function type
//    val sum = {x: Int, y: Int -> x + y }
    val sum: (Int, Int) -> Int = { x, y -> x + y }
//    val action = { println(42) }
    val action: () -> Unit = { println(42) }
    println(sum(1, 2))
    action()
    
    // function can be nullable
    val funOrNull: ((Int, Int) -> Int)? = null
    
    
    // function as arg
    fun twoAndThree(op: (Int, Int) -> Int) {
        val result = op(2, 3)
        println("The result id $result")
    }
    twoAndThree(sum)
    twoAndThree { a, b -> a * b }
    
    fun String.filter(p: (Char) -> Boolean): String {
        val sb = StringBuilder()
        for (i in this.indices)
            if (p(this[i]))
                sb.append(this[i])
        return sb.toString()
    }
    println("ab1c".filter { it in 'a'..'z'})
    
    // default function parameter
    fun <T> Collection<T>.joinToString(
        separator: String = ", ",
        prefix: String = "",
        postfix: String = "",
        transform: (T) -> String = { it.toString() }    // lambda as a default value
    ): String {
        val result = StringBuilder(prefix)
        for ((i, e) in withIndex()) {
            if (i > 0) result.append(separator)
            result.append(transform(e))
        }
        result.append(postfix)
        return result.toString()
    }
    val letters = listOf("Alpha", "Beta")
    println(letters.joinToString { it.toLowerCase() })
    
    // invoke = 이름 없이 호출되는 함수. 람다는 호출될때 invoke 을 호출한다. 
    // if lambda is nullable, use invoke to safe-call
    fun foo(callback: (() -> Unit)?) {
        callback?.invoke()
    }
    
    // returning functions from functions.
    //  - depending on the state, choose the appropriate logic.
    // 상태에 따라 올바른 값이 아닌 연산을 리턴할시
    fun shippingCost(delivery: Delivery): (Order) -> Double = 
        when (delivery) {
            Delivery.EXPEDITED -> { order -> 6 + 2.1 * order.itemCount }
            Delivery.STANDARD -> { order -> 1.2 * order.itemCount }
        }
    println("Shipping costs ${shippingCost(Delivery.STANDARD)(Order(3))}")
    println("Shipping costs ${shippingCost(Delivery.EXPEDITED)(Order(3))}")
    
    val contracts = listOf(Person("Dmitry", "Jemerov", "123-4567"), 
        Person("Stevtlana", "Isakova", null))
    val filter = ContractsFilter()
    with (filter) { // set properties of instance using "with"
        prefix = "Dm"
        onlyPhoneNumber = true
    }
    println(contracts.filter(filter.getPredicate()))
    
    //removing dup with HOF
    println(log.filter { it.os == OS.WINDOWS }
        .map { it.duration }
        .average())
    println(log.filter { it.os == OS.MAC }
        .map { it.duration }
        .average())
    
    fun List<SiteVisit>.avg(p: (SiteVisit) -> Boolean) =
        filter(p).map(SiteVisit::duration).average()
    println(log.avg { it.os in setOf(OS.WINDOWS, OS.MAC) })
    println(log.avg { it.os == OS.MAC })
    
    // inline functions
    //  lambda -> extra class is created -> runtime overhead.
    // if you mark a function with the inline modifer, the compiler will "replace every call" to the function with the "actual code impl".
    //  inline improve performance only with the functions that take lambda as an arguments.
    
    // control flow in HOF
    // "return" in lambadas: return from enclosing function ( only for "inline" )
    // 
    // return with a label: return the lambda and continues execution of the code from which the lmabda was invoked.
    fun lookForAlice(people: List<Person>) {
        people.forEach label@{ p ->
            if (p.lastName == "Alice") {
                println("Found!")
//                return         // return outer function
                return@label  // return lambaa
            }
        }
        println("Alice is not found")
    }
    lookForAlice(listOf(Person("", "Alice", "29"), Person("", "Bob", "31")))
    
    // Labeled "this" expression
    // specify the label of a lambda with a rexiever, can access its implicit receiver using the label.
    println(StringBuilder().apply sb@{
        listOf(1, 2, 3).apply { 
            this@sb.append(this.toString())
        }
    })
}





















