package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {

    private var lstReminderDTO: MutableList<ReminderDTO> = mutableListOf()
    var shouldReturnError = false

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return if(shouldReturnError) {
            Result.Error("Data not found!")
        } else {
            Result.Success(lstReminderDTO)
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        lstReminderDTO.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        val reminderById = lstReminderDTO.find { it.id == id }
        return if (reminderById == null) {
            Result.Error("Data not found!")
        } else {
            Result.Success(reminderById)
        }
    }

    override suspend fun deleteAllReminders() {
        lstReminderDTO.clear()
    }


}