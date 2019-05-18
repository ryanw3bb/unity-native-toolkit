//
//  Rate.m
//  NativeToolkit
//
//  Created by Ryan on 02/02/2015.
//
//

#include "Rate.h"
#include "StringTools.h"
#include "Unity/UnityInterface.h"

@implementation Rate

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    NSString *appId = [[NSUserDefaults standardUserDefaults] valueForKey:@"appId"];
    
    NSLog(@"**didDismissWithAppId: %@", appId);
    
    if (buttonIndex == 0) {
        // yes
        NSString * theUrl = [NSString  stringWithFormat:@"itms-apps://itunes.apple.com/WebObjects/MZStore.woa/wa/viewContentsUserReviews?id=%@&onlyLatestVersion=true&pageNumber=0&sortOrdering=1&type=Purple+Software",appId];
        if ([[UIDevice currentDevice].systemVersion integerValue] > 6)
            theUrl = [NSString stringWithFormat:@"itms-apps://itunes.apple.com/app/id%@",appId];
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:theUrl]];
        
        const char *response = [@"0" UTF8String];
        UnitySendMessage("NativeToolkit", "OnRatePress", response);
    }
    else if (buttonIndex == 1) {
        // later
        const char *response = [@"1" UTF8String];
        UnitySendMessage("NativeToolkit", "OnRatePress", response);
    }
    else if (buttonIndex == 2) {
        // no
        const char *response = [@"2" UTF8String];
        UnitySendMessage("NativeToolkit", "OnRatePress", response);
    }
}

@end

static Rate* rateDelegate = NULL;

extern "C"
{
    void rateApp(char* title, char* message, char* positiveBtnText, char* neutralBtnText, char* negativeBtnText, char* appStoreId)
    {
        if(rateDelegate == NULL) rateDelegate = [[Rate alloc] init];
        
        NSString *nsTitle = [StringTools createNSString:title];
        NSString *nsMessage = [StringTools createNSString:message];
        NSString *btn1 = [StringTools createNSString:positiveBtnText];
        NSString *btn2 = [StringTools createNSString:neutralBtnText];
        NSString *btn3 = [StringTools createNSString:negativeBtnText];
        
        // store apple Id in NSUserDefaults for use later
        NSString *appId = [StringTools createNSString:appStoreId];
        [[NSUserDefaults standardUserDefaults] setValue:appId forKey:@"appId"];
        [[NSUserDefaults standardUserDefaults] synchronize];
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nsTitle message:nsMessage delegate:rateDelegate cancelButtonTitle:btn1 otherButtonTitles:nil];
        [alert addButtonWithTitle:btn2];
        [alert addButtonWithTitle:btn3];
        [alert show];
    }
}
