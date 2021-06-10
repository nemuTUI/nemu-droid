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
        val tv = findViewById<TextView>(R.id.api_location).apply {
            text = api_conn.plus(":").plus(api_port)
        }
    }
}