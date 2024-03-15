package com.example.javaflutter

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val Battery_CHANNEL = "com.systeminfo.battery/batteryLevel"
    private lateinit var channel: MethodChannel

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, Battery_CHANNEL)

        //receive data from Flutter
        channel.setMethodCallHandler { call, result ->
            if (call.method == "getBatteryLevel") {
                val arguments = call.arguments as? Map<String, Any>
                val name = arguments?.get("name")

                val batteryLevel = getBatteryLevel()

                result.success(batteryLevel)
            }
        }
    }

    private fun getBatteryLevel(): Int {
        val batteryLevel: Int
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val intent = ContextWrapper(applicationContext).registerReceiver(null, intentFilter)
            batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        }
        return batteryLevel
    }
}
