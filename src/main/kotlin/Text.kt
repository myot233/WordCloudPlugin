package com.github

import org.ktorm.entity.Entity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

interface Text:Entity<Text>{
    var senderId:Long
    var groupId:Long
    var text:String
    var time: LocalDate

    companion object:Entity.Factory<Text>()
}