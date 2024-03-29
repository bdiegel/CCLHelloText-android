# CCLHelloText-Android
A version of the Google [CastHelloText-Android](https://github.com/googlecast/CastHelloText-android).
example project that uses the [Cast Companion Library](https://github.com/googlecast/CastCompanionLibrary-android).
Use of the CCL library greatly reduces the amount boilerplate code. 

This demo application shows how to send messages from an Android device to a receiver using a custom namespace.

## Project Setup
This is an Android Studio project. The gradle build has an **external** dependency on the Android 
library project [CastCompanionLibrary](https://github.com/googlecast/CastCompanionLibrary-android).
See settings.gradle and build.gradle for details of how this dependency is configured.

The structure of the working directory:

  1. create a directory that will hold both projects and cd into it
  2. git clone [https://github.com/bdiegel/CCLHelloText-android.git]()
  3. git clone [https://github.com/googlecast/CastCompanionLibrary-android]() CastCompanionLibrary

## CCL Dependencies
My fork of CCL uses an updated GMS Play Services SDK and Support Library, providing updated Cast icons.

* The Android SDK android-support-v7-mediarouter support libraries (at least revision 19).
* The Android SDK google-play-services_lib library (at least version 4.4)

## Custom Receiver Instructions
* If you don't want to use the sample App ID, you need to do the following steps
* Get a Chromecast device and get it set up for development: https://developers.google.com/cast/docs/developers#Get_started
* Register an application on the Developers Console (http://cast.google.com/publish). Select the Custom Receiver option and specify the URL to where you are hosting the receiver/receiver.html file (You can use Google Drive to host your files: https://support.google.com/drive/answer/2881970?hl=en). You will get an App ID when you finish registering your application.
* Setup the project dependencies
* Insert your App ID in the strings.xml in the res directory of the project (look for app_id in that file)
* Compile and deploy to your Android device.

## License
See LICENSE

