package com.github

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object TimerConfig:AutoSavePluginConfig("timer"){
    @ValueDescription("这里用来存放定时任务")
    val tasks:MutableList<WordCloudTask> by value(
        mutableListOf(
            WordCloudTask(
                "14:00:00",
                114514L,
                "hello",
                "today"
            )
        )
    )
}


@kotlinx.serialization.Serializable
data class WordCloudTask(val time:String,val group:Long,val text:String,val type:String)


object WordCloudConfig : AutoSavePluginConfig("config") {
    @ValueDescription("设置背景宽度")
    val width:Int by value(1000)

    @ValueDescription("设置背景高度")
    val height:Int by value(1000)

    @ValueDescription("设置最小文字大小")
    val minFontSize:Int by value(10)

    @ValueDescription("设置最大文字大小")
    val maxFontSize:Int by value(40)

    @ValueDescription("将自动过滤匹配到的内容")
    val regexs:List<String> by value(listOf(
        "[1-9][0-9]{4,14}",
        """\[.*\]"""
    ))

    @ValueDescription("设置词云的背景模式,可选 CIRCLE,IMAGE两种")
    val backgroundMode:BackGround by value(BackGround.CIRCLE)

    @ValueDescription("若背景模式为IMAGE,则需要此项来指定背景图片,图片中填充文字之外的部分需用透明色表示")
    val imagePath:String? by value()

    @ValueDescription("给/WordCloud设置别名")
    val alias: MutableList<String> by value(mutableListOf("词云"))

    @ValueDescription("选择分词器,可选 JIEBA,KUMO 两种")
    val tokenizer:Tokenizers by value(Tokenizers.KUMO)

    @ValueDescription("设置词云的字体")
    val fontPath: String by value("default")

    @ValueDescription("设置词云文字可选的颜色,用16进制表示")
    val colorList: MutableList<String> by value(
        mutableListOf(
            "0000FF",
            "40D3F1",
            "40C5F1",
            "40AAF1",
            "408DF1",
            "4055F1"
        )
    )
}