import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SocKtSession(
    val handler: SocKtHandler,
    val webSocketSession: WebSocketSession,
    val meta: MutableMap<String, String> = mutableMapOf()
) {
    suspend fun send(frame: SocKtFrame) {
        webSocketSession.send(frame.toString())
    }

    suspend fun sendDataFrame(head: String, data: String? = null) {
        send(SocKtDataFrame(head, data))
    }

    suspend inline fun <reified T> sendJsonFrame(head: String, data: T) {
        sendDataFrame(head, Json.encodeToString(data))
    }
}
