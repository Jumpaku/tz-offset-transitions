export type TzotJson = Zone[]

/**
 * Represents a timezone consisting of a collection of timezone offset transitions and a collection of transition rules.
 */
export interface Zone {
    /**
     * Name of the timezone.
     */
    id: string;

    /**
     * Collection of timezone offset transitions.
     */
    transitions: Transition[];

    /**
     * Collection of transition rules.
     */
    rules: Rule[];
}

/**
 * Represents a timezone offset transition between timezone offsets.
 */
export interface Transition {
    /**
     * Timestamp indicating when a transition between timezone offsets occurs, formatted as YYYY-MM-DDTHH:mm:ssZ (e.g., 2024-02-24T18:31:25Z).
     */
    transitionTimestamp: string;

    /**
     * Amount of timezone offset before the transition in seconds.
     */
    offsetSecondsBefore: number;

    /**
     * Amount of timezone offset after the transition in seconds.
     */
    offsetSecondsAfter: number;
}
/**
 * Represents a rule to generate yearly transitions based on the following:
 * - Given a year, the transition occurs at a given offset-time.
 * - The transition occurs on the first occurrence of a given day-of-week on or after a given month-day in the year.
 */
export interface Rule {
    /**
     * Amount of timezone offset before the transition in seconds.
     */
    offsetSecondsBefore: number;

    /**
     * Amount of timezone offset after the transition in seconds.
     */
    offsetSecondsAfter: number;

    /**
     * Month of the month-day, from 1 (January) to 12 (December).
     */
    month: number;

    /**
     * Base day of month, on or after which the day-of-week is sought. It is from 1 to 31.
     */
    baseDay: number;

    /**
     * Day-of-week, from 1 (Monday) to 7 (Sunday).
     */
    dayOfWeek: number;

    /**
     * Time of a day with offset, formatted as hh:mm:ss±hh:mm (e.g., 12:34:56+09:00).
     */
    offsetTime: string;
}
