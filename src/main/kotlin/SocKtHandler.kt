import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds

class SocKtHandler(
    val identifier: String
) {

    private val heartbeatInterval = 20.seconds

    private val sessions = mutableListOf<SocKtSession>()

    private var sessionCreateHandler: suspend (SocKtSession) -> Unit = {}
    private var sessionEndHandler: suspend (SocKtSession) -> Unit = {}

    init {
        CoroutineScope(Job()).launch {
            while (true) {
                delay(heartbeatInterval)
                heartbeat()
            }
        }
    }

    fun findByMeta(key: String): SocKtSession? {
        return sessions.find { it.meta.containsKey(key) }
    }

    fun findByMeta(key: String, value: String): SocKtSession? {
        return sessions.find { it.meta[key] == value }
    }

    fun applyConfiguration(configuration: SocKtConfiguration) {
        configuration.sessionCreate?.let { this.sessionCreateHandler = it }
        configuration.sessionEnd?.let { this.sessionEndHandler = it }
    }

    fun addSession(session: SocKtSession) {
        sessions.add(session)

        CoroutineScope(Job()).launch {
            session.send(SocKtInitialFrame)
            sessionCreateHandler(session)
        }
    }

    fun removeSession(session: SocKtSession) {
        sessions.remove(session)

        runBlocking {
            sessionEndHandler(session)
            session.send(SocKtEndFrame)
        }
    }

    fun listSessions() = sessions.toList()

    suspend fun broadcast(frame: SocKtFrame): Int {
        var count = 0

        sessions.forEach {
            it.send(frame)
            count++
        }

        return count
    }

    suspend fun broadcast(function: (SocKtSession) -> SocKtFrame): Int {
        var count = 0

        sessions.forEach {
            it.send(function(it))
            count++
        }

        return count
    }

    private suspend fun heartbeat() {
        broadcast(SocKtHeartbeatFrame)
    }

}
