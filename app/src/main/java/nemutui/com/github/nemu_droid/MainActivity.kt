package nemutui.com.github.nemu_droid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch

const val EXTRA_NEMU_API_LOCATION = "nemutui.com.github.nemu_droid.EXTRA_NEMU_API_LOCATION"
const val EXTRA_NEMU_API_PASSWORD = "nemutui.com.github.nemu_droid.EXTRA_NEMU_API_PASSWORD"
const val EXTRA_NEMU_API_PORT     = "nemutui.com.github.nemu_droid.EXTRA_NEMU_API_PORT"
const val EXTRA_CHECK_CERTIFICATE = "nemutui.com.github.nemu_droid.EXTRA_CHECK_CERTIFICATE"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* This is bad I know, but I'm noob in Kotlin and Android */
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        api_conn_et = findViewById<EditText>(R.id.nemu_api_location)
        api_port_et = findViewById<EditText>(R.id.nemu_api_port)
        api_pass_et = findViewById<EditText>(R.id.nemu_api_password)
        api_check_cert_et = findViewById<Switch>(R.id.trust_all_sw)

        api_conn_et.addTextChangedListener(connect_text_watcher)
        api_port_et.addTextChangedListener(connect_text_watcher)
        api_pass_et.addTextChangedListener(connect_text_watcher)

        preferences = getSharedPreferences(
            getPackageName() + "_preferences", Context.MODE_PRIVATE)

        api_conn_et.setText(preferences.getString("api_conn", ""))
        api_port_et.setText(preferences.getString("api_port", ""))
        api_check_cert_et.setChecked(preferences.getBoolean("api_check_cert", false))
    }

    fun connectToApi(view: View) {
        val api_conn = api_conn_et.text.toString()
        val api_port = api_port_et.text.toString()
        val api_pass = api_pass_et.text.toString()
        val check_cert = api_check_cert_et.isChecked()

        val editor = preferences.edit()
        editor.putString("api_conn", api_conn)
        editor.putString("api_port", api_port)
        editor.putBoolean("api_check_cert", check_cert)
        editor.apply();

        val intent = Intent(this, ConnectToApiActivity::class.java).apply {
            putExtra(EXTRA_NEMU_API_LOCATION, api_conn)
            putExtra(EXTRA_NEMU_API_PASSWORD, api_pass)
            putExtra(EXTRA_NEMU_API_PORT,
                if (api_port.isNullOrEmpty()) getString(R.string.nemu_default_port) else api_port)
            putExtra(EXTRA_CHECK_CERTIFICATE, check_cert)
        }

        startActivity(intent)
    }

    private val connect_text_watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val connect_button = findViewById<Button>(R.id.connect_button)
            val api_conn = api_conn_et.text.toString()
            val api_pass = api_pass_et.text.toString()
            val state = !api_conn.isEmpty() && !api_pass.isEmpty()

            connect_button.isEnabled = state
        }
    }

    private lateinit var api_conn_et : EditText
    private lateinit var api_port_et : EditText
    private lateinit var api_pass_et : EditText
    private lateinit var api_check_cert_et : Switch
    private lateinit var preferences : SharedPreferences
}