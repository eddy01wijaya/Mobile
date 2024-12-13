package com.eddy01wijaya.bcafhive.dao

import com.eddy01wijaya.bcafhive.model.LoginResponse
import com.eddy01wijaya.bcafhive.model.Recruitment
import com.eddy01wijaya.bcafhive.model.RecruitmentResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RecruitmentDao {
    @GET("recruitment/0/desc/tanggaltutup?size=100&col=&val=")
    fun getRecruitments(): Call<RecruitmentResponse>

    @GET("recruitment/0/desc/tanggaltutup?size=100&col=persetujuan&val=P")
    fun getRecruitmentsPending(): Call<RecruitmentResponse>

    @GET("recruitment/0/desc/tanggaltutup?size=100&col=persetujuan&val=A")
    fun getRecruitmentsApproved(): Call<RecruitmentResponse>

    @GET("recruitment/0/desc/tanggaltutup?size=100&col=persetujuan&val=R")
    fun getRecruitmentsRejected(): Call<RecruitmentResponse>

    @PUT("recruitment/{id}")
    fun updateRecruitment(
        @Path("id") id: Int,
        @Body updatedRecruitment: Map<String, String> // Send the request body as Map
    ): Call<Void>

    @POST("auth/login")
    suspend fun login(@Body credentials: Map<String, String>): Response<LoginResponse>
}
