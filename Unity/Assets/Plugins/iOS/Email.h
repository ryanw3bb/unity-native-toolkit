//
//  Email.h
//  NativeToolkit
//
//  Created by Ryan on 01/02/2015.
//
//

#import <Foundation/Foundation.h>
#import <MessageUI/MessageUI.h>
#import <MessageUI/MFMailComposeViewController.h>

@interface Email : UIViewController<MFMailComposeViewControllerDelegate>

-(void)mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error;

@end
