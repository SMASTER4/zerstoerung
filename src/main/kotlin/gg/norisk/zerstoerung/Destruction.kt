package gg.norisk.zerstoerung

import gg.norisk.zerstoerung.Zerstoerung.logger
import gg.norisk.zerstoerung.config.ConfigManager
import gg.norisk.zerstoerung.serialization.BlockPosSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Colors
import net.minecraft.util.math.BlockPos
import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.core.Silk.server
import net.silkmc.silk.core.text.broadcastText
import org.apache.logging.log4j.Logger
import java.awt.Color
import java.io.File

abstract class Destruction(val name: String) {
    var isEnabled = true
    protected val configFile: File by lazy { File(ConfigManager.configFolder, "$name.json") }

    open fun init() {
        ServerTickEvents.END_WORLD_TICK.register(ServerTickEvents.EndWorldTick {
            if (isEnabled) tickServerWorld(it)
        })
    }

    open fun tickServerWorld(world: ServerWorld) {

    }

    open fun onEnable(server: MinecraftServer) {
        logger.info("enabling module $name")
        loadConfig()
        isEnabled = true
    }

    open fun onDisable() {
        logger.info("disabling module $name")
        saveConfig()
        isEnabled = false
    }

    open fun loadConfig() {}
    open fun saveConfig() {}
    open fun destroy() {}

    fun broadcastDestruction(text: Text) {
        server?.broadcastText {
            text("[") { color = Colors.LIGHT_GRAY }
            text(name) { color = Color.RED.rgb }
            text("] ") { color = Colors.LIGHT_GRAY }
            text(text, inheritStyle = true)
            text(" wurde gelöscht.")
        }
    }

    open fun commandCallback(literalCommandBuilder: LiteralCommandBuilder<ServerCommandSource>) {}

    companion object {
        fun Logger.bug(message: String) {
            if (FabricLoader.getInstance().isDevelopmentEnvironment) {
                //info(message)
            }
        }

        val JSON = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
            serializersModule = SerializersModule {
                contextual(BlockPos::class, BlockPosSerializer)
            }
        }
    }
}
