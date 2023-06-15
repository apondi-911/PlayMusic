package com.example.playmusic

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.switchmaterial.SwitchMaterial


class MainActivity : AppCompatActivity() {

    private var player: MediaPlayer? = null
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button


    private lateinit var wifiSwitch: SwitchMaterial
    private lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        wifiSwitch = findViewById(R.id.wifi_switch)



        // Buttons to Start and Stop Ringtone
        btnStart.setOnClickListener {
            val intent = Intent(this, MyService::class.java)
            intent.putExtra("wifi_enabled", wifiSwitch.isChecked)
            startService(intent)
            Toast.makeText(this, "Ringtone is playing", Toast.LENGTH_LONG).show()
        }

        btnStop.setOnClickListener {
            player?.stop()
            player?.release()
            player = null
            stopService(Intent(this, MyService::class.java))
            Toast.makeText(this, "Ringtone is stopped", Toast.LENGTH_LONG).show()
        }


        //Switch to control Wifi ON and OFF
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                wifiManager.isWifiEnabled = true
                wifiSwitch.text = "WiFi is ON"
                player?.start()
            } else {
                wifiManager.isWifiEnabled = false
                wifiSwitch.text = "WiFi is OFF"
                player?.stop()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(wifiStateReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(wifiStateReceiver)
    }
    private val wifiStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val wifiState = intent.getIntExtra(
                WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN
            )

            when (wifiState) {
                WifiManager.WIFI_STATE_ENABLED -> {
                    wifiSwitch.isChecked = true
                    wifiSwitch.text = "WiFi is ON"
                    startMusic()
                    Toast.makeText(this@MainActivity, "WiFi is On", Toast.LENGTH_SHORT).show()
                }

                WifiManager.WIFI_STATE_DISABLED -> {
                    wifiSwitch.isChecked = false
                    wifiSwitch.text = "WiFi is OFF"
                    stopMusic()
                    Toast.makeText(this@MainActivity, "WiFi is Off", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun stopMusic() {
        if (wifiSwitch.isChecked) {
            player?.start()
        }
    }

    private fun startMusic() {
        player?.pause()
    }

}