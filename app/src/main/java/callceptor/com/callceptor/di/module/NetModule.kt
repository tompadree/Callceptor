package callceptor.com.callceptor.di.module;

import android.content.Context
import callceptor.com.callceptor.data.api.APIConstants.Companion.BASE_URL
import callceptor.com.callceptor.data.api.CallceptorApi
import callceptor.com.callceptor.data.api.NetworkApi
import callceptor.com.callceptor.data.api.NetworkApiImpl
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Tomislav on 04,September,2018
 */
@Module
class NetModule {

    @Provides
    @Singleton
    fun provideHttpCache(application: Context): Cache = Cache(application.cacheDir, (10 * 1024 * 1024).toLong())


    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache): OkHttpClient {
        val client = OkHttpClient.Builder().readTimeout(10000, TimeUnit.MILLISECONDS).connectTimeout(15000, TimeUnit.MILLISECONDS)
        client.cache(cache)
        return client.build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideNetworkApi(callceptorApi: CallceptorApi): NetworkApi = NetworkApiImpl(callceptorApi)

    @Provides
    @Singleton
    fun provideEkonergApi(retrofit: Retrofit): CallceptorApi = retrofit.create<CallceptorApi>(CallceptorApi::class.java)

}