package com.mmfsin.musicmaster.base

abstract class BaseUseCaseNoParams<T> {
    abstract suspend fun execute(): T
}