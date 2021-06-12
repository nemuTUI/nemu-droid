package nemutui.com.github.nemu_droid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ConnectToApiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_to_api)

        val api_conn = intent.getStringExtra(EXTRA_NEMU_API_LOCATION)
        val api_port = intent.getStringExtra(EXTRA_NEMU_API_PORT)
        val api_pass = intent.getStringExtra(EXTRA_NEMU_API_PASSWORD)

        val nemu_client = NemuApiClient(api_conn, api_port, api_pass)
        val auth_res = nemu_client.checkAuth()

        val tv = findViewById<TextView>(R.id.api_location).apply {
            this.text = "nEMU [" + api_conn + ":" + api_port + "] " + nemu_client.getErr()
        }
    }
}