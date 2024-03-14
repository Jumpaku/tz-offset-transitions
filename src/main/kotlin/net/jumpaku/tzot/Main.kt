package net.jumpaku.tzot

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

