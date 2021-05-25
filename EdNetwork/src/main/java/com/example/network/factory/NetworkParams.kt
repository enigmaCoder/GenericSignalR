package com.example.network.factory

import java.lang.Exception

enum class NetworkTypes {
    REMOTE,
    MSAL,
    REQUEST
}

class NetworkExceptions{

    class ConnectionException(message: String): Exception(message)

    class RequestException(message: String): Exception(message)
}