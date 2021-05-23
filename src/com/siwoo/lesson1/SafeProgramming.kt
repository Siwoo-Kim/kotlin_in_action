package com.siwoo.lesson1

import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    // substitution model
    val x = add(mult(2, 3), mult(4, 5))
    println(x)
    
    val creditCard = CreditCard()
    val purchases = buyDonut(5, creditCard)
    assertEquals(Donut.PRICE * 5, purchases.payment.amount)
    assertEquals(creditCard, purchases.payment.creditCard)
}

data class Purchase(val donut: List<Donut>, val payment: Payment)

class Payment(val creditCard: CreditCard, val amount: Int) {
    
    fun combine(p: Payment): Payment {
        if (p.creditCard == creditCard)
            return Payment(creditCard, amount+p.amount)
        else
            throw IllegalArgumentException("Cards don't match")
    }
    
    companion object {
        fun groupByCard(payments: List<Payment>): List<Payment> =
            payments.groupBy { it.creditCard }
                .values
                .map { it.reduce(Payment::combine) }
    }
}

class CreditCard {
    fun charge(donut: Donut) {
        
    }
}

class Donut {
    
    companion object {
        val PRICE = 100
    }
}

fun buyDonut(quantity: Int = 1, creditCard: CreditCard): Purchase {
    return Purchase(List(quantity) { Donut() }, 
        Payment(creditCard, Donut.PRICE * quantity))
}

fun add(a: Int, b: Int): Int {
    //side effect
    log("Returning ${a + b} as the result of $a + $b")
    return a + b
}

fun mult(a: Int, b: Int): Int {
    return a * b
}

fun log(s: String) = println(s)

