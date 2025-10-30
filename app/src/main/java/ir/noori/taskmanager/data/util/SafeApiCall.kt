package ir.noori.taskmanager.data.util

import ir.noori.taskmanager.data.network.NetworkChecker
import ir.noori.taskmanager.domain.exceptions.NoInternetException

suspend inline fun <T> safeApiCall(
    networkChecker: NetworkChecker,
    crossinline apiCall: suspend () -> T
): T {
    if (!networkChecker.isConnected()) throw NoInternetException()
    return try {
        apiCall()
    } catch (e: Exception) {
        throw e
    }
}
