//
//  Contacts.m
//  NativeToolkit
//
//  Created by Ryan on 02/04/2015.
//
//

#import "Contacts.h"
#import "StringTools.h"
#include "Unity/UnityInterface.h"

extern UIViewController *UnityGetGLViewController();

@implementation Contacts

- (void)peoplePickerNavigationControllerDidCancel:(ABPeoplePickerNavigationController *)peoplePicker
{
    NSDictionary *dict = @{ @"cancelled" : @"true" };
    
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict
                                                       options:NSJSONWritingPrettyPrinted
                                                       error:&error];
    
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    const char *jsonCString = [StringTools createCString:[jsonString UTF8String]];
    UnitySendMessage("NativeToolkit", "OnPickContactFinished", jsonCString);
    
    [self dismissModalViewControllerAnimated:YES];
}

// ios 8
- (void)peoplePickerNavigationController:(ABPeoplePickerNavigationController *)peoplePicker didSelectPerson:(ABRecordRef)person
{
    [self peoplePickerNavigationController:peoplePicker shouldContinueAfterSelectingPerson:person];
}

// ios 7 and lower
- (BOOL)peoplePickerNavigationController:(ABPeoplePickerNavigationController *)peoplePicker
      shouldContinueAfterSelectingPerson:(ABRecordRef)person
{
    NSString *name = (__bridge NSString *)ABRecordCopyValue(person, kABPersonFirstNameProperty);
    NSString *lastname = (__bridge NSString *)ABRecordCopyValue(person, kABPersonLastNameProperty);
    if(lastname != NULL) name = [[name stringByAppendingString:@" "]stringByAppendingString:lastname];
    
    ABMultiValueRef numbers = ABRecordCopyValue(person, kABPersonPhoneProperty);
    NSString *number = (__bridge NSString *)ABMultiValueCopyValueAtIndex(numbers, 0);
    if(number == NULL) number = @"";
    
    ABMultiValueRef emails = ABRecordCopyValue(person, kABPersonEmailProperty);
    NSString *email = (__bridge NSString *)ABMultiValueCopyValueAtIndex(emails, 0);
    if(email == NULL) email = @"";
    
    NSDictionary *dict = @{ @"name" : name, @"number" : number, @"email" : email };
    
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:&error];
    
    if (jsonData)
    {
        NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
        const char *jsonCString = [StringTools createCString:[jsonString UTF8String]];
        UnitySendMessage("NativeToolkit", "OnPickContactFinished", jsonCString);
    }
    else
    {
        NSLog(@"Got an error: %@", error);
    }
    
    return NO;
}

@end


static Contacts* contactsDelegate = NULL;

void pickContact()
{
    if(contactsDelegate == NULL) contactsDelegate = [[Contacts alloc] init];
        
    ABPeoplePickerNavigationController *picker = [[ABPeoplePickerNavigationController alloc] init];
    picker.peoplePickerDelegate = contactsDelegate;
    
    [UnityGetGLViewController() presentModalViewController:picker animated:YES];
}
