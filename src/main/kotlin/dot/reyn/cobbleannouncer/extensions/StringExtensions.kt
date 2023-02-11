package dot.reyn.cobbleannouncer.extensions

val SMALL_CAPS_MAP = mapOf(
    'a' to 'ᴀ',
    'b' to 'ʙ',
    'c' to 'ᴄ',
    'd' to 'ᴅ',
    'e' to 'ᴇ',
    'f' to 'ғ',
    'g' to 'ɢ',
    'h' to 'ʜ',
    'i' to 'ɪ',
    'j' to 'ᴊ',
    'k' to 'ᴋ',
    'l' to 'ʟ',
    'm' to 'ᴍ',
    'n' to 'ɴ',
    'o' to 'ᴏ',
    'p' to 'ᴘ',
    'q' to 'ǫ',
    'r' to 'ʀ',
    's' to 's',
    't' to 'ᴛ',
    'u' to 'ᴜ',
    'v' to 'ᴠ',
    'w' to 'ᴡ',
    'x' to 'x',
    'y' to 'ʏ',
    'z' to 'ᴢ',
)

/**
 * Converts the characters in a string to the small caps characters.
 */
fun String.smallCaps(): String {
    val builder = StringBuilder()
    var hitPlaceholder = false

    var index = 0
    while (index < this.length) {
        val char = this[index]

        // Check for hex color codes
        if (char == '&' && this[index + 1] == '#') {
            val hexCode = this.substring(index, index + 8)
            builder.append(hexCode)
            index += 8
            continue
        }

        // Check for start or end of a placeholder
        if (char == '%') {
            hitPlaceholder = !hitPlaceholder
        }

        // If we're inside a placeholder, appends the original character and continues;
        if (hitPlaceholder) {
            builder.append(char)
            index++
            continue
        }

        // Otherwise if there's a small caps version of the character, append it or the original character
        builder.append(SMALL_CAPS_MAP[char] ?: char)
        index++
    }
    return builder.toString()
}