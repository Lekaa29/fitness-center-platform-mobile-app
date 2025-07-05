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


    @GET("FitnessCenter/{fitnessCentarId}")
    suspend fun getFitnessCenter(@Path("fitnessCentarId") fitnessCentarId: Int) : FitnessCenter

    @GET("FitnessCenter")
    suspend fun getFitnessCenters() : List<FitnessCenter>

    @GET("FitnessCenter/PromoFitnessCentars")
    suspend fun getPromoFitnessCenters() : List<FitnessCenter>
    @GET("FitnessCenter/coaches/{fitnessCentarId}")
    suspend fun getFitnessCentarsCoaches(@Path("fitnessCentarId") fitnessCentarId: Int): List<Coach>

    @GET("FitnessCenter/ClosestFitnessCentars")
    suspend fun getClosestFitnessCentars(
        @Query("userLat") userLat: Double,
        @Query("userLng") userLng: Double
    ): List<FitnessCenter>

    @GET("Membership/user")
    suspend fun getCurrentUserMemberships(): List<MembershipModel>

    @GET("Membership/user/{fitnessCentarId}")
    suspend fun getUserMembershipByFitnessCenter(@Path("fitnessCentarId") fitnessCentarId: Int): MembershipModel

    @GET("Membership/MembershipPackages/{fitnessCentarId}")
    suspend fun getFitnessCenterMembershipPackages(@Path("fitnessCentarId") fitnessCentarId: Int): List<MembershipPackage>

    @GET("Membership/FitnessCenter/Leaderboard/{fitnessCentarId}")
    suspend fun getFitnessCenterLeaderboard(@Path("fitnessCentarId") fitnessCentarId: Int): List<MembershipModel>



    @GET("Article/articles/{fitnessCentarId}")
    suspend fun getFitnessCenterArticles(@Path("fitnessCentarId") fitnessCentarId: Int): List<Article>




    @POST("Membership/UpdateMembership")
    suspend fun addMembership(
        @Body membership: MembershipModel
    ): Response<Any>

    @GET("Attendance/users")
    suspend fun getCurrentUserAttendances(): List<Attendance>

    @GET("Attendance/fitnesscenters/{fitnessCentarId}")
    suspend fun getAllAttendences(@Path("fitnessCentarId") fitnessCentarId: Int): List<Attendance>
    @GET("Attendance/fitnesscenters/leaving/{fitnessCentarId}")
    suspend fun getLeavingAttendees(@Path("fitnessCentarId") fitnessCentarId: Int): Int

    @GET("Attendance/users/{fitnessCentarId}")
    suspend fun getCurrentUserFitnessCenterAttendances(@Path("fitnessCentarId") fitnessCentarId: Int): List<Attendance>

    @GET("Attendance/fitnesscenters/recent/{fitnessCentarId}")
    suspend fun getRecentAttendees(@Path("fitnessCentarId") fitnessCentarId: Int): Int

    @POST("Attendance/AddAttendance")
    suspend fun addAttendance(
        @Body attendance: Attendance
    ): Response<Any>


    @GET("Shop/items/{fitnessCentarId}")
    suspend fun getFitnessCenterItems(@Path("fitnessCentarId") fitnessCentarId: Int): List<ShopItem>

    @GET("Shop/items/users")
    suspend fun getUserItems(): List<ShopItem>

    @GET("Shop/item/{shopItemId}")
    suspend fun getShopItem(@Path("shopItemId") shopItemId: Int): ShopItem

    @POST("Shop/BuyShopItem/{shopItemId}")
    suspend fun buyShopItem(
        @Path("shopItemId") shopItemId: Int,
    ): Response<Any>

    @GET("Conversation/{conversationId}")
    suspend fun getMessagesByConversation(@Path("conversationId") conversationId: Int): List<Message>

    @GET("Conversation/{conversationId}/participants")
    suspend fun getConversationParticipants(@Path("conversationId") conversationId: Int): List<User>

    @GET("Conversation/id/{userId}")
    suspend fun getConversationIdByRecepient(@Path("userId") userId: Int): Int

    @POST("Conversation/message/send/{recipientId}")
    suspend fun sendMessage(
        @Path("recipientId") recipientId: Int,
        @Body message: Message
    ): Response<Any>

    @GET("Conversation/chats")
    suspend fun getUsersConversations(): List<Conversation>


}