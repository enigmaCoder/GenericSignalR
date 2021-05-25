package com.example.network.factory

import com.example.network.RequestInput
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.serialization.KSerializer

interface IBaseNetwork {

    fun initialize(url: String): Completable

    fun connectOrDisconnect(): Completable{
        return Completable.complete()
    }

    fun isConnected(): Boolean{
        return false
    }

    fun<T:Any> receiveEvents(targetName: String, dataSerializer: KSerializer<T>): Observable<T>{
        return Observable.empty()
    }

    fun<T:Any> sendEvents(methodName: String, data: T, dataSerializer: KSerializer<T>): Completable{
        return Completable.complete()
    }

    fun<T:Any> connectionRequest(requestInput: RequestInput, dataKSerializer: KSerializer<T>): Observable<T>?{
        return null
    }
}