package ufrpe.mobile.kotlincurrencyconverter

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import kotlinx.android.synthetic.main.new_layout.*
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_layout)

    }

    fun get(view: View) {

        val downloadData = Download()

        try {

            val base = radGroupBase.checkedRadioButtonId
            val buttonBase: RadioButton = findViewById(base)
            val textBase = buttonBase.text.toString()

            val convert = radGroupConvert.checkedRadioButtonId
            val buttonConvert: RadioButton = findViewById(convert)
            val textConvert = buttonConvert.text.toString()


            val url = "https://api.exchangeratesapi.io/latest?symbols="

            val sb = StringBuilder()
            sb.append(url).append(textBase).append(",").append(textConvert)
            val c = sb.toString()

            downloadData.execute(c)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class Download : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg args: String?): String {

            var result = ""
            var url: URL
            val httpURLConnection: HttpURLConnection

            try {

                url = URL(args[0])
                httpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream = httpURLConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)

                var data = inputStreamReader.read()

                while (data > 0) {
                    val character = data.toChar()
                    result += character

                    data = inputStreamReader.read()
                }

                return result

            } catch (e: Exception) {
                e.printStackTrace()
                return result
            }

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {

                val jSONObject = JSONObject(result)
                Log.i("VAAB", jSONObject.toString())

                val base = jSONObject.getString("base")
                Log.i("VAAB", base)

                val date = jSONObject.getString("date")
                Log.i("VAAB", date)

                val rates = jSONObject.getString("rates")
                Log.i("VAAB", rates)

                val newJsonObject = JSONObject(rates)

                val convert = radGroupConvert.checkedRadioButtonId
                val buttonConvert: RadioButton = findViewById(convert)
                val textConvert = buttonConvert.text.toString()

                val converted = newJsonObject.getString(textConvert)
                val convertedFloat = converted.toFloat()

                val valor = txt_valor.text.toString().toFloat()

                val finalValue = valor * convertedFloat

                txtFinalValue.text = finalValue.toString()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
