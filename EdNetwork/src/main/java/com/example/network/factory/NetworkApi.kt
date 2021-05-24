package com.example.network.factory

import com.example.network.RemoteConnection

class NetworkApi: INetworkApi {
    override fun buildNetworkAdapter(networkTypes: NetworkTypes): IBaseNetwork? {
        return when(networkTypes){
            NetworkTypes.REMOTE -> RemoteConnection()
            else -> null
        }
    }
}