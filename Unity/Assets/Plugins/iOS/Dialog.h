//
//  Dialog.h
//  NativeToolkit
//
//  Created by Ryan on 01/02/2015.
//
//

#import <Foundation/Foundation.h>

@interface Dialog : UIViewController<UIAlertViewDelegate>

-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex;

@end