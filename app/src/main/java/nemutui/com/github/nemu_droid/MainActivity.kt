package nemutui.com.github.nemu_droid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText

const val EXTRA_NEMU_API_LOCATION = "nemutui.com.github.nemu_droid.EXTRA_NEMU_API_LOCATION"
const val EXTRA_NEMU_API_PASSWORD = "nemutui.com.github.nemu_droid.EXTRA_NEMU_API_PASSWORD"
const val EXTRA_NEMU_API_PORT     = "nemutui.com.github.nemu_droid.EXTRA_NEMU_API_PORT"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        api_conn_et = findViewById<EditText>(R.id.nemu_api_location)
        api_port_et = findViewById<EditText>(R.id.nemu_api_port)
        api_pass_et = findViewById<EditText>(R.id.nemu_api_password)

        api_conn_et.addTextChangedListener(connect_text_watcher)
        api_port_et.addTextChangedListener(connect_text_watcher)
        api_pass_et.addTextChangedListener(connect_text_watcher)
    }

    fun connectToApi(view: View) {
        val api_conn = api_conn_et.text.toString()
        val api_port = api_port_et.text.toString()
        val api_pass = api_pass_et.text.toString()

        val intent = Intent(this, ConnectToApiActivity::class.java).apply {
            putExtra(EXTRA_NEMU_API_LOCATION, api_conn)
            putExtra(EXTRA_NEMU_API_PASSWORD, api_pass)
            putExtra(EXTRA_NEMU_API_PORT, api_port)
        }

        startActivity(intent)
    }

    private val connect_text_watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val connect_button = findViewById<Button>(R.id.connect_button)
            val api_conn = api_conn_et.text.toString()
            val api_port = api_port_et.text.toString()
            val api_pass = api_pass_et.text.toString()
            val state = !api_conn.isEmpty() && !api_port.isEmpty() && !api_pass.isEmpty()

            connect_button.isEnabled = state
        }
    }

    private lateinit var api_conn_et : EditText
    private lateinit var api_port_et : EditText
    private lateinit var api_pass_et : EditText
}