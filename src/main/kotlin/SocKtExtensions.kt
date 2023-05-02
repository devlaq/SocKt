import io.ktor.websocket.*

fun WebSocketSession.initSocKt(identifier: String): SocKtSession {

    val socKtHandler =
        if(SocKt.get(identifier) == null) {
            val handler = SocKtHandler(identifier)
            SocKt.register(handler)

            handler
        } else SocKt.get(identifier)!!

    val socKtSession = SocKtSession(
        handler = socKtHandler,
        webSocketSession = this
    )

    socKtHandler.addSession(socKtSession)

    return socKtSession
}

fun WebSocketSession.configureSocKt(body: SocKtConfiguration.() -> Unit) {
    val configuration = SocKtConfiguration()
    body(configuration)

    getAssignedSocKtHandler()?.applyConfiguration(configuration)
}

fun WebSocketSession.endSocKt(identifier: String) {
    val socKtSession = getAssignedSocKtSession() ?: return
    val socKtHandler = socKtSession.handler

    socKtHandler.removeSession(socKtSession)
}

internal fun WebSocketSession.getAssignedSocKtHandler(): SocKtHandler? {
    return SocKt.getAll().find { h -> h.listSessions().find { s -> s.webSocketSession == this } != null }
}

internal fun WebSocketSession.getAssignedSocKtSession(): SocKtSession? {
    SocKt.getAll().forEach { h ->
        val s = h.listSessions().find { s -> s.webSocketSession == this }
        if(s != null) return s
    }

    return null
}
