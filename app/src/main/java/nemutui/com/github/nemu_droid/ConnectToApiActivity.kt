package nemutui.com.github.nemu_droid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ConnectToApiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE)
        setContentView(R.layout.activity_connect_to_api)
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.api_title)

        val api_conn = intent.getStringExtra(EXTRA_NEMU_API_LOCATION)
        val api_port = intent.getStringExtra(EXTRA_NEMU_API_PORT)
        val api_pass = intent.getStringExtra(EXTRA_NEMU_API_PASSWORD)
        val api_trust = intent.getBooleanExtra(EXTRA_CHECK_CERTIFICATE, false)

        val nemu_client = NemuApiClient(api_conn, api_port, api_pass, api_trust)
        val auth_res = nemu_client.checkAuth()

        nemu_api = nemu_client

        val tv = findViewById<TextView>(R.id.api_location)

        if (!auth_res) {
            tv.apply {
                this.text = "nEMU [" + api_conn + ":" + api_port + "] " + nemu_client.getErr()
            }
        } else {
            val ver = nemu_client.nemuVersion()

            if (ver) {
                tv.apply {
                    this.text = "nEMU [" + api_conn + ":" + api_port + "] " + nemu_client.getVersion()
                }
            } else {
                tv.apply {
                    this.text = "nEMU [" + api_conn + ":" + api_port + "] " + nemu_client.getErr()
                }
            }

            if (!nemu_client.getVmList()) {
                Log.e("nemu-droid", "cannot get VM list")
                //kill app?
            }
            val rv = findViewById<RecyclerView>(R.id.vm_list_rv)
            val rv_adapter =  VmListAadapter(nemu_client.vmlist, nemu_client)
            rec_view = rv_adapter

            rv.apply {
                setHasFixedSize(true)
                adapter = rv_adapter
                layoutManager = LinearLayoutManager(this.context)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val refresh_butt = menu.findItem(R.id.refresh_butt)
        refresh_butt.setVisible(true)

        return super.onCreateOptionsMenu(menu)
    }

    fun refreshData(menu: MenuItem) {
        nemu_api.getVmList()
        rec_view.vmlist = nemu_api.vmlist.toList()
        rec_view.notifyDataSetChanged()
        /*
         list.remove(position);
         recycler.removeViewAt(position);
         mAdapter.notifyItemRemoved(position);
         mAdapter.notifyItemRangeChanged(position, list.size());
         */
    }

    lateinit private var rec_view : VmListAadapter
    lateinit private var nemu_api : NemuApiClient
}