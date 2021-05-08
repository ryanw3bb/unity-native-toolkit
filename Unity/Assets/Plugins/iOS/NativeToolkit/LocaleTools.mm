//
//  LocaleTools.m
//  NativeToolkit
//
//  Created by Ryan on 31/01/2015.
//
//

#import "LocaleTools.h"
#import "StringTools.h"

double latitude;
double longitude;

@implementation LocaleTools

CLLocationManager *locationManager;

- (LocaleTools *)init
{
    locationManager = [[CLLocationManager alloc] init];
    locationManager.delegate = self;
    locationManager.distanceFilter = kCLDistanceFilterNone;
    locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0)
        [locationManager requestWhenInUseAuthorization];
    
    [locationManager startUpdatingLocation];
    
    return self;
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations;
{
    CLLocation *location = [locations lastObject];
    latitude = location.coordinate.latitude;
    longitude = location.coordinate.longitude;
    
    //NSLog(@"lat:%f long:%f", latitude, longitude);
}

@end

static LocaleTools* localeDelegate = NULL;

extern "C"
{
    char* getLocale()
    {
        NSLocale *locale = [NSLocale currentLocale];
        NSString *countryCode = [locale objectForKey: NSLocaleCountryCode];
        
        NSLog(@"##locale: %@", countryCode);
        
        return [StringTools createCString:[countryCode UTF8String]];
    }
    
    void startLocation()
    {
        if(localeDelegate == NULL) localeDelegate = [[LocaleTools alloc] init];
    }
    
    double getLongitude()
    {
        return longitude;
    }
    
    double getLatitude()
    {
        return latitude;
    }
}
