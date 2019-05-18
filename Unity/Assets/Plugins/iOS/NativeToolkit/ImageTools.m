//
//  ImageTools.mm
//  NativeToolkit
//
//  Created by Ryan on 20/03/2014.
//
//

#import "ImageTools.h"
#import "StringTools.h"
#include "Unity/UnityInterface.h"

extern UIViewController *UnityGetGLViewController();

@implementation ImageTools

-(id)init {
    return [super init];
}

-(void)imagePickerController:(UIImagePickerController*)picker
didFinishPickingMediaWithInfo:(NSDictionary*)info{
    
    NSLog(@"**didFinishPickingImage**");
    
    UIImage *img = [self normalizeOrientation:[info objectForKey:UIImagePickerControllerOriginalImage]];
    NSString *path = [NSTemporaryDirectory() stringByAppendingPathComponent:@"image.jpg"];
    
    NSData *imageData = UIImageJPEGRepresentation(img, 0.7);
    [imageData writeToFile:path atomically:YES];
    
    const char *charPath = [path UTF8String];
    
    UnitySendMessage("NativeToolkit", "OnPickImage", charPath);
    
    [picker dismissModalViewControllerAnimated:YES];
}

-(void)imagePickerControllerDidCancel:(UIImagePickerController*)picker
{
    NSString *path = @"Cancelled";
    const char *charPath = [path UTF8String];
    UnitySendMessage("NativeToolkit", "OnPickImage", charPath);
    
    [picker dismissModalViewControllerAnimated:YES];
}

-(UIImage *)normalizeOrientation:(UIImage *)img {
    if (img.imageOrientation == UIImageOrientationUp)
        return img;
    
    UIGraphicsBeginImageContextWithOptions(img.size, NO, img.scale);
    [img drawInRect:(CGRect){0, 0, img.size}];
    UIImage *normalizedImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return normalizedImage;
}

@end

static ImageTools *imageToolsDelegate = NULL;
UIPopoverController *popover;

int saveToGallery(char* path)
{
    NSString *imagePath = [NSString stringWithUTF8String:path];
    
    //NSLog(@"###### This is the file path being passed: %@", imagePath);
    
    ALAssetsLibrary *lib = [[ALAssetsLibrary alloc] init];
    [lib enumerateGroupsWithTypes:ALAssetsGroupSavedPhotos usingBlock:^(ALAssetsGroup *group, BOOL *stop) {
    } failureBlock:^(NSError *error) { }];
    
    ALAuthorizationStatus status = [ALAssetsLibrary authorizationStatus];
    
    if (status == ALAuthorizationStatusRestricted || status == ALAuthorizationStatusDenied)
    {
        NSLog(@"###### Access to Camera Roll denied");
        return 2;
    }
    
    if(![[NSFileManager defaultManager] fileExistsAtPath:imagePath])
    {
        return 0;
    }
    
    UIImage *image = [UIImage imageWithContentsOfFile:imagePath];
    
    if(image)
    {
        NSLog(@"###### Image saved");
        UIImageWriteToSavedPhotosAlbum( image, nil, NULL, NULL );
        return 1;
    }
    
    return 0;
}

void pickImage()
{
    NSLog(@"**pickImage**");
    
    if(imageToolsDelegate == NULL) imageToolsDelegate = [[ImageTools alloc] init];
    
    UIImagePickerController *imagePicker = [[UIImagePickerController alloc] init];
    
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeSavedPhotosAlbum])
        imagePicker.sourceType = UIImagePickerControllerSourceTypeSavedPhotosAlbum;
    else
        imagePicker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    
    imagePicker.delegate = imageToolsDelegate;
    
    [UnityGetGLViewController() presentModalViewController:imagePicker animated:YES];
    
}
