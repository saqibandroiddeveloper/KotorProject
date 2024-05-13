package com.saqiii.kotorproject.ktor.components

sealed class RequestTypes {
    data object Get : RequestTypes()
    data class Post(val body: Any) : RequestTypes()
}