package nemutui.com.github.nemu_droid

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class VmSettingsActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm_name = intent.getStringExtra(EXTRA_NEMU_VM_NAME)
        val api_conn = intent.getStringExtra(EXTRA_NEMU_API_LOCATION)
        val api_port = intent.getStringExtra(EXTRA_NEMU_API_PORT)
        val api_pass = intent.getStringExtra(EXTRA_NEMU_API_PASSWORD)
        val api_trust = intent.getBooleanExtra(EXTRA_CHECK_CERTIFICATE, false)
        val api_version = intent.getStringExtra(EXTRA_NEMU_API_VER)

        val nemu_client = NemuApiClient(api_conn, api_port, api_pass, api_trust)

        setContentView(R.layout.vm_settings)
        val layout = findViewById<LinearLayout>(R.id.ll_settings_layout)

        val api = api_version!!.split(".")
        if (api.size != 2) {
            return printErr(layout, "Invalid version string")
        }
        val api_major = api[0].toInt()
        val api_minor = api[1].toInt()
        if (api_major == 0 && api_minor < 2) {
            return printErr(layout, "Settings available in nEMU API ver >= 0.2")
        }

        val vm_name_tv = TextView(this)
        vm_name_tv.gravity = Gravity.CENTER
        vm_name_tv.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        vm_name_tv.textSize = 26f
        vm_name_tv.text = vm_name + " settings"
        layout.addView(vm_name_tv)

        if (api_major == 0 && api_minor > 2) {
            val save_button = MaterialButton(this)
            save_button.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            save_button.insetTop = 40
            save_button.text = "Save"
            layout.addView(save_button)

        } else {
            val props = nemu_client.getVmProps02(vm_name)
            val vm_props_tv = TextView(this)
            vm_props_tv.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            vm_props_tv.textSize = 24f
            if (props.result) {
                var kvm = "no"
                var hcpu = "no"
                if (props.kvm) {
                    kvm = "yes"
                }
                if (props.hcpu) {
                    hcpu = "yes"
                }
                vm_props_tv.text = " smp: " + props.smp +
                        "\n memory: " + props.mem.toString() +
                        "\n kvm: " + kvm +
                        "\n host cpu: " + hcpu +
                        "\n ifaces: " + props.netifs +
                        "\n disk interface: " + props.drv_iface
            } else {
                vm_props_tv.text = " error: " + nemu_client.getErr()
            }
            layout.addView(vm_props_tv)
        }

        /*val kvm = false
        val name = findViewById(R.id.settings_vm_name) as android.widget.TextView
        val prop_smp = findViewById(R.id.settings_vm_smp) as android.widget.TextView
        val prop_drv_iface = findViewById(R.id.spin_drv_iface) as android.widget.Spinner
        val prop_kvm = findViewById(R.id.settings_vm_kvm) as android.widget.Switch

        prop_kvm.setChecked(kvm)

        val drive_ints = arrayOf("apple", "banana", "cherry")

        var spinAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, drive_ints)
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prop_drv_iface.adapter = spinAdapter
        prop_drv_iface.setSelection(2)

        name.text = vm_name + " settings"
        prop_smp.text = "42"*/
    }

    private fun printErr(layout: LinearLayout, msg: String) {
        val err_text = TextView(this)
        err_text.textSize = 20f
        err_text.gravity = Gravity.CENTER
        err_text.text = msg
        err_text.setPadding(0, 30, 0, 0)
        layout.addView(err_text)
    }
}