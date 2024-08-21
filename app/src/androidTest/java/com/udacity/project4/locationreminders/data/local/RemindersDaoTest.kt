package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: RemindersDatabase

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
    }

    @Test
    fun save_reminder_to_db_success() = runBlocking {
        val data = ReminderDTO("1", "Title 1", "Description 1", 0.0, 0.0, "1")

        db.reminderDao().saveReminder(data)

        val lstData = db.reminderDao().getReminders()

        assertThat(lstData.size, `is`(1))
        Assert.assertEquals(lstData[0], data)
    }


}