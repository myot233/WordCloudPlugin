package com.github

import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.data.PluginDataStorage
import net.mamoe.mirai.console.plugin.id
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.code.MiraiCode.deserializeMiraiCode
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import net.mamoe.mirai.utils.info
import org.ktorm.dsl.KtormDsl
import org.ktorm.entity.add
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

object WordCloudPlugin : KotlinPlugin(JvmPluginDescription(
    id = "com.github.WordCloudPlugin",
    name = "WordCloudPlugin",
    version = "0.0.6",
) {
    author("gsycl2004")
    info("""a plugin that can easily generate wordcloud image of group""")
}) {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override fun onEnable() {
        Class.forName("org.sqlite.JDBC")
        val sequence = WordCloudTable.connect()

        logger.info { "WordCloudPlugin Plugin loaded" }
        WordCloudConfig.reload()
        WordCloudCommand.register()
        CommandAliasConfig.reload()
        globalEventChannel().subscribe<GroupMessageEvent> {
            val string = message.contentToString()
            var containOne = false
            WordCloudConfig.regexs.forEach {
                containOne = containOne || string.contains(it.toRegex())
            }

            if (containOne) return@subscribe ListeningStatus.LISTENING
            sequence.add(Text {
                senderId = sender.id
                groupId = group.id
                text = string
                time = LocalDate.now()
            })
            return@subscribe ListeningStatus.LISTENING
        }
        globalEventChannel().subscribe<GroupMessageEvent> {
            @OptIn(ExperimentalCommandDescriptors::class, ConsoleExperimentalApi::class)
            when (val msg = message.serializeToMiraiCode()) {
                CommandAliasConfig.todayWordCloud -> {
                    WordCloudCommand.execute(this.toCommandSender(), "本日词云", false)
                }
                CommandAliasConfig.yesterdayWordCloud -> {
                    WordCloudCommand.execute(this.toCommandSender(), "昨日词云", false)
                }
                CommandAliasConfig.getMonthWordCloud -> {
                    WordCloudCommand.execute(this.toCommandSender(), "本月词云", false)
                }
                else -> {
                    val sender = this.toCommandSender()

                    val processed = if (msg.startsWith(CommandAliasConfig.getWordCloud)) {
                        msg.replace(CommandAliasConfig.getWordCloud, "获取词云").deserializeMiraiCode()
                    }else if (msg.startsWith(CommandAliasConfig.getUserWordCloud)){
                        msg.replace(CommandAliasConfig.getUserWordCloud, "获取用户词云").deserializeMiraiCode()
                    }else if (msg.startsWith(CommandAliasConfig.getUserMonthWordCloud)){
                        msg.replace(CommandAliasConfig.getUserMonthWordCloud, "个人本月词云").deserializeMiraiCode()
                    }else if (msg.startsWith(CommandAliasConfig.getUserTodayWordCloud)){
                        msg.replace(CommandAliasConfig.getUserTodayWordCloud, "个人本日词云").deserializeMiraiCode()
                    }else if (msg.startsWith(CommandAliasConfig.getUserYesterdayWordCloud)){
                        msg.replace(CommandAliasConfig.getUserYesterdayWordCloud, "个人昨日词云").deserializeMiraiCode()
                    }


                    else{
                        "".deserializeMiraiCode()
                    }
                    val result = CommandManager.executeCommand(toCommandSender(), WordCloudCommand, processed, false)
                    if (result.exception != null){
                        result.exception!!.printStackTrace()
                    }
                }
            }
            return@subscribe ListeningStatus.LISTENING
        }
    }


//
}