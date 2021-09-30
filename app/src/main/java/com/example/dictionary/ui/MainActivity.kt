package com.example.dictionary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.dictionary.databinding.ActivityMainBinding
import com.example.dictionary.model.TextTranslated
import com.example.dictionary.network.Network
import com.example.dictionary.util.Status
import com.example.dictionary.util.UrlModifier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var languagesSource = "en"
    private var languagesTarget = "en"
    @FlowPreview
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSpinner()
        dataflow()
    }
    @SuppressLint("WrongConstant")
    @FlowPreview
    @InternalCoroutinesApi
    fun dataflow(){
        binding.textTransFrom.doOnTextChanged { text, _, _, _ ->
        val flow = flow {
            emit(Network.makeOkHttpRequest(text.toString(),languagesSource,languagesTarget))}
            .flowOn(Dispatchers.IO)
            .debounce(1500)

            lifecycleScope.launch {
                flow.collect{
                    onTextResponse(it)
                }
            }
    }}
    private fun onTextResponse(response: Status<TextTranslated>) {
        when(response){
            is Status.Error->{Log.i(TAG,response.message)}
            is Status.Loading -> {}
            is Status.Success ->{ emittingTheData(response.data) }
        }
    }

    private fun setSpinner(){
        binding.langSourceSpinner.setOnSpinnerItemSelectedListener { _, _, i, _: String ->
            languagesSource = UrlModifier.languageList[i]
        }
        binding.langTargetSpinner.setOnSpinnerItemSelectedListener { _, _, i, _: String ->
            languagesTarget = UrlModifier.languageList[i]
        }
    }

    @SuppressLint("WrongConstant")
    fun emittingTheData(data: TextTranslated){
        if (languagesTarget == "ar"){
            binding.textTransTo.textAlignment = 6
            binding.textTransTo.text = data.translatedText
            binding.textTransTo.textAlignment = 6
        }else{
            binding.textTransTo.textAlignment = 5
            binding.textTransTo.text = data.translatedText
            binding.textTransTo.textAlignment = 5
        }
    }

    @SuppressLint("WrongConstant")
    private fun chickLangSame(data: TextTranslated){
        if (languagesSource == languagesTarget){
            Toast.makeText(applicationContext,"Change the Target Language" , Toast.LENGTH_SHORT).show()
        }
        else{ }
    }

    companion object{
        const val TAG = "NoorAlden"
    }
}
