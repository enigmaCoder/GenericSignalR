package com.example.network.factory

import com.example.network.RemoteConnection
import com.example.network.RequestConnection

class NetworkApi: INetworkApi {
    override fun buildNetworkAdapter(networkTypes: NetworkTypes): IBaseNetwork? {
        return when(networkTypes){
            NetworkTypes.REMOTE -> RemoteConnection()
            NetworkTypes.REQUEST -> RequestConnection()
            else -> null
        }
    }
}