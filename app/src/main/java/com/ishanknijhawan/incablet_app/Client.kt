package com.ishanknijhawan.incablet_app

import okhttp3.OkHttpClient
import okhttp3.Request

object Client {
    private val okHttpClient = OkHttpClient()
    private val request = Request.Builder()
        .url("https://5w05g4ddb1.execute-api.ap-south-1.amazonaws.com/dev/profile/listAll")
        .build()

    val api = okHttpClient.newCall(request)
}