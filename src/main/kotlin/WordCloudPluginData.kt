package com.github

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object WordCloudPluginData: AutoSavePluginData("message") {
    private val groups:MutableMap<Long,MutableMap<String,MutableList<String>>> by value()
    fun hasGroup(id: Long): Boolean {
        return !groups.keys.none { it == id }
    }

    fun addGroup(id: Long){
        groups[id] = HashMap()
    }


    fun getGroup(id:Long,data:String): MutableList<String> {
        if (groups[id]!!.keys.none{ it == data}){
            groups[id]!![data] = ArrayList()
        }
        return groups[id]!![data]!!
    }
}