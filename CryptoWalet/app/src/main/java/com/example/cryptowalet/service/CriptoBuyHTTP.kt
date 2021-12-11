package com.example.cryptowalet.service

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.cryptowalet.models.Criptos
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

object CriptoBuyHTTP {

    val url = "https://www.mercadobitcoin.net/api/"

    private fun connect(urlAdrress: String): HttpURLConnection {
        val second = 1000
        val url = URL(urlAdrress)
        val connection = (url.openConnection() as HttpURLConnection).apply {
            readTimeout = 10 * second
            connectTimeout = 15 * second
            requestMethod = "GET"
            doInput = true
            doOutput = false
        }
        connection.connect()
        return connection
    }

    fun hasConnection(ctx: Context): Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnected
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadCripto(moeda: String): Criptos? {
        try {
            val connection = connect("$url$moeda/ticker/")
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val jsonString = streamToString(inputStream)
                val jsonO = JSONObject(jsonString)
                val json = jsonO.getJSONObject("ticker")
                return readCripto(json)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun readCripto(json: JSONObject): Criptos? {
        try {
            val results = Criptos(
                json.getString("high"),
                json.getString("low"),
                json.getString("vol"),
                json.getString("last"),
                json.getString("buy"),
                json.getString("sell"),
                json.getString("open"),
                json.getString("date")
            )
            return results


        } catch (e: IOException) {
            Log.e("Erro", "Não foi possivel ler o JSON")
        }
        return null
    }

    private fun streamToString(inputStream: InputStream): String {
        val buffer = ByteArray(1024)
        val bigBuffer = ByteArrayOutputStream()
        var bytesRead: Int
        while (true) {
            bytesRead = inputStream.read(buffer)
            if (bytesRead == -1) break
            bigBuffer.write(buffer, 0, bytesRead)
        }
        return String(bigBuffer.toByteArray(), Charset.forName("UTF-8"))
    }
}