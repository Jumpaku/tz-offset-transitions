# tz-offset-transitions

## Overview

This repository provides information about timezone offset transitions based on the IANA TZ database.
The information is available in a JSON file located at `gen/tzot.json`.


## What timezone offset transition is

A timezone is a geographic region of the Earth that observes the same standard time, encompassing one or more timezone offsets.
The timezone offset represents the difference between the local time in a specific timezone and the Coordinated Universal Time (UTC).
A timezone offset transition indicates a shift between different timezone offsets within a particular timezone, which is often prompted by factors like daylight saving time adjustments or political changes in laws.

The IANA Time Zone Database ( https://www.iana.org/time-zones ) is a comprehensive database used to manage timezones.


## What this repository provides

This repository includes a program that parses the IANA Time Zone Database and extracts the information regarding timezone offset transitions as a JSON file to facilitate the handling of the information.
This program is implemented using the `java.time` package in Kotlin.

Additionally, this repository includes an automated mechanism for detecting updates to the database and overriding the `tzot.json`.
This mechanism is implemented as a GitHub Actions workflow which updates the `tzot.json` monthly if a new version of the database is detected.

The resulting `tzot.json` is a simply structured JSON file, specifically designed for handling timezone offset transitions in various programming languages.


## Format of tzot.json

The `tzot.json` is serialized from a Kotlin object of type `List<Zone>`.

`Zone` is defined as follows:

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

`Transition` is defined as follows:

```kt
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
```


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
        ]
    },
    ...
]
```
