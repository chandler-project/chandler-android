package com.chandlersystem.chandler.data.api;

import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.data.models.pojo.Comment;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.data.models.pojo.Request;
import com.chandlersystem.chandler.data.models.pojo.UploadImage;
import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.data.models.request.CommentBody;
import com.chandlersystem.chandler.data.models.request.CreateDealBody;
import com.chandlersystem.chandler.data.models.request.CreateRequestBody;
import com.chandlersystem.chandler.data.models.request.DealRequest;
import com.chandlersystem.chandler.data.models.request.EditProfileBody;
import com.chandlersystem.chandler.data.models.request.LoginRequest;
import com.chandlersystem.chandler.data.models.request.SelectCategoryRequest;
import com.chandlersystem.chandler.data.models.response.AuthenticationRespone;
import com.chandlersystem.chandler.data.models.response.RetrofitResponseItem;
import com.chandlersystem.chandler.data.models.response.RetrofitResponseListItem;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChandlerApi {
    @POST("/api/Members/login")
    Observable<RetrofitResponseItem<AuthenticationRespone>> authentication(@Body LoginRequest request, @Query("force") int force, @Query("include") String include);

    @GET("/api/Categories")
    Observable<RetrofitResponseListItem<Category>> getCategoryList();

    @GET("/api/Deals?filter={\"include\": \"category\"}")
    Observable<RetrofitResponseListItem<Deal>> getDealList(@Query("access_token") String accessToken);

    @GET("/api/Categories/{category_id}/Deals")
    Observable<RetrofitResponseListItem<Deal>> getDealListOfCategory(@Path("category_id") String categoryId, @Query("access_token") String accessToken);

    @GET("/api/Deals/new-feeds")
    Observable<RetrofitResponseListItem<Deal>> getDealNewFeed(@Query("access_token") String accessToken);

    @GET("/api/Requests/new-feeds")
    Observable<RetrofitResponseListItem<Request>> getRequestNewFeed(@Query("access_token") String accessToken);

    @GET("/api/Deals/{deal_id}/comments")
    Observable<RetrofitResponseListItem<Comment>> getComment(@Path("deal_id") String dealId, @Query("access_token") String accessToken);

    @POST("/api/Deals/{deal_id}/comments")
    Observable<RetrofitResponseItem> postComment(@Body CommentBody commentBody, @Path("deal_id") String dealId, @Query("access_token") String accessToken);

    @PATCH("/api/Requests/{request_id}/bid")
    Observable<RetrofitResponseListItem> bidRequest(@Query("access_token") String accessToken);

    @GET("/api/Requests")
    Observable<RetrofitResponseListItem<Request>> getRequestList(@Query("access_token") String accessToken);

    @POST("/api/Members/logout")
    Observable<RetrofitResponseItem> logout(@Query("access_token") String acwcessToken);

    @PATCH("/api/Members/{user_id}")
    Observable<RetrofitResponseItem<User>> editProfile(@Body EditProfileBody editProfileBody, @Path("user_id") String userId, @Query("access_token") String accessToken);

    @POST("api/Members/categories/choose")
    Observable<RetrofitResponseItem> selectCategory(@Body SelectCategoryRequest request, @Query("access_token") String accessToken);

    @POST("api/Categories/{category_id}/Deals")
    Observable<RetrofitResponseItem> createDeal(@Body CreateDealBody request, @Path("category_id") String categoryId, @Query("access_token") String accessToken);

    @POST("api/Categories/{category_id}/Requests")
    Observable<RetrofitResponseItem> createRequest(@Body CreateRequestBody request, @Path("category_id") String categoryId, @Query("access_token") String accessToken);

    @Multipart
    @POST("api/Uploads/chandler/upload")
    Observable<RetrofitResponseItem<UploadImage>> uploadImages(@Query("access_token") String accessToken, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("api/Uploads/chandler/upload")
    Observable<RetrofitResponseItem<UploadImage>> uploadImage(@Query("access_token") String accessToken, @Part MultipartBody.Part file);

    @PATCH("api/Members/{user_id}/shipper")
    Observable<RetrofitResponseItem<User>> becomeShipper(@Path("user_id") String userId, @Query("access_token") String accessToken);
}
