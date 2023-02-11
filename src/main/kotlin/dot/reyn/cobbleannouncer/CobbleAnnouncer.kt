package dot.reyn.cobbleannouncer

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dot.reyn.cobbleannouncer.config.AnnouncerConfig
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.kyori.adventure.platform.fabric.FabricServerAudiences
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * Announcement sidemod for Cobblemon.
 */
class CobbleAnnouncer : ModInitializer {

    private var platform: FabricServerAudiences? = null
    private lateinit var config: AnnouncerConfig

    /**
     * Initializes the mod.
     */
    override fun onInitialize() {
        this.loadConfig()

        ServerLifecycleEvents.SERVER_STARTING.register { this.platform = FabricServerAudiences.of(it) }
        ServerLifecycleEvents.SERVER_STOPPED.register { this.platform = null }
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register { _, _, -> this.loadConfig() }

        CobblemonEvents.POKEMON_CAPTURED.subscribe { event ->
            if (this.platform == null) {
                return@subscribe
            }

            val tokens = this.config.getEligibleTokens(event.pokemon)
            if (tokens.isEmpty()) {
                return@subscribe
            }

            val message = this.config.getMessage(event.player, event.pokemon, tokens)
            this.platform!!.players().sendMessage(message)
        }
    }

    /**
     * Loads the configuration file.
     * If the config does not exist, it will be created.
     */
    private fun loadConfig() {
        val configDir = File("./config/cobbleannouncer")
        if (!configDir.exists()) {
            configDir.mkdirs()
        }

        val configFile = File(configDir, "cobbleannouncer.json")
        val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()

        if (!configFile.exists()) {
            this.config = AnnouncerConfig()
            val fileWriter = FileWriter(configFile, Charsets.UTF_8)

            gson.toJson(this.config, fileWriter)

            fileWriter.flush()
            fileWriter.close()
        } else {
            val fileReader = FileReader(configFile, Charsets.UTF_8)
            this.config = gson.fromJson(fileReader, AnnouncerConfig::class.java)
            fileReader.close()
        }
    }

}