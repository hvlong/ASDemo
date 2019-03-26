package com.challenge.aspire.utils.extension

import com.challenge.aspire.utils.rxAndroid.BaseScheduleProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Use SchedulerProvider configuration for Completable
 */
fun Completable.withScheduler(scheduler: BaseScheduleProvider): Completable =
        this.subscribeOn(scheduler.io()).observeOn(scheduler.ui())

/**
 * Use SchedulerProvider configuration for Single
 */
fun <T> Single<T>.withScheduler(scheduler: BaseScheduleProvider): Single<T> =
        this.subscribeOn(scheduler.io()).observeOn(scheduler.ui())

/**
 * Use SchedulerProvider configuration for Observable
 */
fun <T> Observable<T>.withScheduler(scheduler: BaseScheduleProvider): Observable<T> =
        this.subscribeOn(scheduler.io()).observeOn(scheduler.ui())

/**
 * Use SchedulerProvider configuration for Flowable
 */
fun <T> Flowable<T>.withScheduler(scheduler: BaseScheduleProvider): Flowable<T> =
        this.subscribeOn(scheduler.io()).observeOn(scheduler.ui())