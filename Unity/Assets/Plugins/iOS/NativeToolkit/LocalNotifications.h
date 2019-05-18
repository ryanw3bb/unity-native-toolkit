//
//  LocalNotification.h
//  NativeToolkit
//
//  Created by Ryan on 29/01/2015.
//
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "UnityAppController.h"

@interface LocalNotifications : UnityAppController

-(bool)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions;
-(void)application:(UIApplication*)application didReceiveLocalNotification:(UILocalNotification *)notification;

@end
