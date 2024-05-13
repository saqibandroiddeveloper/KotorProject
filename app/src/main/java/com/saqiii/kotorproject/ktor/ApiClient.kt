package com.saqiii.kotorproject.ktor

import com.saqiii.kotorproject.ktor.NetworkClient.makeNetworkRequest
import com.saqiii.kotorproject.ktor.components.NetworkResponse
import com.saqiii.kotorproject.ktor.components.RequestTypes
import com.saqiii.kotorproject.ktor.model.BodyFatResponse


class ApiClient {
    suspend fun getResponseOfFat(
        age: Int,
        gender: String,
        weight: Int,
        height: Int,
        neck: Int,
        waist: Int,
        hip: Int,
    ): NetworkResponse<BodyFatResponse> {
        val url =
            "https://fitness-calculator.p.rapidapi.com/bodyfat?age=$age&gender=$gender&weight=$weight&height=$height&neck=$neck&waist=$waist&hip=$hip"
        return makeNetworkRequest<BodyFatResponse>(
            url, RequestTypes.Get, headers = mapOf(
                "X-RapidAPI-Key" to "566ad4c078msha9d57d19e2c7942p196e13jsnf62d088d0bfe",
                "X-RapidAPI-Host" to "fitness-calculator.p.rapidapi.com"
            )
        )
    }
}