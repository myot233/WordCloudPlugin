package com.github

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object CommandAliasConfig:AutoSavePluginConfig("alias"){
    @ValueDescription("用于给指令换个名字")
    val todayWordCloud:String by value("本日词云")
    val yesterdayWordCloud:String by value("昨日词云")
    val getWordCloud:String by value("获取词云")

}