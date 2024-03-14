package net.jumpaku.tzot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/** Rule to generate yearly transitions in the future. */
@Serializable
sealed class Rule {

    /** Amount of timezone offset before the transition in second. */
    abstract val offsetSecondsBefore: Int

    /** Amount of timezone offset after the transition in second. */
    abstract val offsetSecondsAfter: Int

    /** Month of the month-day, from 1 (January) to 12 (December). */
    abstract val month: Int

    /** Time of a day with offset such as "12:34:56+09:00". The format is hh:mm:ss±hh:mm. */
    abstract val offsetTime: String


    /**
     * Rule to generate yearly transitions as follows:
     * Given a year, the transition occurs at a given offset-time on the first occurrence of a given day-of-week on or after a given month-day in the year.
     */
    @Serializable
    class WeekDayPositive(

        @SerialName("offset_seconds_before")
        override val offsetSecondsBefore: Int,

        @SerialName("offset_seconds_after")
        override val offsetSecondsAfter: Int,

        override val month: Int,

        @SerialName("offset_time")
        override val offsetTime: String,

        /** Day-of-week, from 1 (Monday) to 7 (Sunday). */
        @SerialName("day_of_week")
        val dayOfWeek: Int,

        /** Days counting from the first of the month, which from 1 to 31. */
        val days: Int,
    ) : Rule()

    /**
     * Rule to generate yearly transitions as follows:
     * Given a year, the transition occurs at a given offset-time on the first occurrence of a given day-of-week on or before a given month-day in the year.
     */
    @Serializable
    class WeekDayNegative(
        @SerialName("offset_seconds_before")
        override val offsetSecondsBefore: Int,

        @SerialName("offset_seconds_after")
        override val offsetSecondsAfter: Int,

        override val month: Int,

        @SerialName("offset_time")
        override val offsetTime: String,

        /** Day-of-week number from 1 (Monday) to 7 (Sunday). */
        @SerialName("day_of_week")
        val dayOfWeek: Int,

        /** Days counting from the last of the month, which from 1 to 28. */
        @SerialName("days_from_last")
        val daysFromLast: Int,
    ) : Rule()

    /**
     * Rule to generate yearly transitions as follows:
     * Given a year, the transition occurs at a given offset-time on a given month-day in the year.
     */
    @Serializable
    class MonthDayPositive(
        @SerialName("offset_seconds_before")
        override val offsetSecondsBefore: Int,

        @SerialName("offset_seconds_after")
        override val offsetSecondsAfter: Int,

        override val month: Int,

        @SerialName("offset_time")
        override val offsetTime: String,

        /** Days counting from the first of the month, which from 1 to 31. */
        val days: Int,
    ) : Rule()

    /**
     * Rule to generate yearly transitions as follows:
     * Given a year, the transition occurs at a given offset-time on a given month-day in the year.
     */
    @Serializable
    class MonthDayNegative(
        @SerialName("offset_seconds_before")
        override val offsetSecondsBefore: Int,

        @SerialName("offset_seconds_after")
        override val offsetSecondsAfter: Int,

        override val month: Int,

        @SerialName("offset_time")
        override val offsetTime: String,

        /** Days counting from the last of the month, which from 1 to 28. */
        @SerialName("days_from_last")

        val daysFromLast: Int,
    ) : Rule()
}

