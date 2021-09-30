package com.example.dictionary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.dictionary.databinding.ActivityMainBinding
import com.example.dictionary.model.TextTranslated
import com.example.dictionary.network.Network
import com.example.dictionary.util.Status
import com.example.dictionary.util.UrlModifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var languagesSource = "en"
    private var languagesTarget = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSpinner()
        dataflow()
    }


    private fun dataflow() {
        binding.textTransFrom.doOnTextChanged { text, _, _, _ ->
            val flow = flow {
                emit(Status.Loading)
                UrlModifier.getUrl(text.toString(), languagesSource, languagesTarget)
                emit(Network.makeOkHttpRequest())

            }.flowOn(Dispatchers.IO).debounce(1500)

            lifecycleScope.launch {
                flow.catch {
                    Log.i(TAG, "filer: ${it.message}")
                }.collect {
                    onTextResponse(it)
                }
            }
        }
    }
    private fun onTextResponse(response: Status<TextTranslated>) {
        binding.imageError.hide()
        when (response) {
            is Status.Error -> {
                Log.i(TAG,response.message)
            }
            is Status.Loading -> { Log.i(TAG,"----Loading---")}
            is Status.Success -> {
                emittingTheData(response.data)
            }
        }
    }

    @SuppressLint("WrongConstant")
    fun emittingTheData(data: TextTranslated) = when {
        languagesSource == languagesTarget -> {
            Toast.makeText(applicationContext, "Change the Target Language", Toast.LENGTH_SHORT)
                .show()
        }
        languagesTarget == "ar" -> {
            binding.textTransTo.textAlignment = 6
            binding.textTransTo.text = data.translatedText
            binding.textTransTo.textAlignment = 6
        }
        else -> {
            binding.textTransTo.textAlignment = 5
            binding.textTransTo.text = data.translatedText
            binding.textTransTo.textAlignment = 5
        }
    }

    private fun setSpinner() {
        binding.langSourceSpinner.setOnSpinnerItemSelectedListener { _, _, i, _: String ->
            languagesSource = UrlModifier.languageList[i]
        }
        binding.langTargetSpinner.setOnSpinnerItemSelectedListener { _, _, i, _: String ->
            languagesTarget = UrlModifier.languageList[i]
        }
    }
    private fun View.hide() {
        this.visibility = View.GONE
    }

    private fun View.show() {
        this.visibility = View.VISIBLE
    }

    companion object {
        const val TAG = "NoorAlden"
    }
}
