package com.chandlersystem.chandler.data.api;

import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.data.models.pojo.UploadImage;
import com.chandlersystem.chandler.data.models.request.CreateDealRequest;
import com.chandlersystem.chandler.data.models.request.DealRequest;
import com.chandlersystem.chandler.data.models.request.LoginRequest;
import com.chandlersystem.chandler.data.models.request.SelectCategoryRequest;
import com.chandlersystem.chandler.data.models.response.AuthenticationRespone;
import com.chandlersystem.chandler.data.models.response.RetrofitResponseItem;
import com.chandlersystem.chandler.data.models.response.RetrofitResponseListItem;
import com.chandlersystem.chandler.data.models.retrofit.Deal;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChandlerApi {
    @POST("/api/Members/login")
    Observable<RetrofitResponseItem<AuthenticationRespone>> authentication(@Body LoginRequest request, @Query("force") int force, @Query("include") String include);

    @GET("/api/Categories")
    Observable<RetrofitResponseListItem<Category>> getCategoryList();

    Call<List<Deal>> getDeals(@Body DealRequest request);

    @POST("/api/Members/logout")
    Observable<RetrofitResponseItem> logout(@Query("access_token") String accessToken);

    @POST("api/Members/categories/choose")
    Observable<RetrofitResponseItem> selectCategory(@Body SelectCategoryRequest request, @Query("access_token") String accessToken);

    @POST("api/Categories/{category_id}/Deals")
    Observable<RetrofitResponseItem> createDeal(@Body CreateDealRequest request, @Path("category_id") String categoryId, @Query("access_token") String accessToken);

    @Multipart
    @POST("api/Uploads/chandler/upload")
    Observable<RetrofitResponseItem<UploadImage>> uploadImage(@Query("access_token") String accessToken, @Part List<MultipartBody.Part> files);
}
