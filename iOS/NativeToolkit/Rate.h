//
//  Rate.h
//  NativeToolkit
//
//  Created by Ryan on 02/02/2015.
//
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@interface Rate : UIViewController<UIAlertViewDelegate>

-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex;

@end
