package com.example.network

import okhttp3.MediaType
import okhttp3.RequestBody

enum class RequestTypes{
    GET,
    POST,
    POST_JSON,
    GET_FILE,
    POST_MULTIPART_FILE
}

data class RequestInput(
    var requestTypes: RequestTypes,
    var urlStr: String,
    var headers: HashMap<String,String> = hashMapOf(),
    var queryParameters: HashMap<String,String> = hashMapOf(),
    var jsonUploadStr: String = ""
){
    internal var jsonUploadData: (String) -> RequestBody = { uploadJsonStr: String -> RequestBody.create(MediaType.parse("application/json"), uploadJsonStr)}
}