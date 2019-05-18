//
//  Dialog.m
//  NativeToolkit
//
//  Created by Ryan on 01/02/2015.
//
//

#import "Dialog.h"
#import "StringTools.h"
#include "Unity/UnityInterface.h"

@implementation Dialog

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    NSLog(@"**didDismissWithButtonIndex**");
    
    if (buttonIndex == 0) {
        const char *response = [@"Yes" UTF8String];
        UnitySendMessage("NativeToolkit", "OnDialogPress", response);
    }
    else if (buttonIndex == 1) {
        const char *response = [@"No" UTF8String];
        UnitySendMessage("NativeToolkit", "OnDialogPress", response);
    }
}

@end

static Dialog* dialogDelegate = NULL;

extern "C"
{
    void showConfirm(char* title, char* message, char* positiveBtnText, char* negativeBtnText)
    {
        if(dialogDelegate == NULL) dialogDelegate = [[Dialog alloc] init];
        
        NSString *nsTitle = [StringTools createNSString:title];
        NSString *nsMessage = [StringTools createNSString:message];
        NSString *btn1 = [StringTools createNSString:positiveBtnText];
        NSString *btn2 = [StringTools createNSString:negativeBtnText];
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nsTitle message:nsMessage delegate:dialogDelegate cancelButtonTitle:btn1 otherButtonTitles:nil];
        [alert addButtonWithTitle:btn2];
        [alert show];
    }
    
    void showAlert(char* title, char* message, char* confirmBtnText)
    {
        if(dialogDelegate == NULL) dialogDelegate = [[Dialog alloc] init];
        
        NSString *nsTitle = [StringTools createNSString:title];
        NSString *nsMessage = [StringTools createNSString:message];
        NSString *btn1 = [StringTools createNSString:confirmBtnText];
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nsTitle message:nsMessage delegate:dialogDelegate cancelButtonTitle:btn1 otherButtonTitles:nil];
        [alert show];
    }
}
