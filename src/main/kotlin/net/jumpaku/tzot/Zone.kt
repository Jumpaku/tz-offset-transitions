package net.jumpaku.tzot

import kotlinx.serialization.Serializable


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
