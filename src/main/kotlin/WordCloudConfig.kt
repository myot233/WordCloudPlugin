package com.github

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object WordCloudConfig : AutoSavePluginConfig("config") {
    @ValueDescription("设置背景宽度")
    val width:Int by value(1000)

    @ValueDescription("设置背景高度")
    val height:Int by value(1000)

    @ValueDescription("设置最小文字大小")
    val minFontSize:Int by value(10)

    @ValueDescription("设置最大文字大小")
    val maxFontSize:Int by value(40)


    @ValueDescription("设置词云的背景模式,可选 CIRCLE,IMAGE两种")
    val backgroundMode:BackGround by value(BackGround.CIRCLE)

    @ValueDescription("若背景模式为IMAGE,则需要此项来指定背景图片,图片中填充文字之外的部分需用透明色表示")
    val imagePath:String? by value()

    @ValueDescription("给/WordCloud设置别名")
    val alias: MutableList<String> by value()

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