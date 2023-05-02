package com.netdata.app.core

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import java.io.IOException
import javax.inject.Inject

class AESCryptoInterceptor @Inject
constructor(private val aes: AES) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val plainQueryParameters = request.url.queryParameterNames
        var httpUrl = request.url
        // Check Query Parameters and encrypt
        if (plainQueryParameters != null && !plainQueryParameters.isEmpty()) {
            val httpUrlBuilder = httpUrl.newBuilder()
            for (i in plainQueryParameters.indices) {
                val name = httpUrl.queryParameterName(i)
                val value = httpUrl.queryParameterValue(i)
                httpUrlBuilder.setQueryParameter(name, aes.encrypt(value))
            }
            httpUrl = httpUrlBuilder.build()
        }

        // Get Header for encryption
        val apiKey = request.headers.get(Session.API_KEY)
        val token = request.headers.get(Session.USER_SESSION)
        val language = request.headers.get(Session.LANGUAGE) ?: ""
        val newRequest: Request
        val requestBuilder = request.newBuilder()

        // Check if any body and encrypt
        val requestBody = request.body
        if (requestBody?.contentType() != null) {
            // bypass multipart parameters for encryption
            val isMultipart = !requestBody.contentType()!!.type.equals("multipart", ignoreCase = true)
            val bodyPlainText = if (isMultipart) transformInputStream(bodyToString(requestBody)) else bodyToString(requestBody)

            if (bodyPlainText != null) {
                requestBuilder
                        .post(RequestBody.create(
                                requestBody.contentType(),
                                bodyPlainText)
                        )
            }
        }
        //DebugLog.w("Encrypted API KEY = " + encryptedApiKey);
        var encryptedToken = ""
        if (token != null && token.isNotEmpty()) {
            encryptedToken = aes.encrypt(token) ?: ""
        }
        // Build the final request
        newRequest = requestBuilder.url(httpUrl)
                .header(Session.API_KEY, aes.encrypt(apiKey)!!)
                .header(Session.USER_SESSION, encryptedToken)
                .header(Session.LANGUAGE, language)
                .build()


        // execute the request
        val proceed = chain.proceed(newRequest)
        // get the response body and decrypt it.
        val cipherBody = proceed.body!!.string()
        val plainBody = aes.decrypt(cipherBody)

        // create new Response with plaint text body for further process
        return proceed.newBuilder()
                .body(ResponseBody.create("text/json".toMediaTypeOrNull(), plainBody!!.trim { it <= ' ' }))
                .build()

    }

    private fun bodyToString(request: RequestBody?): ByteArray? {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return null
            return buffer.readByteArray()
        } catch (e: IOException) {
            return null
        }

    }

    private fun transformInputStream(inputStream: ByteArray?): ByteArray? {
        return aes.encrypt(inputStream!!)
    }
}
