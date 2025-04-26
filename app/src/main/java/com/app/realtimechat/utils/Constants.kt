package com.app.realtimechat.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager

object Constants {
    const val BASE_URL ="wss://free.blr2.piesocket.com/v3/1?api_key=968pF2aLHvMIzWCfEOd0ANoPCkkl7Vy25iCJzWvj&notify_self=1&source=androidsdk&v=1&presence=0&uuid=905159f0-3f1d-4e90-bd26-3f7f719209d2"
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
    // Show an alert dialog for network failures
    fun showAlert(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage("Websocket not connected")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}