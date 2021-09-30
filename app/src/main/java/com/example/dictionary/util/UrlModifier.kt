package com.example.dictionary.util

object UrlModifier {
    private var myUrl: String = " "

    private val list = listOf(
        "en", "ar", "fr", "es", "cz", "de", "hi", "id", "ir","it", "ja", "ko", "pl", "pt", "ru","tr", "vi",
    )

    val languageList: List<String>
        get() = list

    val url: String
        get() = myUrl


    fun getUrl(text: String,langSource: String,langTarget: String){
        myUrl = "https://translate.astian.org/translate?" +
                "q=${text}" +
                "&source=${langSource}" +
                "&target=${langTarget}" +
                "&format=text"
    }


}
