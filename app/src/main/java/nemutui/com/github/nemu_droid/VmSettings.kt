package nemutui.com.github.nemu_droid

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import nemutui.com.github.nemu_droid.R.drawable.edit_text_theme

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

        nemu_client = NemuApiClient(api_conn, api_port, api_pass, api_trust)

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

        props = nemu_client.getVmProps(vm_name)

        if (api_major == 0 && api_minor > 2) {
            val prop_smp_tv = TextView(this)
            prop_smp_tv.textSize = 20f
            prop_smp_tv.setTextColor(Color.DKGRAY)
            prop_smp_tv.setPadding(20, 0, 0, 0)
            prop_smp_tv.text = "SMP"
            layout.addView(prop_smp_tv)

            prop_smp_et = EditText(this)
            prop_smp_et.background =  ResourcesCompat.getDrawable(resources,
                R.drawable.edit_text_theme, null)
            prop_smp_et.setText(props.smp)
            prop_smp_et.setPadding(40, 20, 0, 20)
            prop_smp_et.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            layout.addView(prop_smp_et)

            val prop_mem_tv = TextView(this)
            prop_mem_tv.textSize = 20f
            prop_mem_tv.setTextColor(Color.DKGRAY)
            prop_mem_tv.setPadding(20, 0, 0, 0)
            prop_mem_tv.text = "RAM"
            layout.addView(prop_mem_tv)

            prop_mem_et = EditText(this)
            prop_mem_et.inputType = android.text.InputType.TYPE_CLASS_NUMBER
            prop_mem_et.background =  ResourcesCompat.getDrawable(resources,
                R.drawable.edit_text_theme, null)
            prop_mem_et.setText(props.mem.toString())
            prop_mem_et.setPadding(40, 20, 0, 20)
            prop_mem_et.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            layout.addView(prop_mem_et)

            val prop_ifs_tv = TextView(this)
            prop_ifs_tv.textSize = 20f
            prop_ifs_tv.setTextColor(Color.DKGRAY)
            prop_ifs_tv.setPadding(20, 0, 0, 0)
            prop_ifs_tv.text = "Network interfaces"
            layout.addView(prop_ifs_tv)

            prop_ifs_et = EditText(this)
            prop_ifs_et.inputType = android.text.InputType.TYPE_CLASS_NUMBER
            prop_ifs_et.background =  ResourcesCompat.getDrawable(resources,
                R.drawable.edit_text_theme, null)
            prop_ifs_et.setPadding(40, 20, 0, 20)
            prop_ifs_et.setText(props.netifs.toString())
            prop_ifs_et.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            layout.addView(prop_ifs_et)

            prop_kvm_sw = SwitchMaterial(this)
            prop_kvm_sw.text = "KVM"
            prop_kvm_sw.textSize = 20f
            prop_kvm_sw.setTextColor(Color.DKGRAY)
            prop_kvm_sw.setPadding(20, 0, 20, 0)
            prop_kvm_sw.isChecked = props.kvm
            layout.addView(prop_kvm_sw)

            prop_hcpu_sw = SwitchMaterial(this)
            prop_hcpu_sw.text = "Host CPU"
            prop_hcpu_sw.textSize = 20f
            prop_hcpu_sw.setTextColor(Color.DKGRAY)
            prop_hcpu_sw.setPadding(20, 0, 20, 0)
            prop_hcpu_sw.isChecked = props.hcpu
            layout.addView(prop_hcpu_sw)

            val prop_drive_tv = TextView(this)
            prop_drive_tv.textSize = 20f
            prop_drive_tv.setTextColor(Color.DKGRAY)
            prop_drive_tv.setPadding(20, 0, 0, 0)
            prop_drive_tv.text = "Drive interfaces"
            layout.addView(prop_drive_tv)

            prop_drive_sp = Spinner(this)
            prop_drive_sp.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            prop_drive_sp.setPadding(0, 20, 0, 0)
            layout.addView(prop_drive_sp)

            val spinAdapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, props.drv_iface_list)
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            prop_drive_sp.adapter = spinAdapter
            val idx = props.drv_iface_list.indexOf(props.drv_iface)
            if (idx != -1) {
                prop_drive_sp.setSelection(idx)
            }

            val save_button = MaterialButton(this)
            save_button.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            save_button.insetTop = 60
            save_button.text = "Save"
            save_button.setOnClickListener { this.saveProps(vm_name) }
            layout.addView(save_button)
        } else {
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
    }

    private fun saveProps(vm_name: String?) {
        val newProps = VmProps()

        newProps.smp = prop_smp_et.text.toString()
        newProps.mem = Integer.parseInt(prop_mem_et.text.toString())
        newProps.netifs = Integer.parseInt(prop_ifs_et.text.toString())
        newProps.kvm = prop_kvm_sw.isChecked()
        newProps.hcpu = prop_hcpu_sw.isChecked()
        newProps.drv_iface = prop_drive_sp.getSelectedItem().toString()

        val result = nemu_client.setVmProps(vm_name, props, newProps)
        var msg = "Saved"

        if (!result) {
            msg = "Save failed: " + nemu_client.getErr()
        }

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun printErr(layout: LinearLayout, msg: String) {
        val err_text = TextView(this)
        err_text.textSize = 20f
        err_text.gravity = Gravity.CENTER
        err_text.text = msg
        err_text.setPadding(0, 30, 0, 0)
        layout.addView(err_text)
    }

    private lateinit var props : VmProps
    private lateinit var prop_smp_et : EditText
    private lateinit var prop_mem_et : EditText
    private lateinit var prop_ifs_et : EditText
    private lateinit var prop_kvm_sw : SwitchMaterial
    private lateinit var prop_hcpu_sw : SwitchMaterial
    private lateinit var prop_drive_sp : Spinner
    private lateinit var nemu_client: NemuApiClient
}
