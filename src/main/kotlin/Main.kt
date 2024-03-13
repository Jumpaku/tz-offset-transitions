package net.jumpaku.tzot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.zone.ZoneOffsetTransitionRule
import java.time.zone.ZoneRulesProvider

fun main(vararg args: String) {
    if (args.size != 1) {
        error("one positional argument <output_path> is required")
    }

    val outputPath = args[0]
    val encoder = Json { prettyPrint = true }
    File(outputPath).apply {
        if (!exists()) createNewFile()
        val zones = collectZones()
        writeText(encoder.encodeToString(zones))
    }
}


fun collectZones(): List<Zone> = ZoneRulesProvider.getAvailableZoneIds().map { id ->
    ZoneRulesProvider.getRules(id, false).run {
        Zone(
            id,
            transitions.map { t ->
                Transition(
                    t.instant.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    t.offsetBefore.totalSeconds,
                    t.offsetAfter.totalSeconds
                )
            },
            transitionRules.map { r ->
                val offsetBefore = r.offsetBefore.totalSeconds
                val offsetAfter = r.offsetAfter.totalSeconds
                val transitionAt = when (r.timeDefinition!!) {
                    ZoneOffsetTransitionRule.TimeDefinition.UTC -> ZoneOffset.UTC
                    ZoneOffsetTransitionRule.TimeDefinition.WALL -> r.offsetBefore
                    ZoneOffsetTransitionRule.TimeDefinition.STANDARD -> r.standardOffset
                }.let { offset ->
                    when {
                        r.isMidnightEndOfDay -> "24:00:00$offset"
                        else -> r.localTime.atOffset(offset).format(DateTimeFormatter.ISO_OFFSET_TIME)
                    }
                }
                val month = r.month.value
                val dow = r.dayOfWeek?.value
                val indicator = r.dayOfMonthIndicator
                when {
                    dow != null && r.dayOfMonthIndicator > 0 ->
                        Rule.WeekDayPositive(offsetBefore, offsetAfter, month, transitionAt, dow, indicator)

                    dow != null && r.dayOfMonthIndicator < 0 ->
                        Rule.WeekDayNegative(offsetBefore, offsetAfter, month, transitionAt, dow, -indicator)

                    dow == null && r.dayOfMonthIndicator > 0 ->
                        Rule.MonthDayPositive(offsetBefore, offsetAfter, month, transitionAt, indicator)

                    dow == null && r.dayOfMonthIndicator < 0 ->
                        Rule.MonthDayNegative(offsetBefore, offsetAfter, month, transitionAt, -indicator)

                    else -> error("unexpected rule")
                }
            },
        )
    }
}


/** Timezone consists of a collection of timezone offset transitions and a collection of transition rules. */
@Serializable
data class Zone(
    /** Name of the timezone. */
    val id: String,

    /** Collection of timezone offset transitions. */
    val transitions: List<Transition>,

    /** Collection of transition rules. */
    val rules: List<Rule>,
)

/** Timezone offset transition between timezone offsets. */
@Serializable
data class Transition(
    /** Timestamp indicating when a transition between timezone offsets occurs, which is a text such as `2024-02-24T18:31:25Z`. */
    @SerialName("transition_timestamp")
    val transitionTimestamp: String,

    /** Amount of timezone offset before the transition in second. */
    @SerialName("offset_seconds_before")
    val offsetSecondsBefore: Int,

    /** Amount of timezone offset after the transition in second. */
    @SerialName("offset_seconds_after")
    val offsetSecondsAfter: Int,
)

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
    ) : Rule() {
    }

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

