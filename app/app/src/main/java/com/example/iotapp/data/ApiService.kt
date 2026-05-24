package com.example.iotapp.data

import com.example.iotapp.model.AuthResponse
import com.example.iotapp.model.LoginRequest
import com.example.iotapp.model.MeasurementRequest
import com.example.iotapp.model.MeasurementResponse
import com.example.iotapp.model.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.iotapp.model.RegisterRfidRequest
import com.example.iotapp.model.RfidTagResponse
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.iotapp.model.UserStatsResponse
import retrofit2.http.DELETE
import com.example.iotapp.model.CreateMeasurementSessionRequest
import com.example.iotapp.model.MeasurementSessionResponse
import com.example.iotapp.model.MeasurementHistoryResponse

interface ApiService {

    @POST("api/measurements")
    fun sendMeasurement(
        @Body request: MeasurementRequest
    ): Call<MeasurementResponse>

    @GET("api/users/stats")
    fun getUsersStats(): Call<List<UserStatsResponse>>

    @DELETE("api/users/{id}")
    fun deleteUser(
        @Path("id") id: Int
    ): Call<Void>

    @POST("api/auth/register")
    fun register(
        @Body request: RegisterRequest
    ): Call<AuthResponse>

    @POST("api/auth/login")
    fun login(
        @Body request: LoginRequest
    ): Call<AuthResponse>

    @POST("api/rfid/register")
    fun registerRfid(
        @Body request: RegisterRfidRequest
    ): Call<RfidTagResponse>

    @GET("api/rfid/user/{userId}")
    fun getRfidTagsByUser(
        @Path("userId") userId: Int
    ): Call<List<RfidTagResponse>>

    @POST("api/rfid/login/{tagUid}")
    fun loginByRfid(
        @Path("tagUid") tagUid: String
    ): Call<AuthResponse>

    @POST("api/measurement-sessions")
    fun createMeasurementSession(
        @Body request: CreateMeasurementSessionRequest
    ): Call<MeasurementSessionResponse>

    @GET("api/measurement-sessions/user/{userId}")
    fun getMeasurementSessionsByUser(
        @Path("userId") userId: Int
    ): Call<List<MeasurementSessionResponse>>

    @GET("api/measurements/session/{sessionId}")
    fun getMeasurementsBySession(
        @Path("sessionId") sessionId: Int
    ): Call<List<MeasurementHistoryResponse>>
}