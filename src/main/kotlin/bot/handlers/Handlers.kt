package bot.handlers

import dev.kord.core.Kord
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import global.config.properties.GlobalProperties.CHANNEL_ID
import io.github.oshai.kotlinlogging.KotlinLogging
import jobs.WatchDJBotJob
import global.config.api.DiscordApi
import global.config.properties.GlobalProperties.MENTION_TARGET
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory

class Handlers {

    companion object {
        val log = KotlinLogging.logger { }
        val kord: Kord = DiscordApi.kordApi()

        const val WATCH_DJ_BOT_CRON = "0 0/2 * * * ?"
//        const val WATCH_DJ_BOT_CRON = "0 3 8-16 ? * MON-FRI"
        val DJ_MENTION_ID: String = System.getenv("DJ_MENTION_ID")
    }

    fun runWatchDJBot() {
        kord.on<MessageCreateEvent> {
            if(message.author?.isBot == true)
                return@on
            if (message.content != "!watch")
                return@on
            log.info { "Starting watch dj bot" }
            scheduleWatchDJBot(message.channel)
        }
    }

    private fun scheduleWatchDJBot(channel: MessageChannelBehavior) {
        val schedulerFactory = StdSchedulerFactory()
        val scheduler = schedulerFactory.scheduler
        scheduler.start()

        val jobData = JobDataMap().apply {
            put(CHANNEL_ID, channel.id)
            put(MENTION_TARGET, DJ_MENTION_ID)
        }

        val job = JobBuilder.newJob(WatchDJBotJob::class.java)
            .withIdentity("myJob", "group1")
            .setJobData(jobData)
            .build()


        val trigger = TriggerBuilder.newTrigger()
            .withSchedule(CronScheduleBuilder.cronSchedule(WATCH_DJ_BOT_CRON))
            .build()

        scheduler.scheduleJob(job, trigger)
    }
}