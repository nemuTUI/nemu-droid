package nemutui.com.github.nemu_droid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class VmListAadapter(private val vms: MutableMap<String, Boolean>) :
    RecyclerView.Adapter<VmListAadapter.VmListHolder>() {
    class VmListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vm_name: TextView? = null
        var vm_status: TextView? = null

        init {
            vm_name = itemView.findViewById(R.id.vmitem_name)
            vm_status = itemView.findViewById(R.id.vmitem_status)
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
    }

    override fun getItemCount() = vms.count()

    private var vmlist: List<Pair<String, Boolean>> = vms.toList()
}