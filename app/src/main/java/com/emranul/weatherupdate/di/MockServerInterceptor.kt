package com.emranul.weatherupdate.di

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

class MockServerInterceptor @Inject constructor(
    @ApplicationContext val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        // Read the JSON file from assets based on the request URL

        val jsonFileName: String = getJsonFileNameFromUrl(chain.request().url.toString())!!
        val json = try {
            readJsonFileFromAssets(jsonFileName)
        } catch (e: Exception) {
            ""
        }



        return Response.Builder()
            .code(200)
            .message(json)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_0)
            .body(json.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
            .build()
    }

    private fun getJsonFileNameFromUrl(url: String): String? {
        return when {
            url.contains("news-list") -> "story_list.json"
            url.contains("story") -> "sample_story1.json"
            else -> null
        }
    }

    @Throws(IOException::class)
    private fun readJsonFileFromAssets(fileName: String): String {
        // Read the JSON file from assets directory
        val inputStream: InputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        reader.close()
        return stringBuilder.toString()
    }
}