package com.homework.testsapp.utils

import android.app.Activity
import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import androidx.viewpager2.widget.ViewPager2
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


class RecyclerViewItemCountAssertion<T : RecyclerView.Adapter<*>>(private val expectedCount: Int) :
    ViewAssertion {
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView: RecyclerView = view as RecyclerView
        val adapter: T = recyclerView.adapter as T
        assertThat(adapter.itemCount, `is`(expectedCount))
    }
}

fun withTag(position: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun matchesSafely(view: View): Boolean {
            return view.tag.toString() == position.toString()
        }

        override fun describeTo(description: Description) {
            description.appendText("")
        }
    }
}

fun getChild(parent: Matcher<View>, position: Int): Matcher<View> {
    return allOf(withParent(parent), isDisplayed(), withParentIndex(position))
}

fun getCurrentActivity(): Activity {
    val currentActivity = arrayOf<Activity?>(null)
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
        val resumedActivity: Collection<Activity> =
            ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
        val it = resumedActivity.iterator()
        currentActivity[0] = it.next()
    }
    return currentActivity[0] ?: throw Resources.NotFoundException("No activities found")
}
