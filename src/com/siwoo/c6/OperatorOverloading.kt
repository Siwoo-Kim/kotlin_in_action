package com.siwoo.c6

import org.omg.CosNaming.NameComponent
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.lang.IndexOutOfBoundsException
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

private data class Point(var x: Int, var y: Int) {
    operator fun plus(p: Point) = Point(x+p.x, y+p.y)
}

private data class Person(val firstName: String, val lastName: String)
    : Comparable<Person> {
    override fun compareTo(other: Person): Int {
        return compareValuesBy(this, other, Person::lastName, Person::firstName)
    }
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
    
    // Ordering operators with compareTo
    println(Person("Alice", "Smith") < Person("Bob", "Johnson"))
    
    // get & set index operator
    operator fun Point.get(index: Int): Int = when(index) { 
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException()
    }
    operator fun Point.set(index: Int, value: Int) = when(index) {
        0 -> x = value
        1 -> y = value
        else -> throw IndexOutOfBoundsException()
    }
    val p = Point(10, 20)
    println(p[1])
    p[1] = p[1] * 20
    println(p[1])
    
    // in operator (= contains)
    data class Rectangle(val upperLeft: Point, val lowerRight: Point) {
        operator fun contains(p: Point): Boolean {
            return p.x in upperLeft.x until lowerRight.x &&
                    p.y in upperLeft.y until lowerRight.y
        }
    }
    
    val rec = Rectangle(Point(10, 20), Point(50, 50))
    println(Point(20, 30) in rec)
    println(Point(5, 5) in rec)
    
    // in (contains)
    println(5 in listOf(1, 2, 3, 4))
    
    // rangeTo operator. can use rangeTo operator when class implements Comparable.
    val now = LocalDate.now()
    val vacation = now..now.plusDays(10)
    println(now.plusWeeks(1) in vacation)
    
    println(Person("c", "c") in Person("a", "a") .. Person("d", "d"))
    
    // for each loop needs iterator. you can have extension function to implement iterator
    operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> = 
        object : Iterator<LocalDate> {
            var current = start

            override fun hasNext() = current <= endInclusive
            override fun next() = current.apply { current = plusDays(1) }
        }       
    val newYear = LocalDate.ofYearDay(2017, 1)
    val daysOff = newYear.minusDays(1) .. newYear
    for (dayOff in daysOff) println(dayOff)

    // destructing declaration and component functions 
    // uses function componentN (defined order)
    data class Data(val x: Int, val y: Int, val z: Int)
    val d = Data(1, 2, 3)
    val (x, y, z) = d
    println("$x, $y")
    
    // destructing is useful when return multiple values
    data class FileName(val name: String, val extension: String)
    fun splitFileName(fullName: String): FileName {
        val result = fullName.split(".", limit = 2)
        return FileName(result[0], result[1])
    }
    val (name, ext) = splitFileName("example.kt")
    println("$name, $ext")
    
    fun <K, V> printEntries(map: Map<K, V>) {
        for ((k, v) in map)
            println("$k -> $v")
    }
    
    val map = mapOf("Oracle" to "Java", "JetBrains" to "Kotlin")
    printEntries(map)
    
    //delegated properties. delegates a task to helper object
    //  - object helper
    //  - [type] by [helper]
    class Delegate() {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String 
            = "$thisRef, delegating ${property.name}"
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String)
            = println("$value has been assinged to ${property.name} in $thisRef")
    }
    
    class Foo {
        val de = Delegate()
        var p: String by de
    }
    
    val foo = Foo()
    println(foo.p)
    foo.p = "s"
    
    // lazy init = using delegated properties.
    //  invoke when it's accessed for the first time.
    //  lazy funtion is thread-safe
    data class User(val name: String) {
        val emails by lazy {
            loadEmails(this)
        }
        
        private fun loadEmails(user: User) =
            listOf("kotlin@gg.com", "java@gg.com")
    }
    val user = User("test")
    
    open class PropertyChangeAware {
        protected val support = PropertyChangeSupport(this)
        
        fun addListener(l: PropertyChangeListener) = support.addPropertyChangeListener(l)
        fun removeListener(l: PropertyChangeListener) = support.removePropertyChangeListener(l)
    }

    /**
     * delegation.
     *  operator fun getValue & setValue
     *      params:
     *         firstArg = instance
     *         secondArg = property of the instance
     */
    class Listenable<T>(var propValue: T, val support: PropertyChangeSupport) {
        operator fun getValue(o: Any?, prop: KProperty<*>): T = propValue
        operator fun setValue(o: Any?, prop: KProperty<*>, newValue: T) {
            val oldValue = propValue
            propValue = newValue
            support.firePropertyChange(prop.name, oldValue, newValue)
        }
    }

    class P(val name: String, age: Int, salary: Int): PropertyChangeAware() {
//        var age: Int by Listenable(age, support)
//        var salary: Int by Listenable(salary, support)
        private val onChange = {
            prop: KProperty<*>, oldValue: Int, newValue: Int ->
            support.firePropertyChange(prop.name, oldValue, newValue)
        }
        var age: Int by Delegates.observable(age, onChange)
        var salary: Int by Delegates.observable(salary, onChange)
        
        private val _attributes = hashMapOf<String, String>()
        fun setAttribute(attrName: String, value: String) { _attributes[attrName] = value }
        val email: String by _attributes

        operator fun get(key: String): String = _attributes[key]!!
    }
    

    val pp = P("Dmitry", 34, 2000)
    pp.addListener( PropertyChangeListener { event -> 
        println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
    })
    pp.age = 35
    pp.salary = 2100
    
    val data = mapOf("name" to "Dmitry", "email" to "kotlin@gg.com", "company" to "JetBrains")
    for ((attr, value) in data)
        pp.setAttribute(attr, value)
    println(pp["email"])
}