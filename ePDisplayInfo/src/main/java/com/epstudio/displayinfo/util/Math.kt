package com.epstudio.displayinfo.util

fun gcd(num1: Int, num2: Int):Int {
    var a = num1
    var b = num2
    while(b > 0){
        val c = a % b
        a = b
        b = c
    }
    return a
}