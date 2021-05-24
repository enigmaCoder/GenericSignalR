package com.example.network

import com.example.network.factory.IBaseNetwork
import com.example.network.factory.NetworkExceptions
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.lang.Exception

class RemoteConnection: IBaseNetwork {

    private lateinit var hubConnection: HubConnection

    override fun initialize(url: String): Completable {
        return Completable.create { completeEmitter ->
            try {
                hubConnection = HubConnectionBuilder.create(url).build()
                completeEmitter.onComplete()
            }catch (e: Exception){
                completeEmitter.onError(e)
            }
        }
    }

    override fun connectOrDisconnect(): Completable {
        return when{
            this::hubConnection.isInitialized && hubConnection.connectionState == HubConnectionState.DISCONNECTED -> {
                hubConnection.start()
            }
            this::hubConnection.isInitialized && hubConnection.connectionState == HubConnectionState.CONNECTED -> {
                hubConnection.stop()
            }
            else -> Completable.error(NetworkExceptions.ConnectionException("Connection request could not be completed"))
        }
    }

    override fun isConnected(): Boolean {
        return when{
            this::hubConnection.isInitialized && hubConnection.connectionState == HubConnectionState.DISCONNECTED -> {
                false
            }
            this::hubConnection.isInitialized && hubConnection.connectionState == HubConnectionState.CONNECTED -> {
                true
            }
            else -> false
        }
    }

    override fun <T : Any> receiveEvents(targetName: String, dataSerializer: KSerializer<T>): Observable<T> {
        return Observable.create { dataEmitter ->
            hubConnection.on(targetName, { jsonData: String ->
                try {
                    val dataObj = Json { ignoreUnknownKeys = true }.decodeFromString(dataSerializer,jsonData)
                    dataEmitter.onNext(dataObj)
                }catch (e: Exception){
                    dataEmitter.onError(e)
                }
            }, String::class.java)
        }
    }

    override fun <T : Any> sendEvents(methodName: String, data: T, dataSerializer: KSerializer<T>): Completable {
        return Completable.create { completeEmitter ->
            try {
                if(this::hubConnection.isInitialized && hubConnection.connectionState == HubConnectionState.CONNECTED) {
                    val dataStr = Json { ignoreUnknownKeys = true }.encodeToString(dataSerializer, data)
                    hubConnection.send(methodName, dataStr)
                }else{
                    completeEmitter.onError(NetworkExceptions.ConnectionException("Remote Disconnected"))
                }
                completeEmitter.onComplete()
            }catch (e: Exception){
                completeEmitter.onError(e)
            }
        }
    }
}