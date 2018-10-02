//
//  Locale.h
//  NativeToolkit
//
//  Created by Ryan on 31/01/2015.
//
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import <UIKit/UIKit.h>

@interface Locale : NSObject <CLLocationManagerDelegate>

- (Locale *)init;
- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations;

@end
