package com.dicoding.todoapp.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.dicoding.todoapp.R
import com.dicoding.todoapp.notification.NotificationWorker
import com.dicoding.todoapp.utils.NOTIFICATION_CHANNEL_ID
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { preference, newValue ->
                val channelName = getString(R.string.notify_channel_name)

                //TODO 13 : Schedule and cancel daily reminder using WorkManager with data channelName
                val value = newValue as Boolean
                val workManager = WorkManager.getInstance(requireContext())
                lateinit var periodicWorkrequest: PeriodicWorkRequest
                if (value) {
                    if (value) {
                        val data = Data.Builder().putString(NOTIFICATION_CHANNEL_ID, channelName).build()

                        // Schedule the periodic work request
                        val repeatInterval = 1L // Repeat every 1 day
                        val repeatIntervalTimeUnit = TimeUnit.DAYS
                        val constraints = Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()

                        val periodicWorkRequest = PeriodicWorkRequest.Builder(
                            NotificationWorker::class.java,
                            repeatInterval,
                            repeatIntervalTimeUnit
                        )
                            .setInputData(data)
                            .setConstraints(constraints)
                            .build()

                        workManager.enqueueUniquePeriodicWork(
                            "notificationWork",
                            ExistingPeriodicWorkPolicy.REPLACE,
                            periodicWorkRequest
                        )
                    } else {
                        // Cancel the periodic work request
                        workManager.cancelUniqueWork("notificationWork")
                    }
                }
                true
            }

        }



    }
}