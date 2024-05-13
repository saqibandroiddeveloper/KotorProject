package com.saqiii.kotorproject.ktor.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BodyFatModel(
    @SerialName("Body Fat (U.S. Navy Method)")
    val bodyFatNavyMethod: Double,
    @SerialName("Body Fat Category")
    val fatCategory: String,
    @SerialName("Body Fat Mass")
    val fatMass: Double,
    @SerialName("Lean Body Mass")
    val leanBodyMass: Double,
    @SerialName("Body Fat (BMI method)")
    val bmi:Double
)
@kotlinx.serialization.Serializable
data class BodyFatResponse(
    val `data`: BodyFatModel,
    val request_result: String,
    val status_code: Int
)