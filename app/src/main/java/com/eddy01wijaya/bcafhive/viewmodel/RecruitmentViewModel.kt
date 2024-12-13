package com.eddy01wijaya.bcafhive.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eddy01wijaya.bcafhive.dao.RecruitmentDao
import com.eddy01wijaya.bcafhive.model.Recruitment
import com.eddy01wijaya.bcafhive.model.RecruitmentResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecruitmentViewModel(application: Application) : AndroidViewModel(application) {

    private val _recruitments = MutableLiveData<List<Recruitment>>()
    val recruitments: LiveData<List<Recruitment>> get() = _recruitments
    private val sharedPreferences = application.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    private val recruitmentDao: RecruitmentDao = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecruitmentDao::class.java)

    fun fetchRecruitments() {
        recruitmentDao.getRecruitments().enqueue(object : Callback<RecruitmentResponse> {
            override fun onResponse(
                call: Call<RecruitmentResponse>,
                response: Response<RecruitmentResponse>
            ) {
                if (response.isSuccessful) {
                    _recruitments.value = response.body()?.content
                    Log.d("RecruitmentViewModel", "Data fetched: ${response.body()?.content}")
                } else {
                    Log.e("RecruitmentViewModel", "Failed response: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RecruitmentResponse>, t: Throwable) {
                Log.e("RecruitmentViewModel", "Error: ${t.message}", t)
                _recruitments.value = emptyList()
            }
        })
    }

    fun fetchRecruitmentsPending() {
        recruitmentDao.getRecruitmentsPending().enqueue(object : Callback<RecruitmentResponse> {
            override fun onResponse(
                call: Call<RecruitmentResponse>,
                response: Response<RecruitmentResponse>
            ) {
                if (response.isSuccessful) {
                    _recruitments.value = response.body()?.content
                    Log.d("RecruitmentViewModel", "Data fetched: ${response.body()?.content}")
                } else {
                    Log.e("RecruitmentViewModel", "Failed response: ${response.code()} - ${response.message()}")
                    _recruitments.value = emptyList()
                }
            }

            override fun onFailure(call: Call<RecruitmentResponse>, t: Throwable) {
                Log.e("RecruitmentViewModel", "Error: ${t.message}", t)
                _recruitments.value = emptyList()
            }
        })
    }

    fun fetchRecruitmentsApproved() {
        recruitmentDao.getRecruitmentsApproved().enqueue(object : Callback<RecruitmentResponse> {
            override fun onResponse(
                call: Call<RecruitmentResponse>,
                response: Response<RecruitmentResponse>
            ) {
                if (response.isSuccessful) {
                    _recruitments.value = response.body()?.content
                    Log.d("RecruitmentViewModel", "Data fetched: ${response.body()?.content}")
                } else {
                    Log.e("RecruitmentViewModel", "Failed response: ${response.code()} - ${response.message()}")
                    _recruitments.value = emptyList()
                }
            }

            override fun onFailure(call: Call<RecruitmentResponse>, t: Throwable) {
                Log.e("RecruitmentViewModel", "Error: ${t.message}", t)
                _recruitments.value = emptyList()
            }
        })
    }

    fun fetchRecruitmentsRejected() {
        recruitmentDao.getRecruitmentsRejected().enqueue(object : Callback<RecruitmentResponse> {
            override fun onResponse(
                call: Call<RecruitmentResponse>,
                response: Response<RecruitmentResponse>
            ) {
                if (response.isSuccessful) {
                    _recruitments.value = response.body()?.content
                    Log.d("RecruitmentViewModel", "Data fetched: ${response.body()?.content}")
                } else {
                    Log.e("RecruitmentViewModel", "Failed response: ${response.code()} - ${response.message()}")
                    _recruitments.value = emptyList()
                }
            }

            override fun onFailure(call: Call<RecruitmentResponse>, t: Throwable) {
                Log.e("RecruitmentViewModel", "Error: ${t.message}", t)
                _recruitments.value = emptyList()
            }
        })
    }

    fun updateRecruitment(
        recruitmentId: Int,
        updatedRecruitment: Map<String, String>, // Pastikan map menggunakan tipe yang sesuai
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        recruitmentDao.updateRecruitment(recruitmentId, updatedRecruitment).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("UpdateRecruitment", "Update successful for recruitmentId: $recruitmentId")
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UpdateRecruitment", "Failed to update recruitment. Code: ${response.code()}, Error: $errorBody")
                    onError(Exception("Failed to update recruitment: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UpdateRecruitment", "Error updating recruitment: ${t.message}", t)
                onError(t)
            }
        })
    }


    fun convertToRequestBody(recruitment: Recruitment): Map<String, String> {
        return mapOf(
            "recruitment-name" to recruitment.recruitmentName,
            "requirements" to recruitment.requirements,
            "open-date" to recruitment.openDate,
            "close-date" to recruitment.closeDate,
            "approval" to recruitment.approval
        )
    }

    fun login(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val credentials = mapOf("username" to username, "password" to password)
                val response = recruitmentDao.login(credentials)

                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        saveToken(token)
                        onSuccess()
                    } else {
                        onError("Token not found")
                    }
                } else {
                    onError("Login failed: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }

    private fun saveToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }




}

