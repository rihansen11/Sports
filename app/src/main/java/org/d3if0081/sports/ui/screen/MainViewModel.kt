package org.d3if0081.sports.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if0081.sports.model.Sports
import org.d3if0081.sports.network.ApiStatus
import org.d3if0081.sports.network.SportsApi
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {
    var data = mutableStateOf(emptyList<Sports>())
        private set
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO){
            status.value = ApiStatus.LOADING
            try {
                data.value = SportsApi.service.getSports(userId = userId)
                status.value = ApiStatus.SUCCESS
                Log.d("MainViewModel", "${data.value}")
            }catch (e:Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, nama: String, posisi: String, bitmap: Bitmap){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = SportsApi.service.postSports(
                    userId = userId,
                    nama = nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    posisi = posisi.toRequestBody("text/plain".toMediaTypeOrNull()),
                    image = bitmap.toMultipartBody()
                )
                if (result.status == "success")
                    retrieveData(userId = userId)
                else
                    throw Exception(result.message)
            }catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deletingData(userId: String,id: Long){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = SportsApi.service.deleteSports(userId = userId,id = id.toString())
                if (result.status == "success")
                    retrieveData(userId = userId)
                else
                    throw Exception(result.message)
            }catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part{
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG,80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(),0, byteArray.size
        )
        return MultipartBody.Part.createFormData(
            "image","image.jpg",requestBody
        )
    }

    fun clearMessage(){
        errorMessage.value = null
    }
}