package com.github

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object CommandAliasConfig:AutoSavePluginConfig("alias"){
    @ValueDescription("用于给指令换个名字")
    val todayWordCloud:String by value("本日词云")
    val yesterdayWordCloud:String by value("昨日词云")
    val getWordCloud:String by value("获取词云")
    val getUserWordCloud:String by value("获取用户词云")
    val getMonthWordCloud:String by value("本月词云")
    val getUserMonthWordCloud:String by value("用户本月词云")
    val getUserTodayWordCloud:String by value("用户本日词云")
    val getUserYesterdayWordCloud:String by value("用户昨日词云")

}