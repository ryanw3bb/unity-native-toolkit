//
//  Camera.mm
//  NativeToolkit
//
//  Created by Ryan on 15/03/2015.
//
//

#include "Camera.h"
#include "Unity/UnityInterface.h"

extern UIViewController *UnityGetGLViewController();

@implementation Camera

-(id)init {
    return [super init];
}

-(void)imagePickerController:(UIImagePickerController*)picker
didFinishPickingMediaWithInfo:(NSDictionary*)info{
    
    NSLog(@"**didFinishCameraShot**");
    
    UIImage *img = [info objectForKey:UIImagePickerControllerEditedImage];
    NSString *path = [NSTemporaryDirectory() stringByAppendingPathComponent:@"image.jpg"];
    
    NSData *imageData = UIImageJPEGRepresentation(img, 0.7);
    [imageData writeToFile:path atomically:YES];
    
    const char *charPath = [path UTF8String];
    
    UnitySendMessage("NativeToolkit", "OnCameraFinished", charPath);
    
    [picker dismissModalViewControllerAnimated:YES];
}

-(void)imagePickerControllerDidCancel:(UIImagePickerController*)picker
{
    NSString *path = @"Cancelled";
    const char *charPath = [path UTF8String];
    UnitySendMessage("NativeToolkit", "OnCameraFinished", charPath);
    
    [picker dismissModalViewControllerAnimated:YES];
}

@end

static Camera* cameraDelegate = NULL;

extern "C"
{
    void openCamera()
    {
        NSLog(@"**openCamera**");
        
        if(cameraDelegate == NULL) cameraDelegate = [[Camera alloc] init];
        
        UIImagePickerController *picker = [[UIImagePickerController alloc] init];
        picker.delegate = cameraDelegate;
        picker.allowsEditing = YES;
        picker.sourceType = UIImagePickerControllerSourceTypeCamera;
        
        [UnityGetGLViewController() presentModalViewController:picker animated:YES];
    }
}
