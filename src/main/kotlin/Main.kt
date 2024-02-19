package net.jumpaku.tzot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.ZoneOffset
import java.time.zone.ZoneRulesProvider

fun main(vararg args: String) {
    if (args.size != 1) {
        error("one positional argument <output_path> is required")
    }

    val outputPath = args[0]
    val encoder = Json { prettyPrint = true }
    File(outputPath).apply {
        if (!exists()) createNewFile()
        writeText(encoder.encodeToString(collectZones()))
    }
}

/** Timezone consists of a collection of timezone offset transitions. */
@Serializable
data class Zone(
    /** Name of a timezone. */
    val zone: String,

    /** Collection of timezone offset transitions. */
    val transitions: List<Transition>,
)

/** Timezone offset transition between timezone offsets. */
@Serializable
data class Transition(
    /** Timestamp indicating when a transition between timezone offsets occurs in form `2024-02-24T18:31:25Z`. */
    @SerialName("transition_timestamp")
    val transitionTimestamp: String,

    /** Amount of timezone offset before the transition in second. */
    @SerialName("offset_seconds_before")
    val offsetSecondsBefore: Int,

    /** Amount of timezone offset after the transition in second. */
    @SerialName("offset_seconds_after")
    val offsetSecondsAfter: Int,
)


fun collectZones(): List<Zone> = ZoneRulesProvider.getAvailableZoneIds().map { id ->
    Zone(id, ZoneRulesProvider.getRules(id, false).transitions.map { transition ->
        Transition(
            transition.instant.atOffset(ZoneOffset.UTC).toString(),
            transition.offsetBefore.totalSeconds,
            transition.offsetAfter.totalSeconds
        )
    })
}

