package dot.reyn.cobbleannouncer.config

/**
 * Represents a message that can be sent by the announcer.
 */
data class AnnouncerMessage(
    val enabled: Boolean,
    val message: String,
)