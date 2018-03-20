package com.remybarbosa.rxkotlinexample.part6

import java.util.concurrent.TimeUnit

data class Duration (val duration: Long, val timeUnit: TimeUnit)


operator fun Duration.div(duration: Duration): Long {
    return this.duration / duration.duration
}
