package com.github

import io.ktor.client.request.forms.*
import org.ktorm.database.Database
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*

object WordCloudTable:Table<Text>("wordcloud"){
    val sender_id = long("sender_id").bindTo { it.senderId }
    val group_id = long("group_id").bindTo { it.groupId }
    val text = text("content").bindTo { it.text }
    val time = date("time").bindTo { it.time }

    fun connect(): EntitySequence<Text, WordCloudTable> {
        val dataBase = Database.connect("jdbc:sqlite:data/wordCloud.db").apply {
            this.useConnection {
                val sql = """
                    CREATE TABLE IF NOT EXISTS "wordcloud"(
                        sender_id LONG,
                        group_id LONG,
                        content TEXT,
                        TIME TIME
                    )
                """.trimIndent()
                it.prepareStatement(sql).execute()
            }

        }
        return dataBase.sequenceOf(WordCloudTable)
    }
}
