package cloud.netdata.android.data

import okhttp3.HttpUrl

object URLFactory {

    // server details
    private const val IS_LOCAL = false
    private const val SCHEME = "http"
    private val HOST = if (IS_LOCAL) "18.169.15.187" else "app.netdata.cloud"
    private val API_PATH = if (IS_LOCAL) "api/v1/" else "api/"
    private val PORT = if (IS_LOCAL) 8082 else 8082

    fun provideHttpUrl(): HttpUrl {
        return HttpUrl.Builder()
                .scheme(SCHEME)
                .host(HOST)
//                .port(PORT)
                .addPathSegments(API_PATH)
                .build()
    }


    // API Methods
    object Method {
        const val MAGIC_LINK = "v2/auth/account/magic-link"
    }

    object Link {
        const val SIGN_IN_LINK = "https://app.netdata.cloud/sign-in"
        const val SIGN_UP_LINK = "https://app.netdata.cloud/sign-up"
    }

}
