object SocKt {

    private val handlers = mutableListOf<SocKtHandler>()

    fun getAll(): List<SocKtHandler> {
        return handlers.toList()
    }

    fun get(identifier: String): SocKtHandler? {
        return handlers.find { it.identifier == identifier }
    }

    fun register(handler: SocKtHandler) {
        handlers.add(handler)
    }

    fun remove(identifier: String) {
        handlers.removeIf { it.identifier == identifier }
    }

}
