package dot.reyn.cobbleannouncer.config

import com.cobblemon.mod.common.api.pokemon.labels.CobblemonPokemonLabels
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.aspects.SHINY_ASPECT
import dev.vankka.enhancedlegacytext.EnhancedLegacyText
import dot.reyn.cobbleannouncer.extensions.smallCaps
import net.kyori.adventure.text.Component
import net.minecraft.server.network.ServerPlayerEntity

/**
 * Configuration for the announcer.
 */
data class AnnouncerConfig(
    val tokens: List<AdjectiveToken> = listOf(
        AdjectiveToken(
            enabled = true,
            smallCaps = true,
            criteria = mapOf(
                "aspect" to listOf(SHINY_ASPECT.aspect),
            ),
            adjective = "&#ef9f76⭐ &#e5c890Shiny &#ef9f76⭐",
        ),
        AdjectiveToken(
            enabled = true,
            smallCaps = true,
            criteria = mapOf(
                "label" to listOf(CobblemonPokemonLabels.LEGENDARY)
            ),
            adjective = "&#e78284♦ &#ea999cLegendary &#e78284♦",
        )
    ),

    val baseMessage: AnnouncerMessage = AnnouncerMessage(
        enabled = true,
        smallCaps = false,
        message = "&#414559(&#eebebe!&#414559) &#a6d189%player% &#c6d0f5has caught a %adjectives% %pokemon%&#c6d0f5!",
    )
) {
    /**
     * Returns the message to send to the player.
     */
    fun getMessage(player: ServerPlayerEntity, pokemon: Pokemon, tokens: List<AdjectiveToken>): Component {
        val token = tokens.firstOrNull { it.overrideMessage != null }
        val announcerMessage = token?.overrideMessage ?: this.baseMessage
        var text = announcerMessage.message

        // Standard placeholders
        text = text.replace("%player%", player.gameProfile.name)
        text = text.replace("%pokemon%", pokemon.species.translatedName.string)

        // Adjective placeholders
        val adjectives = tokens.joinToString(" ") { it.getText() }
        text = text.replace("%adjectives%", adjectives)

        return if (announcerMessage.smallCaps) {
            EnhancedLegacyText.get().buildComponent(text.lowercase().smallCaps()).build()
        } else {
            EnhancedLegacyText.get().buildComponent(text).build()
        }
    }

    /**
     * Returns a list of eligible tokens for the given Pokémon.
     */
    fun getEligibleTokens(pokemon: Pokemon): List<AdjectiveToken> {
        return this.tokens
            .filter { it.enabled }
            .filter { it.matches(pokemon) }
            .toList()
    }
}