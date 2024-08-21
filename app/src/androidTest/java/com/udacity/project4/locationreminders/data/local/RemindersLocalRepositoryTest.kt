package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: RemindersDatabase
    private lateinit var repo: RemindersLocalRepository

    @Before
    fun setup() {
        db = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java,
            "locationReminders.db"
        ).build()

        runBlocking {
            db.reminderDao().deleteAllReminders()
        }

        repo = RemindersLocalRepository(db.reminderDao())
    }
    @Test
    fun save_reminder_success() = runBlocking {
        val data = ReminderDTO("1", "Title 1", "Description 1", 0.0, 0.0, "1")

        repo.saveReminder(data)

        val result = repo.getReminder(data.id)

        assertThat(result is Result.Success<*>, `is`(true))

        Assert.assertEquals(result, Result.Success(data))
    }

    @Test
    fun reminder_not_found() = runBlocking {
        val result = repo.getReminder("test")

        assertThat(result is Result.Error, `is`(true))
    }

}