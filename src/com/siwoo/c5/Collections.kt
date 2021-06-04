package com.siwoo.c5

private sealed class List<out A> {
    
    abstract fun isEmpty(): Boolean
    
    fun dropWhile(p: (A) -> Boolean) = dropWhile(p, this)
    
    fun cons(a: @UnsafeVariance A) = Cons(a, this)
    
    fun drop(n: Int) = drop(n, this)
    
    fun concat(a: List<@UnsafeVariance A>) = concat(this, a)
    
    fun reverse() = reverse(invoke(), this)
    
    fun setHead(a: @UnsafeVariance A): List<A> = 
        when(this) {
            is Nil -> throw IllegalStateException()
            is Cons -> tail.cons(a)
        }

    fun init() = init(this)
    
    fun <B> foldLeft(identity: B, f: (B) -> (A) -> B) = foldLeft(this, identity, f)
    
    fun <B> foldRight(identity: B, f: (A) -> (B) -> B) = foldRight(this, identity, f)

    fun length(): Int = length(this)
    
    private object Nil: List<Nothing>() {
        override fun isEmpty(): Boolean = true
        override fun toString(): String = "[Nil]"
    }
    
    private class Cons<A>(val head: A, val tail: List<A>): List<A>() {
        override fun isEmpty(): Boolean = false
        override fun toString(): String = "[${toString("", this)}Nil]"
        fun toString(s: String, a: List<A>): String = 
            when (a) {
                is Nil -> s
                is Cons -> toString("$s${a.head}, ", a.tail)
            }
    }
    
    companion object {
        operator fun <A> invoke(vararg a: A): List<A>
            = a.foldRight(Nil as List<A>) { a, acc: List<A> -> Cons(a, acc) }

        fun <A> drop(n: Int, a: List<A>): List<A> {
            tailrec fun drop(n: Int, a: List<A>): List<A> =
                if (n <= 0) a else when(a) {
                    is Nil -> a
                    is Cons -> drop(n-1, a.tail)
                }
            return drop(n, a)
        }
        
        fun <A> dropWhile(p: (A) -> Boolean, a: List<A>): List<A> {
            tailrec fun dropWhile(a: List<A>): List<A> = when(a) {
                is Nil -> a
                is Cons -> if (p(a.head)) dropWhile(a.tail) else a
            }
            return dropWhile(a)
        }
        
        fun <A> concat(a: List<A>, b: List<A>): List<A> {
            fun concat_(a: List<A>, b: List<A>): List<A> =
                when (a) {
                    is Nil -> b
                    is Cons -> concat_(a.tail, b).cons(a.head) 
                }
            return concat_(a, b)
        }
        
        fun <A> init(a: List<A>): List<A> = 
            if (a is Nil) throw IllegalStateException()
            else a.reverse().drop(1).reverse()
        
        // a -> b -> c -> d -> Nil
        fun <A> reverse(acc:List<A>, a: List<A>): List<A> = foldLeft(a, acc) { acc -> { acc.cons(it) } }
        
        fun <A> length(a: List<A>): Int = foldLeft(a, 0) { acc -> { acc + 1 } }
        
        fun sum(a: List<Int>): Int = foldLeft(a, 0) { acc -> { e -> acc + e } }
        
        fun product(a: List<Double>): Double = foldLeft(a, 1.0) { acc -> { e -> acc * e } }
        
        fun <A, B> foldRight(a: List<A>, identity: B, f: (A) -> (B) -> B): B =
            foldLeft(a.reverse(), identity) { acc -> { e -> f(e)(acc) } }
        
        tailrec fun <A, B> foldLeft(a: List<A>, identity: B, f: (B) -> (A) -> B): B = 
            when(a) {
                is Nil -> identity
                is Cons -> foldLeft(a.tail, f(identity)(a.head), f)
            }
    }
}

fun main(args: Array<String>) {
    val a = List(1, 2, 3, 4, 5)
    println(a)
    val b = a.cons(4)
    println(b)
//    val c = b.setHead(0)
//    println(c)
    val d = a.drop(2)
    println(d)
    val e = a.dropWhile { it < 3 }
    println(e)
    
    println(a.concat(List(1, 2, 3)))
    println(a.reverse())
    println(List<Int>())
    println(a.init())
    println(List.sum(List(1, 2, 3)))
    println(List(1, 2, 3).length())
}