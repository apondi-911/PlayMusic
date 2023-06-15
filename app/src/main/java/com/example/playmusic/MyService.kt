package com.example.playmusic

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings

class MyService : Service(){

    private var player : MediaPlayer? = null
    private var isWifiEnabled: Boolean = false


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        isWifiEnabled = intent.getBooleanExtra("wifi_enabled", false)
        if (isWifiEnabled) {
            player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
            player?.isLooping = true
            player?.start()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
