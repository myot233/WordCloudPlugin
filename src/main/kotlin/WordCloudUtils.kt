package com.github

import com.huaban.analysis.jieba.JiebaSegmenter
import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.bg.CircleBackground
import com.kennycason.kumo.bg.PixelBoundaryBackground
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.palette.ColorPalette
import java.awt.Color
import java.awt.Dimension
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

object WordCloudUtils {
    fun generateWordCloud(text: MutableList<String>): ByteArray {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(300)
        frequencyAnalyzer.setMinWordLength(2)
        frequencyAnalyzer.setWordTokenizer { it ->
            val segmenter = JiebaSegmenter()
            segmenter.process(it, JiebaSegmenter.SegMode.INDEX).map {
                it.word.trim()
            }
        }
        val wordFrequencies = frequencyAnalyzer.load(text)
        val dimension = Dimension(WordCloudConfig.width, WordCloudConfig.height)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.setPadding(2)
        wordCloud.setKumoFont(
            if (WordCloudConfig.fontPath == "default")
                KumoFont(WordCloudPlugin.getResourceAsStream("萝莉体.ttf"))
            else
                KumoFont(File(WordCloudConfig.fontPath))
        )
        val colors = WordCloudConfig.colorList
        wordCloud.setBackground(when(WordCloudConfig.backgroundMode)
        {
            BackGround.CIRCLE -> CircleBackground(((WordCloudConfig.height+WordCloudConfig.width)/4))
            BackGround.IMAGE  -> PixelBoundaryBackground(File(WordCloudConfig.imagePath!!))
        }
        )
        wordCloud.setBackgroundColor(Color(0xFFFFFF))
        wordCloud.setColorPalette(
            ColorPalette(colors.map { it.toIntOrNull(16)?.let { it1 -> Color(it1) } })
        )
        wordCloud.setFontScalar(LinearFontScalar(20, 100))
        wordCloud.build(wordFrequencies)
        val stream = ByteArrayOutputStream()
        wordCloud.writeToStreamAsPNG(stream)
        return stream.toByteArray()
    }

    fun getBeforeDay(date: Date = Date(), before: Int = 1): Date {
        var date = date
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, -before)
        date = calendar.time
        return date
    }
}