# tz-offset-transitions

## Overview

This repository provides information for timezones based on the IANA TZ database.
The information is available in a JSON file located at `gen/tzot.json`.


## What timezone is

A timezone is a geographic region of the Earth that observes the same standard time, encompassing some timezone offsets.
A timezone offset represents the difference between the local time in a specific timezone and the Coordinated Universal Time (UTC).
In a timezone, transitions between timezone offsets may occur due to factors such as daylight saving time adjustments or political changes in laws.
The timezone offset transitions that are going to occur in the future can be scheduled by several types of transition rules.

The IANA Time Zone Database ( https://www.iana.org/time-zones ) is a comprehensive database used to manage timezones.


## What this repository provides

This repository includes a program that parses the IANA Time Zone Database and extracts the information regarding timezones, each of which consists of timezone offset transitions and transition rules.
The extracted data is then formatted into a simply structured JSON file `gen/tzot.json`, specifically designed for handling the timezone offset transitions in various programming languages.
That program is implemented in Kotlin using the `java.time` package.

Additionally, this repository includes an automated mechanism for detecting updates to the database and overriding the `gen/tzot.json`.
This mechanism is implemented as a GitHub Actions workflow which updates the `gen/tzot.json` monthly if a new version of the database is detected.


## Format of gen/tzot.json

The format of the `gen/tzot.json` is a JSON value that can be assigned to a type `TzotJSON`.
The definition of the type is available in `tzot_json.ts`, which specifies the structure of the JSON.

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
