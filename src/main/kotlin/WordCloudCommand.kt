package com.github

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.GroupTempCommandSender
import net.mamoe.mirai.console.command.MemberCommandSender
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.ktorm.dsl.*
import org.ktorm.entity.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.text.DateFormatter

object WordCloudCommand : CompositeCommand(
    WordCloudPlugin,
    "wordcloud",
    "w"
) {
    @SubCommand("获取词云")
    suspend fun MemberCommandSender.genGroup(from: String, to: String) {
        sendMessage(
            genGroup(
                LocalDate.parse(from),
                LocalDate.parse(to)
            )
        )
    }

    @SubCommand("获取用户词云")
    suspend fun MemberCommandSender.genPerson(user: User, from: String, to: String) {
        sendMessage(
            genPerson(
                user,
                LocalDate.parse(from),
                LocalDate.parse(to)
            )
        )
    }

    @SubCommand("本月词云")
    suspend fun MemberCommandSender.genGroupMonth() {
        sendMessage(genGroup(LocalDate.now().withDayOfMonth(1), LocalDate.now().let {
            it.withDayOfMonth(it.lengthOfMonth())
        }))
    }

    @SubCommand("昨日词云")
    suspend fun MemberCommandSender.genGroupYesterday() {
        sendMessage(genGroup(LocalDate.now().minusDays(1), LocalDate.now().minusDays(1)))
    }

    @SubCommand("个人昨日词云")
    suspend fun MemberCommandSender.genPersonYesterday(user: User) {
        sendMessage(genPerson(user, LocalDate.now().minusDays(1), LocalDate.now().minusDays(1)))
    }

    @SubCommand("个人本月词云")
    suspend fun MemberCommandSender.genPersonMonth(user: User) {
        sendMessage(genPerson(user, LocalDate.now().withDayOfMonth(1), LocalDate.now().let {
            it.withDayOfMonth(it.lengthOfMonth())
        }))
    }

    @SubCommand("个人本日词云")
    suspend fun MemberCommandSender.genPersonToday(user: User) {
        sendMessage(genPerson(user, LocalDate.now(), LocalDate.now()))
    }

    @SubCommand("本日词云")
    suspend fun MemberCommandSender.genGroupToday() {
        sendMessage(genGroup(LocalDate.now(), LocalDate.now()))
    }


    private suspend fun MemberCommandSender.genGroup(from: LocalDate, to: LocalDate): Image {
        val sequence = WordCloudTable.connect()
        val texts = sequence.filter {
            it.group_id eq this.group.id
        }.filter {
            it.time between from..to
        }.map { it.text }.toList()
        return group.uploadImage(WordCloudUtils.generateWordCloud(texts).toExternalResource("png"))
    }

    private suspend fun MemberCommandSender.genPerson(
        user: User,
        from: LocalDate,
        to: LocalDate
    ): Image {
        val sequence = WordCloudTable.connect()
        val texts = sequence.filter {
            it.sender_id eq user.id
        }.filter {
            it.time between from..to
        }.map { it.text }.toList()
        return group.uploadImage(WordCloudUtils.generateWordCloud(texts).toExternalResource("png"))
    }

}