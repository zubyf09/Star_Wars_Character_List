package com.trivago.challenge.characters.model

import com.google.gson.annotations.SerializedName

data class HomeworldResponseModel(
        @SerializedName("name") val name: String? = "",
        @SerializedName("population") val population: String? = "")
