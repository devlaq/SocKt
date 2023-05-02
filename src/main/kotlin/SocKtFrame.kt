import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed interface SocKtFrame {
    override fun toString(): String
}

object SocKtInitialFrame : SocKtFrame {
    override fun toString(): String {
        return "i"
    }
}

object SocKtHeartbeatFrame : SocKtFrame {
    override fun toString(): String {
        return "h"
    }
}

object SocKtEndFrame : SocKtFrame {
    override fun toString(): String {
        return "e"
    }
}

class SocKtDataFrame(
    val head: String,
    val data: String? = null
): SocKtFrame {
    override fun toString(): String {
        return "d$head${":$data" ?: ""}"
    }
}
