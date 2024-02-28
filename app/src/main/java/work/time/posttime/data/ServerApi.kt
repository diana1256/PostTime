package work.time.worktim.data

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import work.time.worktim.data.model.Auth
import work.time.worktim.data.model.Coordin
import work.time.worktim.data.model.DataReestr
import work.time.worktim.data.model.Qery
import work.time.worktim.data.model.ResultData

interface ServerApi {
    @FormUrlEncoded
    @POST("/api/V1/auth/login")
    fun login(
        @Field("login") email: String
    ): Call<Auth>

    @GET("/api/V1/user-data")
     fun getUserData(@Header("Authorization") token: String): Call<ResultData>

    @Multipart
    @POST("/api/V1/came-gone")
    fun postCameGone(
        @Part("token") tokenMB: RequestBody,
        @Header("Authorization") authorization: String
    ): Call<String>

    @Multipart
    @POST("/api/V1/remote")
    fun postRemote(
        @Part("token") tokenMB: RequestBody,
        @Header("Authorization") authorization: String
    ): Call<String>

    @GET("/api/V1/reestr")
    fun getReestr(@Header("Authorization") token: String): Call<List<DataReestr>>

    @GET("/api/V1/reestr")
    fun getReestrData(@Header("Authorization") token: String): Call<List<DataReestr>>

    @GET("/api/V1/reestr")
    fun getRees(
        @Header("Authorization") token: String,
        @Query("paginate") paginate: Int = 5,
        @Query("dateStart") dateStart: String,
        @Query("dateEnd") dateEnd: String
    ): Call<List<Qery>>

    @GET("/api/V1/qrcode")
    fun getQRCode(@Header("Authorization") token: String): Call<String>

    @GET("/api/V1/get-coordinates")
     fun getCoordinates(@Header("Authorization")token: String): Call<Coordin>

}