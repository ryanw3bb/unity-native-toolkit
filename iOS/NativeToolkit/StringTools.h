//
//  StringTools.h
//  NativeToolkit
//
//  Created by Ryan on 01/02/2015.
//
//

#import <Foundation/Foundation.h>

@interface StringTools : NSObject

+(NSString*) createNSString:(const char*)string;
+(char*) createCString:(const char*)string;

@end
