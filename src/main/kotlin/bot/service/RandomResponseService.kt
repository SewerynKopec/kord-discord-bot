package org.example.bot.service

import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File

class RandomResponseService(
    val directory: String? = null
) {

    data class Response(val title: String, val body: String)

    companion object {
        const val DEFAULT_DIR_PATH = "/responses"
        val LOG = KotlinLogging.logger {  }
    }

    fun getRandomResponse(): String = loadResponses().random().let { response ->
        LOG.info { "Loading response: \"${response.title}\"" }
        return@let response.body
    }

    fun loadResponses(): List<Response> {
        val directory = File(directory ?: DEFAULT_DIR_PATH)
        require(directory.exists() && directory.isDirectory) { "Directory $DEFAULT_DIR_PATH does not exist." }

        val files = directory.listFiles()

        if (files.isEmpty()) {
            LOG.warn { "No files files found." }
        }

        return files.filter { file -> file.name.endsWith(".txt")}.mapNotNull { file ->
            val response = file.readText()
            if (response.isEmpty()) {
                LOG.warn { "file.name is empty. It won't be loaded into the response pool." }
                return@mapNotNull null
            }
            return@mapNotNull Response(file.name.substringBefore(".txt"), response)
        }
    }

}