package com.yousef.sh.chatapplication.API;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface ApiService {
    @POST("send")
    Call<String> sendRemoteMessage(
            @HeaderMap HashMap<String,String> headres,
            @Body String remoteBody
            );

}
