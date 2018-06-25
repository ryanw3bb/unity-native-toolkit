//
//  StringTools.m
//  NativeToolkit
//
//  Created by Ryan on 01/02/2015.
//
//

#import "StringTools.h"

@implementation StringTools

+(NSString*) createNSString:(const char*)string
{
    if(string)
        return [NSString stringWithUTF8String:string];
    else
        return [NSString stringWithUTF8String:""];
}

+(char*) createCString:(const char*)string
{
    if (string == NULL)
        return NULL;
    
    char* res = (char*)malloc(strlen(string) + 1);
    strcpy(res, string);
    
    return res;
}

@end