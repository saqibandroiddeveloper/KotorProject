package com.saqiii.kotorproject

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.saqiii.kotorproject.ktor.ApiClient
import com.saqiii.kotorproject.ktor.model.InputModel
import com.saqiii.kotorproject.ktor.components.NetworkResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val model = InputModel(
            34,"male",70,178,50,96,92
        )
//        callAPI(model)
    }

    private fun callAPI(model: InputModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiClient().getResponseOfFat(
                model.age,
                model.gender,
                model.weight,
                model.height,
                model.neck,
                model.waist,
                model.hip
            )
            when (response) {
                is NetworkResponse.Failure -> {
                    Log.d("saqiii", "callAPI: ${response.error}")
                }
                is NetworkResponse.Idle -> {}
                is NetworkResponse.Loading -> {
                    Log.d("saqiii", "Loading")

                }
                is NetworkResponse.Success -> {
                    //use runOnUiThread for Xml
                    val result = response.data
                    Log.d(
                        "saqiii", "fatPercent: ${result?.data?.bodyFatNavyMethod}" +
                                "fatCategory: ${result?.data?.fatCategory}" +
                                "fatMass: ${result?.data?.fatMass}" +
                                "bmi: ${result?.data?.bmi}"
                    )
                }
            }
        }
    }
}