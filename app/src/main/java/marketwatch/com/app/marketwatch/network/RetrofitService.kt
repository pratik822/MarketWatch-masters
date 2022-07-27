package com.pratik.marketwatchadmin.network

import com.pratik.marketwatchadmin.data.Req
import com.pratik.marketwatchadmin.data.ResponseData
import marketwatch.com.app.marketwatch.data.AddPostData
import marketwatch.com.app.marketwatch.data.ChartResponseData
import marketwatch.com.app.marketwatch.data.RespNotification
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface RetrofitService {
    @GET("getcreateData.php")
    suspend fun getPost():Response<ResponseData>

    @Headers("Accept: application/json")
    @POST("createData.php")
    fun createPost(@Body addPostData: AddPostData):Call<Req>


    @GET("getcreateDataNotification.php")
    suspend fun getPostNotification():Response<RespNotification>

    @GET("getchart.php")
    suspend fun getChartDetails():Response<ChartResponseData>


//    @Headers("Content-Type: application/x-www-form-urlencoded")
//    @FormUrlEncoded
//    @POST("createData.php")
//    fun createPost(@Field("storename")storename:String ,@Field("buyprice")buyprice:String,@Field("stoploss")stoploss:String,@Field("target1")target1:String,@Field("target2")target2:String,@Field("target3")target3:String,@Field("type")type:String): Call<ResponseDataItem>?
}