package global.config.api

import dev.kord.core.Kord

object DiscordApiFactory {

    suspend fun create(token: String): Kord = Kord(token)

}