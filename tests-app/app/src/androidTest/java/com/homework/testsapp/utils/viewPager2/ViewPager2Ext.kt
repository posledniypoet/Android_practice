package com.homework.testsapp.utils.viewPager2


import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.viewpager2.widget.ViewPager2
import org.hamcrest.Matcher

fun swipeNext(viewPagerId: Int): ViewAction {
    return SwipeAction(viewPagerId, Direction.FORWARD)
}

fun swipePrevious(viewPagerId: Int): ViewAction {
    return SwipeAction(viewPagerId, Direction.BACKWARD)
}

enum class Direction {
    FORWARD,
    BACKWARD
}

private class SwipeAction(val viewPagerId: Int, val direction: Direction) : ViewAction {

    override fun getDescription(): String = "ViewPager with id $viewPagerId swiping $direction"

    override fun getConstraints(): Matcher<View> = withId(viewPagerId)

    override fun perform(uiController: UiController, view: View) {
        val vp = view as ViewPager2
        val isForward = direction == Direction.FORWARD
        val swipeAction: ViewAction = if (vp.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            if (isForward) swipeLeft() else swipeRight()
        } else {
            if (isForward) swipeUp() else swipeDown()
        }
        swipeAction.perform(uiController, view)
    }
}