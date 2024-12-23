package com.example.demo

fun Long?.orZero(): Long {
    return this ?: 0
}