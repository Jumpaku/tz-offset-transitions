type TzotJSON = Zone[]

/** Timezone consists of a collection of timezone offset transitions and a collection of transition rules. */
type Zone = {

    /** Name of the timezone. */
    id: string;

    /** Collection of timezone offset transitions. */
    transitions: Transition[];

    /** Collection of transition rules. */
    rules: Rule[];
};

/** Timezone offset transition between timezone offsets. */
type Transition = {

    /** Timestamp indicating when a transition between timezone offsets occurs, which is a text such as `2024-02-24T18:31:25Z`. */
    transition_timestamp: string;

    /** Amount of timezone offset before the transition in second. */
    offset_seconds_before: number;

    /** Amount of timezone offset after the transition in second. */
    offset_seconds_after: number;
};

/** Rule to generate yearly transitions in the future. */
type Rule =
    | WeekDayPositive
    | WeekDayNegative
    | MonthDayPositive
    | MonthDayNegative

type RuleBase = {

    /** Amount of timezone offset before the transition in second. */
    offset_seconds_before: number;

    /** Amount of timezone offset after the transition in second. */
    offset_seconds_after: number;

    /** Month of the month-day, from 1 (January) to 12 (December). */
    month: number;

    /** Time of a day with offset such as "12:34:56+09:00". The format is hh:mm:ss±hh:mm. */
    offset_time: string;
};

/**
 * Rule to generate yearly transitions as follows:
 * Given a year, the transition occurs at a given offset-time on the first occurrence of a given day-of-week on or after a given month-day in the year.
 */
type WeekDayPositive = RuleBase & {

    type: "net.jumpaku.tzot.Rule.WeekDayPositive";

    /** Day-of-week, from 1 (Monday) to 7 (Sunday). */
    day_of_week: number;

    /** Days counting from the first of the month, which from 1 to 31. */
    days: number;
};

/**
 * Rule to generate yearly transitions as follows:
 * Given a year, the transition occurs at a given offset-time on the first occurrence of a given day-of-week on or before a given month-day in the year.
 */
type WeekDayNegative = RuleBase & {

    type: "net.jumpaku.tzot.Rule.WeekDayNegative";

    /** Day-of-week, from 1 (Monday) to 7 (Sunday). */
    day_of_week: number;

    /** Days counting from the first of the month, which from 1 to 31. */
    days_from_last: number;
};

/**
 * Rule to generate yearly transitions as follows:
 * Given a year, the transition occurs at a given offset-time on a given month-day in the year.
 */
type MonthDayPositive = RuleBase & {

    type: "net.jumpaku.tzot.Rule.MonthDayPositive";

    /** Days counting from the first of the month, which from 1 to 31. */
    days: number;
};

/**
 * Rule to generate yearly transitions as follows:
 * Given a year, the transition occurs at a given offset-time on a given month-day in the year.
 */
type MonthDayNegative = RuleBase & {

    type: "net.jumpaku.tzot.Rule.MonthDayNegative";

    /** Days counting from the first of the month, which from 1 to 31. */
    days_from_last: number;
}