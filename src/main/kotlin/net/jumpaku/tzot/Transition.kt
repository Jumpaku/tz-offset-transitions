package net.jumpaku.tzot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


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
