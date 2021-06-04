package com.siwoo.c5

import java.io.BufferedReader
import java.lang.IllegalStateException
import java.lang.NumberFormatException

fun printAllCaps(s: String?) {
    val allCaps: String? = s?.toUpperCase()
    println(allCaps)
}

class Employee(val name: String, val manager: Employee?)

fun managerName(emp: Employee): String? = emp.manager?.name

class Address(val street: String, val zip: Int, val city: String, val country: String)
class Company(val name: String, val address: Address?)

class Person(val name: String, val company: Company? = null, val age: Int? = null) {
    fun isOrderThan(other: Person): Boolean? {
        if (age == null || other.age == null)
            return null
        return this.age > other.age
    }
    override fun equals(o: Any?): Boolean {
        val that = o as? Person ?: return false // terminate
        return that.name == name
    }

    override fun hashCode(): Int = name.hashCode() * 37
}

fun Person.countryName() = this.company?.address?.country ?: "Unknown"

fun foo(s: String?) = s ?: ""   // elvis operator

fun strLenSafe(s: String?) = s?.length ?: 0 // default value for null

fun printShipping(p: Person) {
    val address = p.company?.address ?: throw IllegalArgumentException()
    with(address) { //change context
        println(street)
        println("$zip $city, $country")
    }
}

fun ignoreNulls(s: String?) {
    val sNotNull: String = s!!
    println(sNotNull.length)
}

fun sendEmailTo(email: String) {    // ?.let function = ifPresent
    println("Sending email to $email")
}

class MyService {
    fun serve(): String = "foo"
}

class MyTest {
    private lateinit var myService: MyService
    
    fun setup(): MyTest {
        myService = MyService()
        return this
    }
    
    fun testServer() {
        assert("foo" == myService.serve())
    }
}

fun verifyUserInput(input: String?) {
    fun String?.isNullOrBlank(): Boolean = this?.isBlank() ?: true  // extension function for nullable type
    if (input.isNullOrBlank())  // no safe call is required.
        println("Please fill in the required fields")
}

fun <T: Any> printHashCode(t: T) {
    println(t.hashCode())
}

fun showProgress(progress: Int) {
    val percent = progress.coerceIn(0, 100)
    println("We're ${percent}% done!")
}

interface Processor<T> {
    fun process(): T
}

class NoResultProcessor: Processor<Unit> {
    override fun process() {
        // no return
    }
}

fun fail(message: String): Nothing {    // never return result
    throw IllegalStateException(message)
}

fun readNums(reader: BufferedReader): List<Int?> {
    val result = ArrayList<Int?>()
    for (line in reader.lineSequence()) {
        try {
            val num = line.toInt()
            result.add(num)
        } catch (e: NumberFormatException) {
            result.add(null)
        }
    }
    return result
}

fun addValidNums(numbers: List<Int?>) {
    val validNums = numbers.filterNotNull()
    println("${validNums.sum()} : ${numbers.size - validNums.size}")
}
fun main(args: Array<String>) {
    printAllCaps("ABC")
    printAllCaps(null)
    
    val ceo = Employee("Da Boss", null)
    val developer = Employee("Bob Smith", ceo)
    println(managerName(ceo))
    println(managerName(developer))
    
    val person = Person("Dmitry", null)
    println(person.countryName())
    
    val address = Address("Elsestr. 47", 80687, "Munich", "Germany")
    val jetbrains = Company("JetBrains", address)
    val person2 = Person("Dmitry", jetbrains)
    printShipping(person2)
    
    val p1 = Person("Dimitry")
    val p2 = Person("Dimitry")
    println(p1 == p2)
    println(p1.equals(42))
    
//    ignoreNulls(null)
    
    var email: String? = "yole@example.com"
    email?.let { sendEmailTo(it) }
    email = null
    email?.let { sendEmailTo(it) }
    
    MyTest().setup().testServer()
    
    verifyUserInput(null)

//    printHashCode(null)
    var i: Long = 1
    val list = listOf(1L, 2L, 3L)
    println(i in list)
    
    showProgress(23)
    
    println('1'.toInt())    //ascii
    println("1".toInt())
    
    val answer: Any = 42    // supertype for all non-nullable type
}