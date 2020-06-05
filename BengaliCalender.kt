package xyz.alap.alapapp.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.floor

class BengaliCalender {
    private val bengaliMonths = arrayOf(
        "বৈশাখ",
        "জ্যৈষ্ঠ",
        "আষাঢ়",
        "শ্রাবণ",
        "ভাদ্র",
        "আশ্বিন",
        "কার্তিক",
        "অগ্রহায়ণ",
        "পৌষ",
        "মাঘ",
        "ফাল্গুন",
        "চৈত্র"
    )
    private val bengaliWeekDays =
        arrayOf("রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার", "শনিবার")
    private val bengaliSession = arrayOf("গ্রীষ্ম", "বর্ষা", "শরৎ", "হেমন্ত", "শীত", "বসন্ত")
    private val totalMonthDays = arrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 29, 30)

    private fun isLeapYear(year: Int = Calendar.getInstance().get(Calendar.YEAR)): Boolean {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)
    }

    private fun diffInDays(yearOne: Long, yearTwo: Long): Long {
        val diff = yearOne - yearTwo
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    fun getBengaliDate(date: Date = Date()): BengaliDate {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date.time
        return getBengaliDate(calendar)
    }

    fun getBengaliDate(timestamp: Long = Date().time): BengaliDate {
        return getBengaliDate(Date(timestamp))
    }

    fun getBengaliDate(calendar: Calendar): BengaliDate {
        val month = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)
        val days = calendar.get(Calendar.DAY_OF_MONTH)

        if (isLeapYear(year)) {
            totalMonthDays[10] = 30
        }

        if (month < 3 || (month == 3 && days < 14)) {
            year -= 1
        }
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val bengaliLastDate = dateFormat.parse("13/04/${year}")
        val bengaliYear = year - 593
        var dayRemaining = diffInDays(calendar.timeInMillis, bengaliLastDate?.time ?: Date().time)
        println(dayRemaining)
        var bengaliMonthIndex = 0
        for ((i, _) in bengaliMonths.withIndex()) {
            if (dayRemaining <= totalMonthDays[i]) {
                bengaliMonthIndex = i
                break
            }
            dayRemaining -= totalMonthDays[i]
        }
        val bengaliSession = bengaliSession[floor((bengaliMonthIndex / 2).toDouble()).toInt()]
        return BengaliDate(
            toBengaliString(dayRemaining.toInt()),
            bengaliMonths[bengaliMonthIndex],
            toBengaliString(bengaliYear),
            bengaliSession,
            bengaliWeekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1]
        )
    }

    private fun toBengaliString(number: Int): String {
        return number.toString().replace("0".toRegex(), "০").replace("1".toRegex(), "১")
            .replace("2".toRegex(), "২")
            .replace("3".toRegex(), "৩").replace("4".toRegex(), "৪").replace("5".toRegex(), "৫")
            .replace("6".toRegex(), "৬")
            .replace("7".toRegex(), "৭").replace("8".toRegex(), "৮").replace("9".toRegex(), "৯")

    }

    data class BengaliDate(
        val day: String,
        val month: String,
        val year: String,
        val session: String,
        val weekDay: String
    ) {
        fun format(pattern: String): String {
            return pattern.replace("dd", day).replace("mm", month).replace("yyyy", year)
                .replace("ss", session).replace("DD", weekDay)
        }

        override fun toString(): String {
            return "BengaliDate(day=$day, month=$month, year=$year, session=$session, weekDay=$weekDay)"
        }
    }

}

fun main() {
    println(BengaliCalender().getBengaliDate(Date()).format("DD, dd mm, yyyy"))
}