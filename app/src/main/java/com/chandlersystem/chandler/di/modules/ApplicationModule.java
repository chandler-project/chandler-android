package com.chandlersystem.chandler.di.modules;

import android.app.Application;
import android.content.Context;
import android.support.multidex.BuildConfig;

import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.di.scopes.ApplicationContext;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {
    private final Application context;

    public ApplicationModule(Application context) {
        this.context = context;
    }

    /**
     * Provide only one Application Object which exist in entire application
     *
     * @return ApplicationContext (Can be implicit like a Context)
     */
    @Provides
    @Singleton
    @ApplicationContext
    Context provideApplicationContext() {
        return context;
    }


    /**
     * This is required because of using Inject Constructor in SharedPreferenceManager
     *
     * @return only one Gson which would help to prevent making too many Gson objects
     */
    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    /**
     * Profile OkHttpClient which installs Interceptor without Header
     * Can update Header
     * Can update Locale
     */
    @Provides
    @Singleton
    OkHttpClient provideClients() {
        Locale currentLocale = context.getResources().getConfiguration().locale;

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(
                        chain -> {
                            Request original = chain.request();
                            // Request customization: add request headers
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("locale", currentLocale.getLanguage())
                                    .method(original.method(), original.body());

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        })
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

//        Only show log in dev environment
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor);
        }

        return builder.build();
    }

    /**
     * Use this to create RxJava2 Call Adapter Factory, which help to use retrofit 2 with RxJav2
     *
     * @return
     */
    @Singleton
    @Provides
    RxJava2CallAdapterFactory provideRxJavaCallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    /**
     * Use this for convert response value with Gson
     *
     * @return
     */
    @Singleton
    @Provides
    GsonConverterFactory gsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    /**
     * Build retrofit into API Class
     *
     * @param client
     * @param rxAdapterFactory
     * @param gsonConverterFactory
     * @return the interface for api return
     */
    @Singleton
    @Provides
    ChandlerApi provideRetrofitV1(OkHttpClient client,
                                  RxJava2CallAdapterFactory rxAdapterFactory,
                                  GsonConverterFactory gsonConverterFactory) {
        return new Retrofit
                .Builder()
                .baseUrl(ApiConstant.BASE_URL_VER1)
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxAdapterFactory)
                .build().create(ChandlerApi.class);
    }

    /**
     * Provide Firebase database for listening data change purpose
     *
     * @return
     */
    @Singleton
    @Provides
    FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Singleton
    @Provides
    FirebaseAnalytics provideFirebaseAnalytics() {
        return FirebaseAnalytics.getInstance(context);
    }
}
