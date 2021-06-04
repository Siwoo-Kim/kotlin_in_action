package com.siwoo.c4

import java.util.concurrent.ConcurrentHashMap

/**
 * corecursion - 한 단계의 결과를 다음 단계의 입력으로 사용. (첫 단계부터 차례로 계산.)
 *  공재귀는 각 단계를 즉시 계산할 수 있다. (모든 단계를 저장할 필요가 없다, 평가를 미룰 필요가 없다)
 * recursion - 마지막 단계부터 계산. 
 * 
 * 공재귀이고, 제일 마지막에 하는 일이 결과를 반환한다면 꼬리 호출을 제거하기 위해 tailrec 로 표시.
 * 
 */
fun append(c: Char, s: String) = "$s$c"
fun prepend(c: Char, s: String) = "$c$s"

fun toString(list: List<Char>): String {
    tailrec fun toString(list: List<Char>, s: String): String =
        if (list.isEmpty()) s
        else toString(list.dropLast(1), prepend(list.last(), s))
    return toString(list, "")
}

fun sum(n: Int): Int {
    tailrec fun sum(i: Int, s: Int): Int =
        if (i > n) s else sum(i+1, s+i)
    return sum(0, 0)
}

fun inc(n: Int) = n + 1
fun dec(n: Int) = n - 1
tailrec fun add(a: Int, b: Int): Int = if (b == 0) a else add(inc(a), dec(b))

val factorial: (Int) -> Int = {n ->
    tailrec fun recur(i: Int, acc: Int): Int = if (i == n) acc else recur(i+1, acc*i)
    recur(1, 1)
}

fun fibonacci(n: Int): Int {
    tailrec fun fibonacci(v1: Int, v2: Int, i: Int): Int =
        if (i == 0) 1
        else if (i == 1) v1 + v2
        else fibonacci(v2, v1+v2, i-1)
    return fibonacci(0, 1, n)
}

fun <T> makeString(list: List<T>, delim: String): String {
    tailrec fun <T> makeString(remains: List<T>, s: String): String =
        if (remains.isEmpty()) s
        else if (remains.drop(1).isEmpty()) makeString(remains.drop(1), "$s${remains.first()}")
        else makeString(remains.drop(1), "$s${remains.first()}${delim}")
    
    return makeString(list, "")
}

fun <T, U> foldLeft(init: U, list: List<T>, f: (U, T) -> U): U {
    tailrec fun fold(list: List<T>, acc: U): U =
        if (list.isEmpty()) acc
        else fold(list.drop(1), f(acc, list.first()))
    return fold(list, init)
} 

fun sumFold(list: List<Int>) = foldLeft(0, list) { acc, e -> acc + e }

fun stringFold(list: List<Char>) = foldLeft("", list) { acc, e -> acc + e }

fun <T> makeStringFold(list: List<T>, delim: String) = foldLeft("", list) { acc, e -> if (acc.isEmpty()) "$e" else "$acc$delim$e" } 

fun toStringS(list: List<Char>): String = foldRight(list, "") {u, acc -> prepend(u, acc) }

fun <T, U> foldRight(list: List<T>, acc: U, f: (T, U) -> U): U = 
    if (list.isEmpty()) acc
    else f(list.first(), foldRight(list.drop(1), acc, f))

fun <T> reverse(list: List<T>): List<T> = foldLeft(listOf(), list) { acc, e ->  prepend(acc, e) }

fun <T> prepend(list: List<T>, e: T): List<T> = foldLeft(listOf(e), list) { acc, e -> acc + e }

fun <T> copy(list: List<T>): List<T> = foldLeft(listOf(), list) { list, e -> list + e }

fun range(start: Int, end: Int): List<Int> = unfold(start, { e -> e + 1 }, { e -> e <= end })

fun <T> unfold(seed: T, f: (T) -> T, p: (T) -> Boolean): List<T> {
    fun unfold(seed: T, list: List<T>): List<T> =
        if (!p(seed)) list
        else unfold(f(seed), list + seed)
    return unfold(seed, listOf())
} 

fun rangeR(start: Int, end: Int): List<Int> = unfoldR(start, { e -> e + 1 }, { e -> e <= end })

fun <T> unfoldR(seed: T, f: (T) -> T, p: (T) -> Boolean): List<T> {
    fun unfoldR(seed: T): List<T> = 
        if (!p(seed)) listOf()
        else prepend(unfoldR(f(seed)), seed)
    return unfoldR(seed)
}

fun <T> iterate(seed: T, f: (T) -> T, n: Int): List<T> {
    fun iterate(seed: T, list: List<T>): List<T> =
        if (list.size > n) list
        else iterate( f(seed), list + seed)
    
    return iterate(seed, listOf())
}

fun <T, U> map(list: List<T>, f: (T) -> U): List<U> = foldLeft(listOf(), list) { list, e -> list + f(e) }

class Memoizer<T, U> private constructor(){
    val cache = ConcurrentHashMap<T, U>()
    
    private fun memorize(f: (T) -> U): (T) -> U = {
        key -> cache.computeIfAbsent(key) { f(it) }
    }
    
    companion object {
        fun <T, U> memorize(f: (T) -> U): (T) -> U = Memoizer<T, U>().memorize(f)
    }
}

fun main(args: Array<String>) {
    val s = toStringS(listOf('a', 'b', 'c'))
    println(s)
    
    println(sum(100))
    println(add(10, 5))
    
    println(factorial(10))
    
    (0 until 10).forEach { println("${fibonacci(it)}")}

    println(stringFold(listOf('a', 'b', 'c')))
    println(makeStringFold(listOf(1, 2, 3, 4), ","))
    println(sumFold(listOf(1, 2, 3, 4, 5)))
    
    println(reverse(listOf(1, 2, 3)))
    
    println(range(10, 15))
    println(rangeR(10, 15))
    
    range(10, 15).forEach{ i -> println(i) }
    
    val map = mutableMapOf<Int, Int>()
}
