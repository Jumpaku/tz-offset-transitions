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

@Serializable
data class Transition(
    @SerialName("transition_timestamp")
    val transitionTimestamp: String,
    @SerialName("offset_seconds_before")
    val offsetSecondsBefore: Int,
    @SerialName("offset_seconds_after")
    val offsetSecondsAfter: Int,
)

@Serializable
data class Zone(val zone: String, val version: String, val transitions: List<Transition>)

fun collectZones(): List<Zone> = ZoneRulesProvider.getAvailableZoneIds().map { id ->
    val (version, rules) = ZoneRulesProvider.getVersions(id).lastEntry()
    return@map Zone(id, version, rules.transitions.map { transition ->
        Transition(
            transition.instant.atOffset(ZoneOffset.UTC).toString(),
            transition.offsetBefore.totalSeconds,
            transition.offsetAfter.totalSeconds
        )
    })
}

