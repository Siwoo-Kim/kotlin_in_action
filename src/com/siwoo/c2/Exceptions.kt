package com.siwoo.c2

import java.lang.Exception
import java.nio.file.Paths

fun main(args: Array<String>) {
    val num : Int = try { args[0].toInt() } 
        catch (e: Exception) { 0 }
        finally {
            println("get num")
        }
    println(num)
    
    val lines = Paths.get("./test.txt").toFile()
        .inputStream()
        .use {  // auto-closeable
            it.bufferedReader()
                .lineSequence() // Sequence - lazy eval (should be used in "use" block)
                .toList()
        }
    for (s in lines) println(s)
}