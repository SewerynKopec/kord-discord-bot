package global.config.api

import dev.kord.core.Kord
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import global.config.properties.GlobalProperties.DISCORD_TOKEN

object DiscordApi {
    private val TOKEN = System.getenv(DISCORD_TOKEN) ?: throw IllegalStateException("Discord token is null. Set DISCORD_TOKEN env.")

    private lateinit var kord: Kord

    fun kordApi() = kord

    init {
        runBlocking {
            launch {
                kord = DiscordApiFactory.create(TOKEN)
            }
        }
    }
}