package com.homework.testsapp

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.homework.catalogs.recycler.CatalogAdapter
import com.homework.catalogs.recycler.CatalogViewHolder
import com.homework.testsapp.module.FakeDataSource
import com.homework.testsapp.utils.RecyclerViewItemCountAssertion
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CatalogFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        ActivityScenario.launch(MainActivity::class.java).moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun items_count_test() {
        onView(withId(R.id.recycler_view_catalogs))
            .check(RecyclerViewItemCountAssertion<CatalogAdapter>(FakeDataSource.catalogsNumber))

        onView(withId(R.id.recycler_view_catalogs))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CatalogViewHolder>(0, click()))

        onView(withId(R.id.recycler_view_questionnaires))
            .check(RecyclerViewItemCountAssertion<CatalogAdapter>(FakeDataSource.questionnairesNumber))

        onView(withId(R.id.recycler_view_questionnaires))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CatalogViewHolder>(0, click()))

        onView(withId(R.id.button_start)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}