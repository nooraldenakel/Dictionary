package com.example.dictionary.network

import android.util.Log
import com.example.dictionary.util.Status
import com.example.dictionary.model.TextTranslated
import com.example.dictionary.util.UrlModifier
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

object Network {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val builder = FormBody.Builder()
    fun makeOkHttpRequest(): Status<TextTranslated> {
        val request = Request.Builder()
            .url(UrlModifier.url)
            .post(builder.build())
            .build()
        val response = client.newCall(request).execute()
        return if (response.isSuccessful){
            val myResponse = gson.fromJson(response.body?.string(), TextTranslated::class.java)
            Log.i("MAY_TAG",myResponse.translatedText.toString())
            Status.Success(myResponse)
        }
        else{
            Status.Error(response.message)
        }

    }
}
