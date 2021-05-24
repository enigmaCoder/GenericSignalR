package com.example.network.factory

import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.serialization.KSerializer

interface IBaseNetwork {

    fun initialize(url: String): Completable

    fun connectOrDisconnect(): Completable

    fun isConnected(): Boolean

    fun<T:Any> receiveEvents(targetName: String, dataSerializer: KSerializer<T>): Observable<T>

    fun<T:Any> sendEvents(methodName: String, data: T, dataSerializer: KSerializer<T>): Completable
}