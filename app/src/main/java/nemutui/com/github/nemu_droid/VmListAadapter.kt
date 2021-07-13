package nemutui.com.github.nemu_droid

import android.service.autofill.OnClickAction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class VmListAadapter(private val vms: MutableMap<String, Boolean>, api: NemuApiClient) :
    RecyclerView.Adapter<VmListAadapter.VmListHolder>() {
    class VmListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vm_name: TextView? = null
        var vm_status: TextView? = null
        var vm_options: TextView? = null

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

    override fun onBindViewHolder(holder: VmListHolder, position: Int) {
        holder.vm_name?.text = vmlist[position].first
        holder.vm_status?.text = if (vmlist[position].second) "running" else "stopped"

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
                    else -> false
                }
            }
            menu.show()
        })
    }

    override fun getItemCount() = vms.count()

    var vmlist: List<Pair<String, Boolean>> = vms.toList()
    private var nemu_api: NemuApiClient = api
}