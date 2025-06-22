package com.example.fitnesscentarchat.data.api

import com.example.fitnesscentarchat.data.models.*
import okhttp3.MultipartBody
import retrofit2.Response

import retrofit2.http.*

interface ChatApiService {

    @POST("Account/login")
    suspend fun login(@Body authRequest: AuthRequest): AuthResponse

    @POST("Account/Register")
    suspend fun register(@Body user: AuthRequest): AuthResponse

    @GET("users")
    suspend fun getUsers(): List<User>
    @POST("Account/UpdateUser")
    suspend fun updateUser(
        @Body user: User
    ): Response<Any>



    @GET("Account/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): User


    @GET("FitnessCenter/{fitnessCenterId}")
    suspend fun getFitnessCenter(@Path("fitnessCenterId") fitnessCenterId: Int) : FitnessCenter

    @GET("FitnessCenter")
    suspend fun getFitnessCenters() : List<FitnessCenter>

    @GET("Membership/user")
    suspend fun getCurrentUserMemberships(): List<Membership>

    @GET("Membership/user/{fitnessCenterId}")
    suspend fun getUserMembershipByFitnessCenter(@Path("fitnessCenterId") fitnessCenterId: Int): Membership

    @POST("Membership/UpdateMembership")
    suspend fun addMembership(
        @Body membership: Membership
    ): Response<Any>

    @GET("Attendance/users")
    suspend fun getCurrentUserAttendances(): List<Attendance>

    @GET("Attendance/users/{fitnessCenterId}")
    suspend fun getCurrentUserFitnessCenterAttendances(@Path("fitnessCenterId") fitnessCenterId: Int): List<Attendance>

    @GET("Attendance/fitnesscenters/recent/{fitnessCenterId}")
    suspend fun getRecentAttendees(@Path("fitnessCenterId") fitnessCenterId: Int): List<Attendance>

    @POST("Attendance/AddAttendance")
    suspend fun addAttendance(
        @Body attendance: Attendance
    ): Response<Any>


    @GET("Shop/items/{fitnessCenterId}")
    suspend fun getFitnessCenterItems(@Path("fitnessCenterId") fitnessCenterId: Int): List<ShopItem>

    @GET("Shop/items/users")
    suspend fun getUserItems(): List<ShopItem>

    @GET("Shop/item/{shopItemId}")
    suspend fun getShopItem(@Path("shopItemId") shopItemId: Int): ShopItem

    @POST("Shop/BuyShopItem/{shopItemId}")
    suspend fun buyShopItem(
        @Path("recipientId") shopItemId: Int,
    ): Response<Any>

    @GET("Conversation/{conversationId}")
    suspend fun getMessagesByConversation(@Path("conversationId") conversationId: Int): List<Message>


    @POST("Conversation/message/send/{recipientId}")
    suspend fun sendMessage(
        @Path("recipientId") recipientId: Int,
        @Body message: Message
    ): Response<Any>

    @GET("Conversation/chats")
    suspend fun getUsersConversations(): List<Conversation>


}