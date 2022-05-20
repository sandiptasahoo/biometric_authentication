package com.example.biometric

import android.util.Log

open class Parent {
    private val a = println("Parent.a")

    constructor(arg: Unit=println("Parent primary constructor default argument")) {
        println("Parent primary constructor")
    }

    init {
        println("Parent.init")
    }

    private val b = println("Parent.b")
}

class Child : Parent {
    val a = println("Child.a")

    init {
        println("Child.init 1")
    }

    constructor(arg: Unit=println("Child primary constructor default argument")) : super() {
        println("Child primary constructor")
    }

    val b = println("Child.b")

    constructor(arg: Int, arg2:Unit= println("Child secondary constructor default argument")): this() {
        println("Child secondary constructor")
    }

    init {
        println("Child.init 2")
    }
}
