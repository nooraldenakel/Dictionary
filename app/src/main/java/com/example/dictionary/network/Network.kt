package com.example.dictionary.network

import com.example.dictionary.util.Status
import com.example.dictionary.model.TextTranslated
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

object Network {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val builder = FormBody.Builder()
    var result = " "
    fun makeOkHttpRequest(text: String,langSource: String,langTarget: String): Status<TextTranslated> {
        val request = Request.Builder()
            .url("https://translate.astian.org/translate?" +
                    "q=${text}" +
                    "&source=${langSource}" +
                    "&target=${langTarget}" +
                    "&format=text")
            .post(builder.build())
            .build()
        val response = client.newCall(request).execute()
        return if (response.isSuccessful){
            val myResponse = gson.fromJson(response.body?.string(), TextTranslated::class.java)
            result = myResponse.translatedText.toString()
            Status.Success(myResponse)
        }
        else{
            Status.Error(response.message)
        }

    }
}
