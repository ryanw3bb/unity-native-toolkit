//
//  Locale.m
//  NativeToolkit
//
//  Created by Ryan on 31/01/2015.
//
//

#import "Locale.h"
#import "StringTools.h"

@implementation Locale

@end

extern "C"
{
    char* getLocale()
    {
        NSLocale *locale = [NSLocale currentLocale];
        NSString *countryCode = [locale objectForKey: NSLocaleCountryCode];
        
        NSLog(@"##locale: %@", countryCode);
        
        return [StringTools createCString:[countryCode UTF8String]];
    }
}