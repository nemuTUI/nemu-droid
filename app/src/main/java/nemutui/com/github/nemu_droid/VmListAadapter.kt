package nemutui.com.github.nemu_droid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

const val EXTRA_NEMU_VM_NAME     = "nemutui.com.github.nemu_droid.EXTRA_NEMU_VM_NAME"
const val EXTRA_NEMU_API_VER     = "nemutui.com.github.nemu_droid.EXTRA_NEMU_API_VER"

class VmListAadapter(private val vms: MutableMap<String, Boolean>, api: NemuApiClient) :
    RecyclerView.Adapter<VmListAadapter.VmListHolder>() {
    class VmListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vm_name: TextView? = null
        var vm_status: ImageView? = null
        var vm_options: ImageView? = null

        init {
            vm_name = itemView.findViewById(R.id.vmitem_name)
            vm_status = itemView.findViewById(R.id.vmitem_status)
            vm_options = itemView.findViewById(R.id.vmitem_options)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VmListHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.vmlist_item, parent, false)
        return VmListHolder(itemView)
    }

    @SuppressLint("IntentReset")
    override fun onBindViewHolder(holder: VmListHolder, position: Int) {
        holder.vm_name?.text = vmlist[position].first
        if (vmlist[position].second) {
            holder.vm_status?.setImageResource(R.drawable.ic_round_power_on_18)
        } else {
            holder.vm_status?.setImageResource(R.drawable.ic_round_power_off_18)
        }

        holder.itemView.setOnClickListener{view ->
            val intent = Intent(view.context, VmSettingsActivity::class.java).apply {
                putExtra(EXTRA_NEMU_VM_NAME,  vmlist[position].first)
                putExtra(EXTRA_NEMU_API_LOCATION,  nemu_api.getApiAddr())
                putExtra(EXTRA_NEMU_API_PORT,  nemu_api.getApiPort())
                putExtra(EXTRA_NEMU_API_PASSWORD,  nemu_api.getApiPass())
                putExtra(EXTRA_CHECK_CERTIFICATE,  nemu_api.getApiTrust())
                putExtra(EXTRA_NEMU_API_VER,  nemu_api_version)
            }

            view.context.startActivity(intent)
        }

        holder.vm_options?.setOnClickListener(View.OnClickListener {
            val menu = PopupMenu(holder.itemView.context, holder.vm_options)
            val name = vmlist[position].first
            val status = vmlist[position].second

            menu.inflate(R.menu.menu_options)
            menu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_start -> {
                        if (!status) {
                            nemu_api.startVm(name)
                            Toast.makeText(holder.itemView.context, name + " started",
                                    Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(holder.itemView.context, name + " already started",
                                    Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    R.id.menu_stop -> {
                        if (status) {
                            nemu_api.stopVm(name)
                            Toast.makeText(holder.itemView.context, name + " stopped",
                                    Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(holder.itemView.context, name + " already stopped",
                                    Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    R.id.menu_force_stop -> {
                        if (status) {
                            nemu_api.forceStopVm(name)
                            Toast.makeText(holder.itemView.context, name + " force stopped",
                                    Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(holder.itemView.context, name + " already stopped",
                                    Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    R.id.menu_connect -> {
                        if (status) {
                            val aspice_intent = holder.itemView.context.
                            getPackageManager().getLaunchIntentForPackage(
                                    "com.iiordanov.freeaSPICE")

                            if (aspice_intent == null) {
                                Toast.makeText(holder.itemView.context, "install aSPICE",
                                        Toast.LENGTH_SHORT).show()
                            } else {
                                if (nemu_api.connectPort(name)) {
                                    val preferences = holder.itemView.context.getSharedPreferences(
                                        holder.itemView.context.getPackageName() + "_preferences", Context.MODE_PRIVATE)
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    val use_ssh = preferences.getBoolean("ssh_proxy", false)
                                    val ssh_host = preferences.getString("ssh_host", nemu_api.getApiAddr())
                                    val ssh_port = preferences.getString("ssh_port", "22")
                                    val ssh_user = preferences.getString("ssh_user", "user")
                                    val ssh_pass = preferences.getString("ssh_pass", "not set")
                                    var uri = "spice://" + nemu_api.getApiAddr() + ":" +
                                           nemu_api.getConnectPort() + "?ConnectionName=" + name

                                    if (use_ssh) {
                                        uri += "&SpicePassword&SshHost=" + ssh_host + "&SshPort=" +
                                            ssh_port + "&SshUsername=" + ssh_user + "&SecurityType=24"
                                    }

                                    if (use_ssh && !ssh_pass.equals("not set")) {
                                        uri += "&SshPassword=" + ssh_pass
                                    }

                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.setType("application/vnd.spice")
                                    intent.setData(Uri.parse(uri))
                                    holder.itemView.context.startActivity(intent)
                                } else {
                                    Toast.makeText(holder.itemView.context, name + "cannot get connect port",
                                            Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(holder.itemView.context, name + " not running",
                                    Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    else -> false
                }
            }
            menu.show()
        })
    }

    override fun getItemCount() = vms.count()

    var vmlist: List<Pair<String, Boolean>> = vms.toList()
    private var nemu_api: NemuApiClient = api
    private var nemu_api_version = api.getApiVersion()
}