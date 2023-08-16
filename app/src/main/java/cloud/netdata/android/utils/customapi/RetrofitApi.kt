package cloud.netdata.android.utils.customapi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*class RetrofitApi {
    private val BASE_URL = "https://app.netdata.cloud/"
    var apiService: MainApi

    companion object{
        private var INSTANCE: RetrofitApi? = null
        fun getInst(): RetrofitApi
        {
            if(INSTANCE == null)
            {
                INSTANCE = RetrofitApi()
            }
            return INSTANCE!!
        }
    }

    init {
        val httpLogger = HttpLoggingInterceptor()
        httpLogger.level = HttpLoggingInterceptor.Level.BODY
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().addInterceptor(httpLogger).build())
            .addConverterFactory(GsonConverterFactory.create()).build()
        apiService = retrofit.create(MainApi::class.java)
    }
}*/

object NetworkClient {
    private var baseUrl: String = ""

    val httpLogger = HttpLoggingInterceptor()

    private val okHttpClient: OkHttpClient by lazy {
        httpLogger.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(httpLogger)
            .build()
    }

    private val retrofitBuilder: Retrofit.Builder by lazy {

        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
    }

    fun setBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofitBuilder.baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(serviceClass)
    }
}

/*.addInterceptor(Interceptor { chain ->
    val builder = chain.request().newBuilder()

    if (Constant.COOKIE_SI.isNotEmpty() && Constant.COOKIE_SV.isNotEmpty()) {
        builder.addHeader("cookie", "s_i=${Constant.COOKIE_SI};s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}")
    }

    val request = chain.request()
    val response = chain.proceed(request)
    Log.e("code", response.code.toString())

    *//*val build = builder.build()
    chain.proceed(build)*//*

    response
})*/
