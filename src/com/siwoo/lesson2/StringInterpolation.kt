package com.siwoo.lesson2

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun main(args: Array<String>) {
    val name = "Jay"
    val date = ZonedDateTime.of(LocalDateTime.of(2022, 3, 4, 0, 0, 0), ZoneId.of("UTC"))
    
    //eval with $ or ${}
    println("$name's registration date: $date")
    println("$name's registration date: ${date.withZoneSameInstant(ZoneId.of("America/New_York"))}")
    
    //multiple lines with """string"""
    // | use to locate the start point in trimMargin
    println("""
        |This is the first line
    |interfere
        |and this is the second line
    """.trimMargin("|"))
}