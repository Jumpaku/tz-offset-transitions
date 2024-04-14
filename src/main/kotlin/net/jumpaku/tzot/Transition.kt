package net.jumpaku.tzot

import kotlinx.serialization.Serializable


/** Timezone offset transition between timezone offsets. */
@Serializable
data class Transition(

    /** Timestamp indicating when a transition between timezone offsets occurs, which is a text such as `2024-02-24T18:31:25Z`. */
    val transitionTimestamp: String,

    /** Amount of timezone offset before the transition in second. */
    val offsetSecondsBefore: Int,

    /** Amount of timezone offset after the transition in second. */
    val offsetSecondsAfter: Int,
)
