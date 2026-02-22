package bot

import bot.handlers.Handlers
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import global.config.api.DiscordApi

class DiscordBot {
    init {
        runBlocking {
            launch {
                launch()
            }
        }
    }

    suspend fun launch(){
        val kord = DiscordApi.kordApi()

        Handlers().runWatchDJBot()

        kord.login {
            // we need to specify this to receive the content of messages
            @OptIn(PrivilegedIntent::class)
            intents += Intent.MessageContent
        }
    }

}