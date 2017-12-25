package com.chandlersystem.chandler.data.api;

import com.chandlersystem.chandler.data.models.request.DealRequest;
import com.chandlersystem.chandler.data.models.request.LoginRequest;
import com.chandlersystem.chandler.data.models.response.AuthenticationRespone;
import com.chandlersystem.chandler.data.models.response.RetrofitResponseItem;
import com.chandlersystem.chandler.data.models.retrofit.Deal;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChandlerApi {
    @POST("/api/Members/login")
    Observable<RetrofitResponseItem<AuthenticationRespone>> authentication(@Body LoginRequest request, @Query("force") int force, @Query("include") String include);

    Call<List<Deal>> getDeals(@Body DealRequest request);
}
