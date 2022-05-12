package com.homework.testsapp.utils.viewPager2

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.viewpager2.widget.ViewPager2

class ViewPagerIdleWatcher(private val viewPager: ViewPager2) : ViewPager2.OnPageChangeCallback() {
    private var state = ViewPager2.SCROLL_STATE_IDLE
    private var waitingForIdle = false
    private val lock = Object()
    private val counter = CountingIdlingResource("Idle when $viewPager is not scrolling")

    init {
        IdlingRegistry.getInstance().register(counter)
        viewPager.registerOnPageChangeCallback(this)
    }

    override fun onPageScrollStateChanged(state: Int) {
        synchronized(lock) {
            this.state = state
            if (waitingForIdle && state == ViewPager2.SCROLL_STATE_IDLE) {
                counter.decrement()
                waitingForIdle = false
            }
        }
    }

    fun waitForIdle() {
        synchronized(lock) {
            if (!waitingForIdle && state != ViewPager2.SCROLL_STATE_IDLE) {
                waitingForIdle = true
                counter.increment()
            }
        }
    }

    fun unregister() {
        viewPager.unregisterOnPageChangeCallback(this)
        IdlingRegistry.getInstance().unregister(counter)
    }
}