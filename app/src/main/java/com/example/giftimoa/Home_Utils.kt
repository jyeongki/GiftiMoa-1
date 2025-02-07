package com.example.giftimoa

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.giftimoa.dto.Badge
import com.example.giftimoa.dto.Home_gift
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Home_Utils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun calDday(gifticon: Home_gift): Badge {
        //0:사용가능, 1:사용완료, 2:기간만료
        return when (gifticon.h_state) {
            1 -> {
                Badge("사용완료", "#5f5f69")

            }
            2 -> {
                Badge("기간만료", "#5f5f69")
            }
            else -> {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val due = gifticon.h_effectiveDate.split(" ")[0].format(dateFormat)
                var now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                val dueTime = dateFormat.parse(due)?.time
                val nowTime = dateFormat.parse(now)?.time

                val dDay = (dueTime!! - nowTime!!) / (24 * 60 * 60 * 1000)

                return if (dDay.toInt() == 0) {
                    Badge("오늘까지", "#FF5755")
                } else if (dDay.toInt() < 0) {
                    Badge("기간만료", "#5f5f69")
                } else {
                    var color = "#8ED2CD"
                    if (dDay <= 3) {
                        color = "#FF9796"
                    } else if (dDay <= 7) {
                        color = "#F7B733"
                    }

                    Badge("D-$dDay", color)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calState(gifticon: Home_gift): Int {
        //0:사용가능, 1:사용완료, 2:기간만료

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val due = gifticon.h_effectiveDate.split(" ")[0].format(dateFormat)
        var now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val dueTime = dateFormat.parse(due)?.time
        val nowTime = dateFormat.parse(now)?.time

        val dDay = (dueTime!! - nowTime!!) / (24 * 60 * 60 * 1000)

        return if (dDay.toInt() >= 0) {
            return 0
        } else {
            return 2
        }
    }
}