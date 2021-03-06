package com.chandlersystem.chandler.data.api;

import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.data.models.pojo.Comment;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.data.models.pojo.Feedback;
import com.chandlersystem.chandler.data.models.pojo.Order;
import com.chandlersystem.chandler.data.models.pojo.OrderCount;
import com.chandlersystem.chandler.data.models.pojo.Request;
import com.chandlersystem.chandler.data.models.pojo.UploadImage;
import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.data.models.request.BidRequestBody;
import com.chandlersystem.chandler.data.models.request.CommentBody;
import com.chandlersystem.chandler.data.models.request.CreateDealBody;
import com.chandlersystem.chandler.data.models.request.CreateRequestBody;
import com.chandlersystem.chandler.data.models.request.DealRequest;
import com.chandlersystem.chandler.data.models.request.Device;
import com.chandlersystem.chandler.data.models.request.EditProfileBody;
import com.chandlersystem.chandler.data.models.request.FeedbackBody;
import com.chandlersystem.chandler.data.models.request.LoginRequest;
import com.chandlersystem.chandler.data.models.request.OrderBody;
import com.chandlersystem.chandler.data.models.request.PaymentBody;
import com.chandlersystem.chandler.data.models.request.SelectCategoryRequest;
import com.chandlersystem.chandler.data.models.response.AuthenticationRespone;
import com.chandlersystem.chandler.data.models.response.RetrofitResponseItem;
import com.chandlersystem.chandler.data.models.response.RetrofitResponseListItem;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
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

    @PATCH("api/Members/{user_id}")
    Observable<RetrofitResponseItem> subscribeRegistrationId(@Body Device device,
                                                             @Path("user_id") String userId,
                                                             @Query("access_token") String accessToken);

    @GET("/api/Members/{member_id}/deals")
    Observable<RetrofitResponseListItem<Deal>> getDealList(@Path("member_id") String userId);

    @GET("/api/Categories/{category_id}/Deals")
    Observable<RetrofitResponseListItem<Deal>> getDealListOfCategory(@Path("category_id") String categoryId, @Query("access_token") String accessToken);

    @GET("/api/Deals/new-feeds?filter={\"include\": \"category\"}")
    Observable<RetrofitResponseListItem<Deal>> getDealNewFeed(@Query("access_token") String accessToken);

    @PATCH("api/Requests/{request_id}/{shipper_id}/choose")
    Observable<RetrofitResponseItem> chooseShipperForRequest(@Path("request_id") String requestId, @Path("shipper_id") String shipperId, @Query("access_token") String accessToken);

    @GET("/api/Requests/new-feeds")
    Observable<RetrofitResponseListItem<Request>> getRequestNewFeed(@Query("access_token") String accessToken);

    @PATCH("/api/Deals/{deal_id}/upvote")
    Observable<RetrofitResponseItem> upVote(@Path("deal_id") String dealId, @Query("access_token") String access_token);

    @PATCH("/api/Deals/{deal_id}/downvote")
    Observable<RetrofitResponseItem> downVote(@Path("deal_id") String dealId, @Query("access_token") String access_token);

    @GET("/api/Deals/{deal_id}/comments?filter={\"order\": \"modified DESC\"}")
    Observable<RetrofitResponseListItem<Comment>> getComment(@Path("deal_id") String dealId, @Query("access_token") String accessToken);

    @POST("/api/Deals/{deal_id}/comments")
    Observable<RetrofitResponseItem> postComment(@Body CommentBody commentBody, @Path("deal_id") String dealId, @Query("access_token") String accessToken);

    @PATCH("/api/Deals/{deal_id}/request")
    Observable<RetrofitResponseItem<Deal>> buyDeal(@Path("deal_id") String dealId, @Query("access_token") String accessToken, @Query("amount") float amount);

    @PATCH("/api/Requests/{request_id}/bid")
    Observable<RetrofitResponseItem<Request>> bidRequest(@Body BidRequestBody bidRequestBody, @Path("request_id") String requestId, @Query("access_token") String accessToken);

    @GET("/api/Members/{member_id}/feedbacks")
    Observable<RetrofitResponseListItem<Feedback>> getFeedback(@Path("member_id") String memberId);

    @POST("/api/Members/{member_id}/feedback")
    Observable<RetrofitResponseItem<Feedback>> sendFeedback(@Body FeedbackBody feedbackBody, @Path("member_id") String memberId, @Query("access_token") String accessToken);

    @GET("/api/Members/{member_id}/requests")
    Observable<RetrofitResponseListItem<Request>> getRequestList(@Path("member_id") String userId);

    @GET("/api/Members/{member_id}")
    Observable<RetrofitResponseItem<User>> getPublicProfile(@Path("member_id") String userId);

    @POST("/api/Members/logout")
    Observable<RetrofitResponseItem> logout(@Query("access_token") String accessToken);

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

    @GET("api/Orders/pendings")
    Observable<RetrofitResponseListItem<Order>> getPendingOrder(@Query("access_token") String accessToken);

    @GET("api/Orders")
    Observable<RetrofitResponseListItem<Order>> getOrder(@Query("access_token") String accessToken, @Query("filter") String filter);

    @GET("api/Orders/pendings/count")
    Observable<RetrofitResponseItem<OrderCount>> getPendingOrderCount(@Query("access_token") String accessToken);

    @PATCH("api/Orders/{order_id}/disclaimer")
    Observable<RetrofitResponseItem> removeOrder(@Path("order_id") String orderId, @Query("access_token") String accessToken);

    @PATCH("api/Orders/pay")
    Observable<RetrofitResponseItem> payment(@Body PaymentBody body, @Query("access_token") String accessToken);

    @PATCH("api/Orders/{order_id}/accepted")
    Observable<RetrofitResponseItem<Order>> acceptDelivery(@Path("order_id") String orderId, @Query("access_token") String accessToken);

    @PATCH("api/Orders/{order_id}/rejected")
    Observable<RetrofitResponseItem<Order>> rejectDelivery(@Body String body, @Path("order_id") String orderId, @Query("access_token") String accessToken);

    @PATCH("api/Orders/{order_id}/delivered")
    Observable<RetrofitResponseItem<Order>> deliveredOrder(@Path("order_id") String orderId, @Query("access_token") String accessToken);

    @PATCH("api/Orders/{order_id}/confirmed")
    Observable<RetrofitResponseItem<Order>> confirmedOrder(@Path("order_id") String orderId, @Query("access_token") String accessToken);

    @PATCH("api/Orders/{order_id}/denied")
    Observable<RetrofitResponseItem<Order>> deniedOrder(@Body String body, @Path("order_id") String orderId, @Query("access_token") String accessToken);
}
