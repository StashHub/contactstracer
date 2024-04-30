package com.drgayno.contactstracer.ext

import kotlin.math.pow

fun Int.rssiToDistance(): Double {
    /*
     * RSSI = TxPower - 10 * n * lg(d)
     * n = 2 (in free space)
     *
     * d = 10 ^ ((TxPower - RSSI) / (10 * n))
     */
    return (10.0.pow((-65 - this) / (10.0 * 2))) * 3.281
}

fun Int.rssiToDistanceString(): String {
    return String.format("%.1f ft", rssiToDistance())
}

fun Int.daysToMilis(): Long {
    return this * 86400000L
}

fun Int.hoursToMilis(): Long {
    return this * 3600000L
}

fun Int.minutesToMilis(): Long {
    return this * 60000L
}

fun Int.secondsToMilis(): Long {
    return this * 1000L
}