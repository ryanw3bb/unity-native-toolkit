//
//  ImageTools.h
//  NativeToolkit
//
//  Created by Ryan on 30/01/2015.
//
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <AssetsLibrary/AssetsLibrary.h>

@interface ImageTools : UIViewController<UIImagePickerControllerDelegate, UINavigationControllerDelegate>

-(id)init;
-(void)imagePickerController:(UIImagePickerController*)picker
       didFinishPickingMediaWithInfo:(NSDictionary*)info;
-(void)imagePickerControllerDidCancel:(UIImagePickerController*)picker;
-(UIImage *)normalizeOrientation:(UIImage *)img;

@end
