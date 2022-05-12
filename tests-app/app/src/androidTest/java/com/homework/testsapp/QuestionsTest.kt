package com.homework.testsapp

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.viewpager2.widget.ViewPager2
import com.homework.catalogs.recycler.CatalogViewHolder
import com.homework.questionnaires.recycler.QuestionnaireViewHolder
import com.homework.testsapp.module.FakeDataSource
import com.homework.testsapp.utils.*
import com.homework.testsapp.utils.viewPager2.Direction
import com.homework.testsapp.utils.viewPager2.ViewPagerIdleWatcher
import com.homework.testsapp.utils.viewPager2.swipeNext
import com.homework.testsapp.utils.viewPager2.swipePrevious
import com.homework.testsapp.utils.watcher.ConditionWatcher
import com.homework.testsapp.utils.watcher.ElementVisibleCondition
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class QuestionsTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        ActivityScenario.launch(MainActivity::class.java).moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.recycler_view_catalogs))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CatalogViewHolder>(0, click()))

        onView(withId(R.id.recycler_view_questionnaires))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<QuestionnaireViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.button_start)).perform(click())
    }

    private val pagerMatcher = withId(R.id.viewpager)
    private val pagerId = R.id.viewpager
    private lateinit var idleWatcher: ViewPagerIdleWatcher
    private var questionId = 0
    @Test
    fun correct_test() {

        (0..1).forEach { _ ->
            setOption(FakeDataSource.quizAnswer)
            next()
        }

        (0..1).forEach { _ ->
            FakeDataSource.checkboxAnswer.forEach {
                setOption(it)
            }
            next()
        }

        val textView =
            getChild(allOf(withId(R.id.frame_layout), isDisplayed(), withTag(questionId)), 0)
        onView(textView).perform(typeText(FakeDataSource.textAnswer)).perform(closeSoftKeyboard())
        onView(allOf(withId(R.id.button_finish), isDisplayed())).perform(click())

        onView(withId(R.id.phrase)).check(matches(withText("Good")))
        onView(withId(R.id.score)).check(matches(withText("122")))
    }

    @Test
    fun random_test() {
        questionId = 0

        setOption(3)
        next()

        setOption(2)
        prev()

        setOption(0)
        next()

        next()

        setOption(2)
        setOption(0)

        next()

        setOption(3)
        setOption(2)
        setOption(0)

        onView(allOf(withId(R.id.button_finish), isDisplayed())).perform(click())

        onView(withId(R.id.phrase)).check(matches(withText("Bad")))
        onView(withId(R.id.score)).check(matches(withText("11")))
    }

    private fun setOption(position: Int) {
        val containerMatcher =
            getChild(allOf(withId(R.id.frame_layout), isDisplayed(), withTag(questionId)), 0)
        val option = getChild(containerMatcher, position)
        ConditionWatcher.waitForCondition(ElementVisibleCondition(option))
        onView(option).perform(click())
    }

    private fun viewPagerSwipe(direction: Direction) {
        registerViewPagerIdling()
        onView(pagerMatcher).perform(
            if (direction == Direction.FORWARD) swipeNext(pagerId)
            else swipePrevious(pagerId)
        )
        idleWatcher.waitForIdle()
        unregisterViewPagerIdling()
    }



    private fun next() {
        viewPagerSwipe(Direction.FORWARD)
        questionId++
    }

    private fun prev() {
        viewPagerSwipe(Direction.BACKWARD)
        questionId--
    }

    private fun registerViewPagerIdling() {
        val viewPager = getCurrentActivity().findViewById(pagerId) as ViewPager2
        idleWatcher = ViewPagerIdleWatcher(viewPager)
    }

    private fun unregisterViewPagerIdling() {
        idleWatcher.unregister()
    }
}