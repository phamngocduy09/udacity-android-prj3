package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.firebase.ui.auth.AuthUI
import com.udacity.project4.base.BaseActivity
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.utils.LoginFirebase

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : BaseActivity(), OnClickListener {

    private lateinit var _binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        _binding.btnLogin.setOnClickListener(this)
        if (authFirebase.currentUser != null) {
            navigateReminders()
        }
        // TODO: Implement the create account and sign in using FirebaseUI,
        //  use sign in using email and sign in using Google

        // TODO: If the user was authenticated, send him to RemindersActivity

        // TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout
    }

    override fun onResume() {
        super.onResume()
        if (authFirebase.currentUser != null) {
            navigateReminders()
        }
    }

    private fun navigateReminders() {
        startActivity(Intent(this, RemindersActivity::class.java))
    }

    override fun onClick(v: View?) {
        if (v?.id == _binding.btnLogin.id) {
            startActivityForResult(
                LoginFirebase,
                1
            )
        }
    }
}