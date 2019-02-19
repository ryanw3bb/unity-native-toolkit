using System.IO;
using UnityEditor;
using UnityEditor.Callbacks;
#if UNITY_IPHONE
using UnityEditor.iOS.Xcode;
#endif

public class PostProcessor 
{
    [PostProcessBuild]
    public static void OnPostProcessBuild(BuildTarget target, string path)
    {
#if UNITY_IPHONE
		
        if(target != BuildTarget.iOS) { return; }

        string projPath = Path.Combine(path, "Unity-iPhone.xcodeproj/project.pbxproj");

        // automatically @include frameworks and disable bitcode
        PBXProject proj = new PBXProject();
        string file = File.ReadAllText(projPath);
        proj.ReadFromString(file);

        string targetGuid = proj.TargetGuidByName("Unity-iPhone");

        proj.SetBuildProperty(targetGuid, "CLANG_ENABLE_MODULES", "YES");
        proj.SetBuildProperty(targetGuid, "ENABLE_BITCODE", "NO");

        File.WriteAllText(projPath, proj.WriteToString());

        // add necessary permissions to Plist
        string plistPath = Path.Combine(path, "Info.plist");
        PlistDocument plist = new PlistDocument();
        plist.ReadFromString(File.ReadAllText(plistPath));

        PlistElementDict rootDict = plist.root;
        rootDict.SetString("NSPhotoLibraryUsageDescription", "Requires access to the Photo Library");
        rootDict.SetString("NSPhotoLibraryAddUsageDescription", "Requires access to the Photo Library");
        rootDict.SetString("NSCameraUsageDescription", "Requires access to the Camera");
        rootDict.SetString("NSContactsUsageDescription", "Requires access to Contacts");
        rootDict.SetString("NSLocationAlwaysUsageDescription", "Requires access to Location");
        rootDict.SetString("NSLocationWhenInUseUsageDescription", "Requires access to Location");
        rootDict.SetString("NSLocationAlwaysAndWhenInUseUsageDescription", "Requires access to Location");

        File.WriteAllText(plistPath, plist.WriteToString());
#endif
    }	
}