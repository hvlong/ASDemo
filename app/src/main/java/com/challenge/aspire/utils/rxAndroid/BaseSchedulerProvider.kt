package com.challenge.aspire.utils.rxAndroid

import io.reactivex.Scheduler
import io.reactivex.annotations.NonNull

interface BaseScheduleProvider {
    @NonNull
    fun computation(): Scheduler

    @NonNull
    fun io(): Scheduler

    @NonNull
    fun ui(): Scheduler
}
