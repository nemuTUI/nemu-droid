package nemutui.com.github.nemu_droid

data class VmProps02(
    var result: Boolean = false,
    var smp: String = "",
    var mem: Int = 0,
    var kvm: Boolean = false,
    var hcpu: Boolean = false,
    var netifs: Int = 0,
    var drv_iface: String = "",
    var drv_iface_list: Set<String> = setOf<String>()
)