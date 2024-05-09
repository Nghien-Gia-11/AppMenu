package com.example.admin.api

import android.os.Build
import androidx.annotation.BoolRes
import androidx.annotation.RequiresApi
import com.example.admin.model.Bill
import com.example.admin.model.DetailBill
import com.example.admin.model.Food
import com.example.admin.model.FoodInRate
import com.example.admin.model.IdFood
import com.example.admin.model.Rate
import com.example.admin.model.RevenueDay
import com.example.admin.model.RevenueMonth
import com.example.admin.model.TimesStar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface ApiService {
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        private val gson: Gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
                LocalDateTime.parse(json.asString, DateTimeFormatter.RFC_1123_DATE_TIME)
            })
            .create()
        fun create() : ApiService{
            var retrofit = Retrofit.Builder()
                .baseUrl("http://172.16.0.2:3333")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

//  các phương thức GET POST Api
    @GET("/bill/getAll")
    fun getAllBill() : Call<MutableList<Bill>>

    @GET("/detailBill/getById/{idBill}")
    fun searchDetailBillByIdBill(@Path("idBill") idBill : String) : Call<MutableList<Bill>>

    @GET("/detailBill/{idBill}")
    fun getDetailBill(@Path("idBill") idBill : String) : Call<MutableList<DetailBill>>

    @GET("/rate/getAllFoodInRate")
    fun getAllFoodInRate() : Call<MutableList<FoodInRate>>

    @GET("/rate/getRateByFood/{idFood}")
    fun getRateByFood(@Path("idFood") idFood : String) : Call<MutableList<Rate>>

    @GET("/rate/getAll")
    fun getAllRate() : Call<MutableList<Rate>>

    @GET("/rate/getTimes")
    fun getTimesStar() : Call<MutableList<TimesStar>>

    @GET("/rate/getRateByStar/{star}")
    fun getRateByStar(@Path("star") star : Int) : Call<MutableList<Rate>>

    @GET("/food/getAll")
    fun getAllFood() : Call<MutableList<Food>>

    @GET("/food/getIdFood")
    fun getIdFood() : Call<MutableList<IdFood>>

    @PUT("/food/updateFood/{idFood}")
    fun updateFoodById(
        @Path("idFood") idFood : String,
        @Body food : Food
    ) : Call<Void>

    @POST("/food/insertFood")
    fun insertFood(
        @Body food : Food
    ) : Call<Void>

    @GET("/revenue/byDay/{years}/{months}/{dayFirst}/{daySecond}")
    fun getRevenueByDay(
        @Path("years") years : Int,
        @Path("months") months : Int,
        @Path("dayFirst") dayFirst : Int,
        @Path("daySecond") daySecond : Int,
    ) : Call<MutableList<RevenueDay>>

    @GET("/revenue/byMonth/{years}")
    fun getRevenueByMonth(@Path("years") years : Int) : Call<MutableList<RevenueMonth>>

}