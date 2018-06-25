//
//  Camera.h
//  NativeToolkit
//
//  Created by Ryan on 15/03/2015.
//
//

#import <Foundation/Foundation.h>

@interface Camera : UIViewController<UIImagePickerControllerDelegate, UINavigationControllerDelegate>

-(id)init;
-(void)imagePickerController:(UIImagePickerController*)picker
didFinishPickingMediaWithInfo:(NSDictionary*)info;
-(void)imagePickerControllerDidCancel:(UIImagePickerController*)picker;

@end
