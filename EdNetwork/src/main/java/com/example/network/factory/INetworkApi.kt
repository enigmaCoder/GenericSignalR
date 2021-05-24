package com.example.network.factory

interface INetworkApi {

    fun buildNetworkAdapter(networkTypes: NetworkTypes): IBaseNetwork?
}