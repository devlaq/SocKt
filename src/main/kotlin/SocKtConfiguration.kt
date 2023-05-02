class SocKtConfiguration {

    /**
     * SocKtSession create/end handler
     */
    var sessionCreate: (suspend (SocKtSession) -> Unit)? = null
    var sessionEnd: (suspend (SocKtSession) -> Unit)? = null
}
