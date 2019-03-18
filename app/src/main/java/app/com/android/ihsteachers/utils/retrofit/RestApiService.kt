package app.com.android.ihsteachers.utils.retrofit

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.http.GET



interface RestApiService {

    @FormUrlEncoded
    @POST("/downloadFiles")
    fun registerUser(@Field ("firstName") fname: String,
                     @Field ("lastName") lname: String,
                     @Field ("emailAddress") emailAdd: String,
                     @Field ("mobileNo") mobile: String,
                     @Field ("username") uname: String,
                     @Field ("password") pword: String,
                     @Field ("userid") uid: Int) : Call<JsonObject>

    @GET("/validate")
    fun validateUser(@Query ("userName") uname: String,
                     @Query  ("userpass") upass: String) : Call<JsonObject>

    @POST("/add-transctions")
    fun makeTransaction(@Query ("transcode") tCode: Int,
                        @Query ("fullname") fullName: String,
                        @Query ("emailAddress") emailAdd: String,
                        @Query ("mobileNo") mobile: String,
                        @Query ("serviceType") sType: String,
                        @Query ("serviceProvider") sProvider: String,
                        @Query ("serviceIdNo") sNo: String,
                        @Query ("cost") cost: Double,
                        @Query ("bankName") bank: String,
                        @Query ("commChannel") comm: String,
                        @Query ("userid") uid: Int,
                        @Query ("username") uname: String,
                        @Query ("createdAt") created: String,
                        @Query ("tStatus") tranStatus: Int) : Call<JsonObject>

    @GET("/transaction-details")
    fun retrieveTransactions(@Query ("transacId") id: Int) : Call<JsonObject>

    @GET("/track-transactions")
    fun retrieveTrackTransactions() : Call<JsonObject>

    @Streaming
    @GET
    fun downloadFileWithDynamicUrlSync(@Url fileUrl: String): Call<ResponseBody>

}