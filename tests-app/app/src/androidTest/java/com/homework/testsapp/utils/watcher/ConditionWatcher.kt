package com.homework.testsapp.utils.watcher

object ConditionWatcher {

    private const val DEFAULT_TIMEOUT_LIMIT = 10_000L
    private const val DEFAULT_INTERVAL = 250L
    private const val DURATION = 0L

    fun waitForCondition(
        instruction: Instruction, timeoutLimit: Long = DEFAULT_TIMEOUT_LIMIT,
        watchInterval: Long = DEFAULT_INTERVAL, duration: Long = DURATION, ignoreError: Boolean = false) {
        var status = ConditionStatus.IDLE
        var elapsedTime = 0L

        do {
            if (instruction.checkCondition()) {
                status = ConditionStatus.SUCCESS
            } else {
                elapsedTime += watchInterval
                Thread.sleep(watchInterval)
            }
            if (elapsedTime >= timeoutLimit) {
                if (!ignoreError) throw ConditionWatcherTimeoutException(instruction, timeoutLimit)
            }
        } while (status != ConditionStatus.SUCCESS)

        Thread.sleep(duration)
    }

    private enum class ConditionStatus {
        IDLE,
        SUCCESS
    }

    private class ConditionWatcherTimeoutException(
        instruction: Instruction,
        timeoutLimit: Long
    ) : RuntimeException(
        "${instruction.getDescription()} took more than $timeoutLimit milliseconds. Test failed."
    )
}