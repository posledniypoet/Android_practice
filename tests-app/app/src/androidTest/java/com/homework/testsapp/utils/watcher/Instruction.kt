package com.homework.testsapp.utils.watcher

interface Instruction {

    fun getDescription(): String

    fun checkCondition(): Boolean
}