using System;
using System.IO;
using System.Collections.Generic;
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

        // tells Xcode to automatically @include frameworks
        string pbxproj = path + "/Unity-iPhone.xcodeproj/project.pbxproj";
        string insertKeyword = "buildSettings = {";
        string foundKeyword = "CLANG_ENABLE_MODULES";
        string modulesFlag = "				CLANG_ENABLE_MODULES = YES;";

        List<string> lines = new List<string>();
		
        foreach(string str in File.ReadAllLines(pbxproj)) 
        {
            if(!str.Contains(foundKeyword)) 
            { 
                lines.Add(str);
            }
            if(str.Contains(insertKeyword))
            {
                lines.Add(modulesFlag);
            }
        }

        using(File.Create(pbxproj)) {}

        foreach(string str in lines) 
        {
            File.AppendAllText(pbxproj, str + Environment.NewLine);
        }

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