# tz-offset-transitions

## Overview

This repository provides information with respect to timezones based on the IANA TZ database.
The information is available in a JSON file located at `gen/tzot.json`.


## What timezone is

A timezone is a geographic region of the Earth that observes the same standard time, encompassing some timezone offsets.
A timezone offset represents the difference between the local time in a specific timezone and the Coordinated Universal Time (UTC).
In a timezone, timezone offset may transition to the another timezone offset by factors like daylight saving time adjustments or political changes in laws.
The timezone offset transitions which are going to occur in the future can be scheduled by transition rules.

The IANA Time Zone Database ( https://www.iana.org/time-zones ) is a comprehensive database used to manage timezones.


## What this repository provides

This repository includes a program that parses the IANA Time Zone Database and extracts the information regarding timezones, each of which consists of timezone offset transitions and transition rules, as a JSON file to facilitate the handling of the information.
That program is implemented using the `java.time` package in Kotlin.

Additionally, this repository includes an automated mechanism for detecting updates to the database and overriding the `gen/tzot.json`.
This mechanism is implemented as a GitHub Actions workflow which updates the `gen/tzot.json` monthly if a new version of the database is detected.

The resulting `gen/tzot.json` is a simply structured JSON file, specifically designed for handling timezone offset transitions in various programming languages.


## Format of gen/tzot.json

The format of the `gen/tzot.json` is a JSON value that can be assigned to a type `TzotJSON`.
The definition of the type is available in `tzot.ts`.

The below shows an example of the JSON value.

```json
[
  {
    "id": "Europe/Zurich",
    "transitions": [
      {
        "transition_timestamp": "1853-07-15T23:25:52Z",
        "offset_seconds_before": 2048,
        "offset_seconds_after": 1786
      },
      {
        "transition_timestamp": "1894-05-31T23:30:14Z",
        "offset_seconds_before": 1786,
        "offset_seconds_after": 3600
      },
      ...
    ],
    "rules": [
      {
        "type": "net.jumpaku.tzot.Rule.WeekDayPositive",
        "offset_seconds_before": 3600,
        "offset_seconds_after": 7200,
        "month": 3,
        "offset_time": "01:00:00Z",
        "day_of_week": 7,
        "days": 25
      },
      {
        "type": "net.jumpaku.tzot.Rule.WeekDayPositive",
        "offset_seconds_before": 7200,
        "offset_seconds_after": 3600,
        "month": 10,
        "offset_time": "01:00:00Z",
        "day_of_week": 7,
        "days": 25
      },
      ...
    ]
  },
  ...
]
```
