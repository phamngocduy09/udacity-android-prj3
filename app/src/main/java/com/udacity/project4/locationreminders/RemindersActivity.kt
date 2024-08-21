package com.udacity.project4.locationreminders

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import com.udacity.project4.R
import com.udacity.project4.base.BaseActivity
import com.udacity.project4.databinding.ActivityRemindersBinding

/**
 * The RemindersActivity that holds the reminders fragments
 */
class RemindersActivity : BaseActivity() {

    private lateinit var binding: ActivityRemindersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (authFirebase.currentUser == null) {
            navigateAuthentication()
        }
        binding = ActivityRemindersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



    override fun onResume() {
        super.onResume()
        if (authFirebase.currentUser == null) {
            navigateAuthentication()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                    NavHostFragment().navController.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
