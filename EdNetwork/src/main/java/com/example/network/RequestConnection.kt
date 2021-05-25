package com.example.network

import com.example.network.factory.IBaseNetwork
import com.example.network.factory.NetworkExceptions
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class RequestConnection: IBaseNetwork {

    private lateinit var retroFiInstance: IRetrofit

    override fun initialize(url: String): Completable {
        return Completable.create { completeEmitter ->
            try {
                val retrofit = Retrofit.Builder().baseUrl(url).addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()
                )
                retroFiInstance = retrofit.build().create(IRetrofit::class.java)
                completeEmitter.onComplete()
            }catch (e: Exception){
                completeEmitter.onError(e)
            }
        }
    }

    override fun <T : Any> connectionRequest(requestInput: RequestInput, dataKSerializer: KSerializer<T>): Observable<T>? {
        return when(requestInput.requestTypes){
            RequestTypes.GET -> retroFiInstance.getInfoRx(requestInput.urlStr,requestInput.headers,requestInput.queryParameters)
            RequestTypes.POST -> retroFiInstance.postInfoCallRx(requestInput.urlStr,requestInput.headers,requestInput.queryParameters)
            RequestTypes.POST_JSON -> retroFiInstance.postInfoJSONRx(requestInput.urlStr,requestInput.headers,requestInput.queryParameters,requestInput.jsonUploadData(requestInput.jsonUploadStr))
            else -> null
        }?.concatMap { responseData ->
            Observable.create { dataModelEmitter ->
                when(responseData.code()){
                    200 -> {
                        try {
                            val responseBody = responseData.body()?.string()?:""
                            val responseDataModel = Json { ignoreUnknownKeys = true }.decodeFromString(dataKSerializer,responseBody)
                            dataModelEmitter.onNext(responseDataModel)
                        }catch (e: Exception){
                            dataModelEmitter.onError(e)
                        }
                    }
                    else -> dataModelEmitter.onError(NetworkExceptions.RequestException("Network Request Connection Failure Code :${responseData.code()} with : ${responseData.errorBody()}"))
                }
            }
        }
    }
}