package com.challenge.aspire.utils.resources

/**
 * Status of a resource that is provided to the UI.
 *
 *
 * These are usually created by the Repository classes where they return
 * `Resource<T>` to pass back the latest data to the UI withScheduler its fetch status.
 */
enum class Status {
    SUCCESS,
    ERROR,
    FETCHING,
    REFRESH_DATA,
    LOAD_MORE,
    SEARCH_DATA
}
