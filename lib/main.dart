import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const batteryChannel =
      MethodChannel("com.systeminfo.battery/batteryLevel");
  String batteryLevel = 'waiting...';

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text("JavaFLUTTER"),
        ),
        body: Column(
          children: [
            const Text("Welcome to Jumanji"),
            const SizedBox(height: 10.0),
            Text(
              batteryLevel,
              textAlign: TextAlign.center,
            ),
            Center(
              child: ElevatedButton(
                onPressed: () {
                  getBatteryLevel();
                },
                child: const Text('Click Me'),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Future<void> getBatteryLevel() async {
    final arguments = {'name': 'Sarah Abs'};
    try {
      final int newBatteryLevel =
          await batteryChannel.invokeMethod('getBatteryLevel', arguments);
      setState(() {
        batteryLevel = '$newBatteryLevel';
      });
    } on PlatformException catch (e) {
      print("Failed to get battery level: '${e.message}'.");
    }
  }
}
