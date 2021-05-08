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
Add the files contained in Unity/Assets/ to your project then call the required method from your code. For instance:
```csharp
public void OnSaveScreenshotPress()
{
	NativeToolkit.SaveScreenshot("MyScreenshot", "MyScreenshotFolder", "jpeg");
}
```
For iOS builds make sure you have Scripting Backend set to IL2CPP and Architecture set to Universal in Unity Build Settings.

For Android builds Write Access needs to be set to External (SDCard) to allow saving of images, and Build System set to Gradle. A minimum API level of 16 and target API level of 27 was used during testing.

## Documentation
Detailed API documentation can be found [here](https://ryanwebb.com/nativetoolkit/).

## Notes
The example project was built using Unity 2019.4.0, Android Studio 3.1.3 and Xcode 11.3.1.

Android (Java) source located in Android/app/src/main/java/  
iOS (Obj-C) source located in Unity/Assets/Plugins/iOS/
