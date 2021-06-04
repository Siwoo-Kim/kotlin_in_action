package com.siwoo.c2

import java.lang.StringBuilder

@JvmName("StringFunctions")
fun <T> Collection<T>.joinToString(
    collection: Collection<T>,
    separator: String = ",",
    prefix: String = "",
    postFix: String = ""
): String {
    val result = StringBuilder(prefix)
    for ((index, e) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(e)
    }
    result.append(postFix)
    return result.toString()
}

fun Collection<String>.join(separator: String = ", "): String = joinToString(separator)

const val UNIX_LINE_SEPARATOR = "\n"

var opCount = 0 //static

fun performOp() {
    opCount++
}

fun reportOperationCount() {
    println("Operation performed ${opCount} times")
}

fun main(args: Array<String>) {
    val list = listOf(1, 2, 3)
    val list2 = list + 4
    val list3 = list + list2
    println(list)
    println(list2)
    println(list3)
    
    val mlist1 = mutableListOf(1, 2, 3)
    val mlist2 = mlist1.add(4)
    val mlist3 = mlist1.addAll(mlist1)
    println(mlist1)
    println(mlist2)
    println(mlist3)
    
    fun create() {
        val set = hashSetOf(1, 7, 53)
        val list = arrayListOf(1, 7, 53)
        val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
        val pair = 1 to 2
        println(pair.javaClass)
        println(set.javaClass)
        println(list.javaClass)
        println(map.javaClass)
        
        println(list.last())
        println(list.max())
        println(list.joinToString(list, prefix = "(", postFix = ")", separator = ";"))
        println(listOf("a", "b", "c").join())
    }
    create()
    
    val ss = "12.345-6.A".split("[.\\-]".toRegex())
    println(ss)
    
    parsePath("/home/siwoo/kotlin/chapter-1.docx")
    
    //multiline tripe-quoted strings.
    val kotlinLogo = """| //        
                       .|//
                       .|/ \
    """.trimMargin(".")
    println(kotlinLogo)
    
    data class User(val id: Int, val name: String, val address: String)
    
    fun User.validate() {
        fun validate(value: String, fieldName: String) {
            if (value.isEmpty())
                throw IllegalArgumentException("Can't save user ${this.id}: empty $fieldName")
        }
        validate("name", "Name")
        validate("address", "Address")
    }
    
    fun saveUser(user: User) {
        user.validate()
        // save user
    }
    
    saveUser(User(1, "", ""))
}

fun parsePath(path: String) {
    val regex = "(.+)/(.+)\\.(.+)".toRegex()
    val matchResult = regex.matchEntire(path)
    if (matchResult != null) {
        val (dir, filename, extension) = matchResult.destructured
        println("Dir $dir, name: $filename, extension: $extension")
    }
}