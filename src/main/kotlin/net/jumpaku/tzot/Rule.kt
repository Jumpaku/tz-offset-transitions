package net.jumpaku.tzot

import kotlinx.serialization.Serializable


/**
 * Rule to generate yearly transitions as follows:
 * Given a year, the transition occurs at a given offset-time on the first occurrence of a given day-of-week on or after a given month-day in the year.
 */
@Serializable
data class Rule(

    /** Amount of timezone offset before the transition in second. */
    val offsetSecondsBefore: Int,

    /** Amount of timezone offset after the transition in second. */
    val offsetSecondsAfter: Int,

    /** Month of the month-day, from 1 (January) to 12 (December). */
    val month: Int,

    /** Base day of month, on or after which the day-of-week is seeked. It is from 1 to 31. */
    val baseDay: Int,

    /** Day-of-week, from 1 (Monday) to 7 (Sunday). */
    val dayOfWeek: Int,

    /** Time of a day with offset such as "12:34:56+09:00". The format is hh:mm:ss±hh:mm. */
    val offsetTime: String,
)

