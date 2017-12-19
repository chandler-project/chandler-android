package com.chandlersystem.chandler.data.api;

import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.models.request.DealRequest;
import com.chandlersystem.chandler.data.models.request.LoginRequest;
import com.chandlersystem.chandler.data.models.response.AuthenticationRespone;
import com.chandlersystem.chandler.data.models.retrofit.Deal;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ChandlerApi {
    @Headers({"Content-type: application/json"})
    @POST("/api/Members/login")
    Call<AuthenticationRespone> authentication(@Body LoginRequest request, @Query("force") int force, @Query("include") String include);
    Call<List<Deal>> getDeals(@Body DealRequest request);
}
