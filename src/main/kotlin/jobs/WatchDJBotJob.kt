package jobs

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.MessageChannelBehavior
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import global.config.api.DiscordApi
import global.config.properties.GlobalProperties.CHANNEL_ID
import global.config.properties.GlobalProperties.MENTION_TARGET
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import org.example.bot.service.RandomResponseService
import org.quartz.Job
import org.quartz.JobExecutionContext
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

class WatchDJBotJob: Job  {
    val randomResponseService = RandomResponseService()

    override fun execute(context: JobExecutionContext?) {
        runBlocking {
            launch {
                val channelId = getChannelId(context)
                val channel = MessageChannelBehavior(channelId, DiscordApi.kordApi())
                val mentionTarget = getMentionTarget(context)

                val messages = channel.messages
                    .filter { message -> Clock.System.now() - message.timestamp < 16.hours }
                    .filter { message -> message.author?.isBot == true }
                    .toList()

                if (messages.count() >= 0)
                    return@launch

                channel.createMessage(randomResponseService.getRandomResponse())
                channel.createMessage("Halo halo, <@$mentionTarget>")
            }
        }
    }

    private fun getChannelId(context: JobExecutionContext?): Snowflake =
        context?.mergedJobDataMap[CHANNEL_ID] as? Snowflake
            ?: throw IllegalStateException("Channel ID is null")

    private fun getMentionTarget(context: JobExecutionContext?): String =
        context?.mergedJobDataMap[MENTION_TARGET] as? String
            ?: throw IllegalStateException("Mention target is null")

}