//
//  Email.m
//  NativeToolkit
//
//  Created by Ryan on 01/02/2015.
//
//

#import "Email.h"
#import "StringTools.h"
extern UIViewController *UnityGetGLViewController();

@implementation Email

- (void)mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error
{
    NSLog(@"**dismissEmailWindow**");
    [controller dismissViewControllerAnimated:YES completion:nil];
}

@end

static Email* emailDelegate = NULL;

void sendEmail(char* to, char* cc, char* bcc, char* subject, char* body, char* imagePath)
{
    NSLog(@"**sendEmail**");
        
    if(emailDelegate == NULL) emailDelegate = [[Email alloc] init];
        
    NSString *nsTo = [StringTools createNSString:to];
    NSString *nsCc = [StringTools createNSString:cc];
    NSString *nsBcc = [StringTools createNSString:bcc];
    NSString *nsSubject = [StringTools createNSString:subject];
    NSString *nsBody = [StringTools createNSString:body];
    NSString *nsImage = [StringTools createNSString:imagePath];
        
    if([MFMailComposeViewController canSendMail])
    {
        MFMailComposeViewController *mailCont = [[MFMailComposeViewController alloc] init];
        [mailCont setToRecipients:[NSArray arrayWithObject:nsTo]];
        [mailCont setCcRecipients:[NSArray arrayWithObject:nsCc]];
        [mailCont setBccRecipients:[NSArray arrayWithObject:nsBcc]];
        [mailCont setMailComposeDelegate:emailDelegate];
        [mailCont setSubject:nsSubject];
        [mailCont setMessageBody:nsBody isHTML:YES];
        
        if(![nsImage isEqualToString:@""])
        {
            NSData *imageData = [NSData dataWithContentsOfFile:nsImage];
            NSString *fileName = [nsImage lastPathComponent];
            NSString *type = [@"image/" stringByAppendingString:[nsImage pathExtension]];
            [mailCont addAttachmentData:imageData mimeType:type fileName:fileName];
        }
            
        [UnityGetGLViewController() presentViewController:mailCont animated:YES completion:nil];
    }
}