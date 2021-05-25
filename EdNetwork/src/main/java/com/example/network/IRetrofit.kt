package com.example.network

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface IRetrofit {

    @GET//GET_INFO
    fun getInfoRx(@Url urlString: String, @HeaderMap headersMap: HashMap<String,String>, @QueryMap queryMap: HashMap<String,String>): Observable<Response<ResponseBody>>

    @Streaming
    @GET//FILE_DOWNLOAD
    fun downloadFileFromUrlRx(@Url urlString: String): Observable<Response<ResponseBody>>

    @POST//POST_INFO
    fun postInfoCallRx(@Url urlString: String, @HeaderMap headersMap: HashMap<String,String>, @QueryMap queryMap: HashMap<String,String>): Observable<Response<ResponseBody>>

    @POST//POST_JSON_DATA
    fun postInfoJSONRx(@Url urlString: String, @HeaderMap headersMap: HashMap<String,String>, @QueryMap queryMap: HashMap<String,String>, @Body uploadData: RequestBody?): Observable<Response<ResponseBody>>

    @Multipart
    @POST//FILE_MULTIPLE_UPLOAD
    fun uploadMultipleFileRx(@Url urlString: String, @HeaderMap headersMap: HashMap<String,String>, @Part requestBody: RequestBody?, @Part file: List<MultipartBody.Part?>): Observable<Response<ResponseBody>>
}