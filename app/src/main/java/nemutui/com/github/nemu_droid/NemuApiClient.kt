package nemutui.com.github.nemu_droid

import android.util.Log
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.network.tls.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class X509TrustAllManager : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
    override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls<X509Certificate>(0)
}

class NemuApiClient(addr: String?, port: String?, pass: String?, trust: Boolean) {
    fun checkAuth(): Boolean {
        val request = "{\"exec\":\"auth\", \"auth\":\"" + api_pass + "\"}"
        if (this.send_request(request)) {
            return true
        }

        return false
    }

    fun nemuVersion() : Boolean {
        val request = "{\"exec\":\"nemu_version\", \"auth\":\"" + api_pass + "\"}"
        if (this.send_request(request)) {
            version = reply
            return true
        }

        return false
    }

    fun getErr() : String {
        return this.err_msg
    }

    fun getVersion() : String {
        return this.version
    }

    private fun send_request(request: String): Boolean {
        var rc = true
        if (api_addr == null || api_port == null || api_pass == null) {
            this.err_msg = "null input params"
            return false
        }

        runBlocking {
            try {
                    val socket = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
                            .connect(InetSocketAddress(api_addr, api_port.toInt()))
                            .tls(Dispatchers.IO, if (!api_trust) X509TrustAllManager() else null, "SHA1PRNG")
                    val sw = socket.openWriteChannel(autoFlush = false)
                    sw.writeStringUtf8(request)
                    sw.flush()
                    val sr = socket.openReadChannel()
                    reply = sr.readUTF8Line().toString()
                } catch (ex: Exception) {
                    Log.e("nemu-droid", "something goes wrong", ex)
                    err_msg = ex.message.toString()
                    rc = false
                }
        }

        return rc
    }

    private val api_addr = addr
    private val api_port = port
    private val api_pass = pass
    private val api_trust = trust
    private var err_msg : String = ""
    private lateinit var reply : String
    private lateinit var version : String
}