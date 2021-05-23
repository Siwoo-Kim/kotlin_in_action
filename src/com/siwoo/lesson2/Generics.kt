package com.siwoo.lesson2

/**
 * covariance (공변성)
 *  Red 가 Color 의 하위 타입이라면
 *  Matcher<Red> 도 Matcher<Color> 의 하위 타입.
 * 
 * contravariant (반공변성)
 *  Red 가 Color 의 하위 타입이라면
 *  Matcher<Color> 도 Matcher<Red> 의 하위 타입.
 *  
 * in = 값을 넣기만 한다.
 * out = 값을 가져오기만 한다.
 */
fun main(args: Array<String>) {
    val s = "A string"
    val a: Any = s
    
    val ls = mutableListOf(s)
    val la: MutableList<Any> = mutableListOf()
    addAll(la, ls)  // ls out as Any, should be fine.

    useBag(BagImpl())
}

fun useBag(bag: Bag<in MyClass>): Boolean {
    return true
}

interface Bag<T> {
    fun get(): T
    fun use(t: T): Boolean
}

class BagImpl: Bag<MyClassParent> {
    override fun get(): MyClassParent = MyClassParent()
    override fun use(t: MyClassParent): Boolean = true
}

//interface Bag<in T> {
////    fun get(): T
//    fun use(t: T): Boolean
//}
//
//class BagImpl: Bag<MyClassParent> {
//    override fun use(t: MyClassParent) = true
//
////    override fun get(): MyClass = MyClass()
//}

open class MyClassParent

class MyClass: MyClassParent()

fun <T> addAll(list1: MutableList<T>, list2: MutableList<out T>) {
    for (e in list2) list1.add(e)
}