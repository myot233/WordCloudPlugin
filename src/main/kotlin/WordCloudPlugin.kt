package com.github

import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.execute
import net.mamoe.mirai.console.data.PluginDataStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.info
import java.text.SimpleDateFormat
import java.util.*

object WordCloudPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "com.github.WordCloudPlugin",
        name = "WordCloudPlugin",
        version = "0.0.1",
    ) {
        author("gsycl2004")
        info("""a plugin that can easily generate wordcloud image of group""")
    }
) {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override fun onEnable() {
        logger.info { "WordCloudPlugin Plugin loaded" }
        WordCloudPluginData.reload()
        WordCloudConfig.reload()
        CommandAliasConfig.reload()
        WordCloudCommand.register()
        this.globalEventChannel().subscribe<GroupMessageEvent> {
            val msg = it.message.contentToString()
            if (!WordCloudPluginData.hasGroup(this.group.id)) {
                WordCloudPluginData.addGroup(this.group.id)
            }
            val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
            WordCloudPluginData.getGroup(this.group.id, date)
                .add(this.message.filterIsInstance<PlainText>().joinToString {
                    msg
                })

            when (msg) {
                CommandAliasConfig.todayWordCloud -> {
                    WordCloudCommand.execute(this.toCommandSender(), CommandAliasConfig.todayWordCloud, false)
                }
                CommandAliasConfig.yesterdayWordCloud -> {
                    WordCloudCommand.execute(this.toCommandSender(), CommandAliasConfig.todayWordCloud, false)
                }
                else -> {
                    if (msg.startsWith(CommandAliasConfig.getWordCloud)) {
                        val time = msg.removePrefix(CommandAliasConfig.getWordCloud).trim().split(" ")[0]
                        WordCloudCommand.execute(this.toCommandSender(), "${CommandAliasConfig.getWordCloud} $time", false)
                    }

                }
            }
            return@subscribe ListeningStatus.LISTENING

        }

    }
}