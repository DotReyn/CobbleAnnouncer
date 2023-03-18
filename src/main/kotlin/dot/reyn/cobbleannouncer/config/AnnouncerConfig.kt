package dot.reyn.cobbleannouncer.config

import com.cobblemon.mod.common.api.pokemon.labels.CobblemonPokemonLabels
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.aspects.SHINY_ASPECT
import eu.pb4.placeholders.api.TextParserUtils
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

/**
 * Configuration for the announcer.
 */
data class AnnouncerConfig(
    val tokens: List<AdjectiveToken> = listOf(
        AdjectiveToken(
            enabled = true,
            criteria = mapOf(
                "aspect" to listOf(SHINY_ASPECT.aspect),
            ),
            adjective = "<color:#ef9f76>⭐ <color:#e5c890>sʜɪɴʏ</color> ⭐</color>",
        ),
        AdjectiveToken(
            enabled = true,
            criteria = mapOf(
                "label" to listOf(CobblemonPokemonLabels.LEGENDARY)
            ),
            adjective = "<color:#e78284>♦ <color:#ea999c>ʟᴇɢᴇɴᴅᴀʀʏ</color> ♦</color>",
        )
    ),

    val baseMessage: AnnouncerMessage = AnnouncerMessage(
        enabled = true,
        message = "<color:#414559>(<color:#eebebe>!</color>)</color> <color:#a6d189>%player%</color> <color:#c6d0f5>has caught a %adjectives% %pokemon%</color><color:#c6d0f5>!</color>",
    )
) {
    /**
     * Returns the message to send to the player.
     */
    fun getMessage(player: ServerPlayerEntity, pokemon: Pokemon, tokens: List<AdjectiveToken>): Text {
        val token = tokens.firstOrNull { it.overrideMessage != null }
        val announcerMessage = token?.overrideMessage ?: this.baseMessage
        var text = announcerMessage.message

        // Standard placeholders
        text = text.replace("%player%", player.gameProfile.name)
        text = text.replace("%pokemon%", pokemon.species.translatedName.string)

        // Adjective placeholders
        val adjectives = tokens.joinToString(" ") { it.getText() }
        text = text.replace("%adjectives%", adjectives)
        return TextParserUtils.formatNodes(text).toText()
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