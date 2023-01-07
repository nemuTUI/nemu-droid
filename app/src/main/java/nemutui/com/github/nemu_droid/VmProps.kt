package nemutui.com.github.nemu_droid

data class VmProps(
    var result: Boolean = false,
    var smp: String = "",
    var mem: Int = -1,
    var kvm: Boolean = false,
    var hcpu: Boolean = false,
    var netifs: Int = -1,
    var drv_iface: String = "",
    var drv_iface_list: Array<String> = arrayOf<String>()
)
