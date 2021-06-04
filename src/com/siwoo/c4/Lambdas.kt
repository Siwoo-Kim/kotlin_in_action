package com.siwoo.c4

import java.io.File

private data class Person(val name: String, val age: Int)

private fun findOldest(people: List<Person>): Person? {
    var maxAge = 0
    var candidate: Person? = null
    for (p in people) {
        if (p.age > maxAge) {
            maxAge = p.age
            candidate = p
        }
    }
    return candidate
}

fun printErrorWithPrefix(msgs: Collection<String>, prefix: String) {
    msgs.filter { it.startsWith("5") || it.startsWith("4") }.forEach {
        println("$prefix $it")
    }
}

fun printErrorCounts(msgs: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0
    msgs.forEach { 
        if (it.startsWith("4")) 
            clientErrors++ 
        else if (it.startsWith("5")) 
            serverErrors++ 
    }
    println("$clientErrors client errors, $serverErrors server errors")
}

fun alphabet() = StringBuffer().apply {
    for (c in 'A' .. 'Z')
        append(c)
    append("\nNow I know the alphabet!")
}.toString()

fun main(args: Array<String>) {
    val people = listOf(Person("Alice", 29), Person("Bob", 31), Person("Carol", 31))
    val nums = listOf(1, 2, 3, 4)
    println(findOldest(people))
    println(people.maxBy {it.age})
    
    val sum = { a: Int, b: Int -> a + b }
    println(sum(1, 2))
    val names = people.joinToString(separator = " ") { it.name }
    println(names)
    
    val msgs = listOf("404 Forbidden", "404 Not Found", "201 OK", "500 Internal Server Error")
    printErrorWithPrefix(msgs, "Error:")
    printErrorCounts(msgs)
    
    println(people.filter { it.age > 30 }.map{ it.name })
    println(nums.map { it * it })
    
    val numMaps = mapOf(0 to "zero", 1 to "one")
    println(numMaps.mapValues { it.value.toUpperCase() })
    
    val canBeInClub27 = { p: Person -> p.age >= 30 }
    println(people.all(canBeInClub27))
    println(people.any(canBeInClub27))
    println(people.count(canBeInClub27))
    println(people.groupBy { it.age })
    
    val ss = listOf("a", "ab", "b")
    println(ss.groupBy { it.first() })
    
    data class Book(val title: String, val authors: List<String>)
    
    val books = listOf(Book("Thursday Next", listOf("Jasper Fforde")),
            Book("Mort", listOf("Terry Pratchett")),
            Book("Good Omens", listOf("Terry Pratchett", "Neil Gaiman")))
    println(books.map{it.authors})
    println(books.flatMap{it.authors})
    
    //lazy collection = sequence
    people.asSequence().map(Person::name).filter { it.startsWith("A") }.toList()
    
    listOf(1, 2, 3, 4).asSequence()
        .map { print("map($it) "); it * it  }
        .filter { print("filter($it) "); it % 2 == 0 }
        .toList()
    
    println()
    println(listOf(1, 2, 3, 4).asSequence().map{
        print("map: $it ")
        it * it 
    }.find { 
        print("find: $it ")
        it > 3 
    })
    
    val naturalNums = generateSequence(0) { it + 1 }
    val numsTo100 = naturalNums.takeWhile { it <= 100 }
    println(numsTo100.sum())
    
    fun File.isInsideHiddenDirectory() = generateSequence(this) { it.parentFile }.findLast { it.isHidden }
    
    val file = File("/home/siwoo/.mvn/settings.xml")
    println(file.isInsideHiddenDirectory())
    
    println(alphabet())
}




























