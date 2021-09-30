package com.example.dictionary.model


import com.google.gson.annotations.SerializedName

data class TextTranslated(
    @SerializedName("translatedText")
    val translatedText: String?
)
