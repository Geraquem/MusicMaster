package com.mmfsin.musicmaster.base

abstract class BaseUseCase<params, T> {
    abstract suspend fun execute(params: params): T
}