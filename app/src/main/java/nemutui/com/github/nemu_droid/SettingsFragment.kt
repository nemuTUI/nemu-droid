package nemutui.com.github.nemu_droid

import android.os.Bundle
import android.text.InputType
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.EditTextPreference

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref)
        val port_pref: EditTextPreference? = findPreference("ssh_port")
        val pass_pref: EditTextPreference? = findPreference("ssh_pass")

        port_pref?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        pass_pref?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }
}