package nemutui.com.github.nemu_droid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

const val EXTRA_NEMU_API_LOCATION = "nemutui.com.github.nemu_droid.EXTRA_NEMU_API_LOCATION"
const val EXTRA_NEMU_API_PASSWORD = "nemutui.com.github.nemu_droid.EXTRA_NEMU_API_PASSWORD"
const val EXTRA_NEMU_API_PORT     = "nemutui.com.github.nemu_droid.EXTRA_NEMU_API_PORT"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun connectToApi(view: View) {
        val api_conn_et = findViewById<EditText>(R.id.nemu_api_location)
        val api_port_et = findViewById<EditText>(R.id.nemu_api_port)
        val api_pass_et = findViewById<EditText>(R.id.nemu_api_password)

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
}