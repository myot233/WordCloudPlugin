package com.github

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object WordCloudCommand : CompositeCommand(
    WordCloudPlugin,
    "WordCloud",
    description = "the command of wordCloud",
    secondaryNames = WordCloudConfig.alias.toTypedArray()
) {
    @SubCommand("本日词云")
    @Description("获取今天的词云")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.todayWordCloud() {
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val byte = WordCloudUtils.generateWordCloud(WordCloudPluginData.getGroup(this.fromEvent.group.id, date))

        val exRes = byte.toExternalResource("png")
        val image = this.fromEvent.group.uploadImage(exRes)
        sendMessage(image)

        withContext(Dispatchers.IO) {
            exRes.close()
        }
    }

    @SubCommand("昨日词云")
    @Description("获取昨天的词云")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.yesterdayWordCloud() {

        val date = SimpleDateFormat("yyyy-MM-dd").format(WordCloudUtils.getBeforeDay(Date(),-1))
        val byte = WordCloudUtils.generateWordCloud(WordCloudPluginData.getGroup(this.fromEvent.group.id, date))
        val exRes = byte.toExternalResource("png")
        val image = this.fromEvent.group.uploadImage(exRes)
        sendMessage(image)

        withContext(Dispatchers.IO) {
            exRes.close()
        }
    }

    @SubCommand("获取词云")
    @Description("获取指定某天的词云 如2022-01-30就是获取22年1月30号的词云")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.findWordCloud(time:String) {
        val byte = WordCloudUtils.generateWordCloud(WordCloudPluginData.getGroup(this.fromEvent.group.id, time))
        val exRes = byte.toExternalResource("png")
        val image = this.fromEvent.group.uploadImage(exRes)
        sendMessage(image)

        withContext(Dispatchers.IO) {
            exRes.close()
        }
    }

    @SubCommand("帮助")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.help(){
        sendMessage(this@WordCloudCommand.usage)
    }


}