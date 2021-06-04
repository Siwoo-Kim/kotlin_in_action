package com.siwoo.c3

import java.io.Serializable
import java.lang.IllegalStateException

interface Clickable {
    fun click()
    fun showOff() = println("I'm clickable")    //default method
}

interface Focusable {
    fun setFocus(b: Boolean) = println("I ${if (b) "got" else "lost"} focus")
    fun showOff() = println("I'm focusable.")
}

class Button: Clickable, Focusable, View {
    override fun click() = println("I was clicked")
    
    override fun showOff() {    // multi-inherit
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }
    
    inner class ButtonState: State {    //default is static class. 
        
    }

    override fun getCurrentState(): State {
        TODO("Not yet implemented")
    }

    override fun restoreState(state: State) {
        TODO("Not yet implemented")
    }
}

open class RichButton: Clickable {
    fun disable() {}
    open fun animate() {}   // only this can be overridden by subclass
    final override fun click() {}
}

class TestButton: RichButton() {

    override fun animate() {
        super.animate()
    }

}

abstract class Animated {
    abstract fun animate()  //must be overridden
    
    open fun stopAnimating() {} // can be overridden
    
    fun animateTwice() { }  // final method
}

internal open class TalkactiveButton: Focusable {
    private fun yell() = println("hey!")
    protected fun whisper() = println("let's a talk")
}

internal fun TalkactiveButton.giveSpeech() {    // cannot be public because the class is internal
    //yell()
    //whisper()
}

interface State: Serializable

interface View {
    fun getCurrentState(): State
    fun restoreState(state: State)
}

class Outer {
    val x = 1
    
    inner class Inner {
        fun getOuterReference(): Outer = this@Outer     //to access outer
    }
}

sealed class Expr { //all subclass should be in Expr
    
    fun eval(): Int = 
        when(this) {
            is Num -> value
            is Sum -> left.eval() + right.eval()
            else -> throw IllegalStateException()
        }

    class Num(val value: Int): Expr()
    
    class Sum(val left: Expr, val right: Expr): Expr()
}



fun main(args: Array<String>) {
    val button = Button()
    button.showOff()
    button.setFocus(true)
    button.click()
}