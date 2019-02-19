# Native Toolkit
Easily integrate native iOS & Android functionality into Unity projects.
* Pick images from camera roll
* Use device camera to take an image
* Save image to gallery
* Save screenshot to gallery
* Send email with attachments
* Retrieve a contact from contacts list
* Alert dialogs
* App rating
* Local notifications
* GPS data (lat/long as double)

## Usage
Add the files contained in Unity/Assets/ to your project then call the required static function from your code. For instance:
```csharp
public void OnSaveScreenshotPress()
{
	NativeToolkit.SaveScreenshot("MyScreenshot", "MyScreenshotFolder", "jpeg");
}
```
For iOS builds make sure you have Scripting Backend set to IL2CPP and Architecture set to Universal in Unity Build Settings. Once you have built an Xcode project, ensure Enable Bitcode is set to NO in Xcode build settings (if you require Bitcode include the raw .h/.mm files in your project instead of the compiled library).

For Android builds Write Access needs to be set to External (SDCard) to allow saving of images, and Build System set to Gradle. A minimum API level of 16 and target API level of 27 was used during testing.

## Documentation
Detailed API documentation can be found here:
http://secondfury.com/nativetoolkit/

## Notes
The example project was built using Unity 2018.2.20f1 and the libraries using Android Studio 3.1.3 and Xcode 10.1.

Android (Java) source located in Android/app/src/main/java/  
iOS (Obj-C) source located in iOS/NativeToolkit/

If rebuilding the iOS library be sure to add the location of UnityAppController.h to Xcode header paths, it's normally located in a subfolder of the Unity install directory.
