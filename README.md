# tz-offset-transitions

This repository provides the information of the timezone offset transitions based on the IANA TZ database.
This information is provided as a JSON file at `gen/tzot.json`.

## Automatic Update

By a GitHub Actions workflow, the `tzot.json` is automatically updated once a month if the new IANA TZ database version is detected.


## Format of tzot.json

The `tz.json` is serialized from a Kotlin object of type `List<Zone>`.

`Zone` is defained as follows:

```kt
/** Timezone consists of a collection of timezone offset transitions. */
@Serializable
data class Zone(
    /** Name of a timezone. */
    val zone: String,

    /** Collection of timezone offset transitions. */
    val transitions: List<Transition>,
)
```

`Transition` is defained as follows:

```kt
/** Timezone offset transition between timezone offsets. */
@Serializable
data class Transition(
    /** Timestamp when a transition between timezone offsets occurs in form `2024-02-24T18:31:25Z`. */
    @SerialName("transition_timestamp")
    val transitionTimestamp: String,

    /** Amount of timezone offset before the transition in second. */
    @SerialName("offset_seconds_before")
    val offsetSecondsBefore: Int,

    /** Amount of timezone offset after the transition in second. */
    @SerialName("offset_seconds_after")
    val offsetSecondsAfter: Int,
)
```

`transition_timestamp` is a timestamp when a transition between timezone offsets occurs:


### Example

```json
[
    {
        "zone": "Asia/Aden",
        "transitions": [
            {
                "transition_timestamp": "1947-03-13T20:53:08Z",
                "offset_seconds_before": 11212,
                "offset_seconds_after": 10800
            }
        ]
    },
    {
        "zone": "America/Cuiaba",
        "transitions": [
            {
                "transition_timestamp": "1914-01-01T03:44:20Z",
                "offset_seconds_before": -13460,
                "offset_seconds_after": -14400
            },
            {
                "transition_timestamp": "1931-10-03T15:00Z",
                "offset_seconds_before": -14400,
                "offset_seconds_after": -10800
            },
            {
                "transition_timestamp": "1932-04-01T03:00Z",
                "offset_seconds_before": -10800,
                "offset_seconds_after": -14400
            },
        ...
    },
    ...
]
```
