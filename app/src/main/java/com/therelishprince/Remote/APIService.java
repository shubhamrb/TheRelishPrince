package com.therelishprince.Remote;



import com.therelishprince.model.MyResponse;
import com.therelishprince.model.Sender;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA2nf5tVQ:APA91bFNc1K1ZTs6L1oeX1ONFKbPsHbSlGnZOkdUbJgfiEWXLoBlQDcFOXieCL0XNNmV7YtjUpBovsyq5b_hUzTkgC3yTGiUyNGoMvycij5s38KbCm04s63-zkp9IYTPIidJGFWvsr0j"
            }
    )
    @POST("fcm/send")
    retrofit2.Call<MyResponse>sendNotification(@Body Sender body);


}
