package org.piramalswasthya.sakhi.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.piramalswasthya.sakhi.database.room.InAppDb
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.network.D2DNetworkApiService
import org.piramalswasthya.sakhi.network.NcdNetworkApiService
import org.piramalswasthya.sakhi.network.TmcNetworkApiService
import org.piramalswasthya.sakhi.network.interceptors.ContentTypeInterceptor
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertD2DInterceptor
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertTmcInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val baseD2DUrl = "http://d2dapi.piramalswasthya.org:9090/api/"
        //"http://117.245.141.41:9090/api/"

    private const val baseTmcUrl = // "http://assamtmc.piramalswasthya.org:8080/"
    "http://uatamrit.piramalswasthya.org:8080/"

    private const val baseNcdURL = "http://117.245.141.46:8080/";

    private val baseClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(ContentTypeInterceptor())

            .build()

    @Singleton
    @Provides
    fun provideMoshiInstance(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    @Named("logInClient")
    fun provideD2DHttpClient() : OkHttpClient {
        return baseClient
            .newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(TokenInsertD2DInterceptor())
            .build()
    }

    @Singleton
    @Provides
    @Named("uatClient")
    fun provideTmcHttpClient() : OkHttpClient {
        return baseClient
            .newBuilder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(TokenInsertTmcInterceptor())
            .build()
    }

    @Singleton
    @Provides
    @Named("ncdClient")
    fun provideNcdHttpClient() : OkHttpClient {
        return baseClient
            .newBuilder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(TokenInsertTmcInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideD2DApiService(moshi : Moshi,@Named("logInClient") httpClient: OkHttpClient) : D2DNetworkApiService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            //.addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseD2DUrl)
            .client(httpClient)
            .build()
            .create(D2DNetworkApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideTmcApiService(moshi : Moshi,@Named("uatClient") httpClient: OkHttpClient) : TmcNetworkApiService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            //.addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseTmcUrl)
            .client(httpClient)
            .build()
            .create(TmcNetworkApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideNcdApiService(moshi : Moshi,@Named("ncdClient") httpClient: OkHttpClient) : NcdNetworkApiService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            //.addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseNcdURL)
            .client(httpClient)
            .build()
            .create(NcdNetworkApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context : Context) = InAppDb.getInstance(context)


    @Singleton
    @Provides
    fun providePreference(@ApplicationContext context : Context) = PreferenceDao(context)




}