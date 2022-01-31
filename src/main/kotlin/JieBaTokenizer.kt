package com.github

import com.huaban.analysis.jieba.JiebaSegmenter
import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer

class JieBaTokenizer:WordTokenizer{
    override fun tokenize(sentence: String?): MutableList<String> {
        val segmenter = JiebaSegmenter()
        return segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).map {
            it.word.trim()
        }.toMutableList()
    }


}