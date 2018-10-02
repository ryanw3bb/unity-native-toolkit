package com.secondfury.nativetoolkit;

import android.content.Context;
import android.os.Bundle;
import android.location.LocationManager;
import android.location.LocationListener;

public class Location implements LocationListener
{
    public static android.location.Location LastLocation;

    private LocationManager locationManager;

    public void init(Context context)
    {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 0, this);
    }

    @Override
    public void onLocationChanged(android.location.Location location)
    {
        LastLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }
}
