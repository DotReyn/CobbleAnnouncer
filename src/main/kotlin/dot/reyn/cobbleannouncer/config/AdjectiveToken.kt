package dot.reyn.cobbleannouncer.config

import com.cobblemon.mod.common.pokemon.Pokemon
import dot.reyn.cobbleannouncer.extensions.smallCaps

/**
 * Adjective token object for announcing context.
 */
data class AdjectiveToken(
    val enabled: Boolean,
    val criteria: Map<String, List<String>>,
    val adjective: String,
    val smallCaps: Boolean,
    val overrideMessage: AnnouncerMessage? = null
) {
    /**
     * Returns the adjective with formatting.
     */
    fun getText(): String {
        return if (this.smallCaps) {
            this.adjective.lowercase().smallCaps()
        } else {
            this.adjective
        }
    }

    /**
     * Returns true if the Pokémon matches the criteria.
     */
    fun matches(pokemon: Pokemon): Boolean {
        for ((criteria, values) in this.criteria) {
            if (criteria == "aspect" && !pokemon.aspects.containsAll(values)) {
                return false
            }
            if (criteria == "label" && !pokemon.hasLabels(*values.toTypedArray())) {
                return false
            }
            if (criteria == "species" && !values.contains(pokemon.species.name)) {
                return false
            }
        }
        return true
    }
}