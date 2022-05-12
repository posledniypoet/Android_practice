package com.homework.testsapp.utils.watcher

import android.view.View
import androidx.test.espresso.AmbiguousViewMatcherException
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.homework.testsapp.utils.withTag
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher


class ElementVisibleCondition(private val matcher: Matcher<View>) : Instruction {

    override fun getDescription(): String {
        return "Is view with matcher visible?"
    }

    override fun checkCondition(): Boolean {
        return try {
            onView(matcher).check(matches(isCompletelyDisplayed()))
            true
        } catch (ignored: AssertionError) {
            false
        } catch (ignored: AmbiguousViewMatcherException) {
            false
        } catch (ignored: NoMatchingViewException) {
            false
        }
    }
}


