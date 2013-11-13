package edu.sdsmt.csc492.bobbytravis.weatherapp;

import edu.sdsmt.csc492.bobbytravis.weatherapp.Model.Forecast;
import edu.sdsmt.csc492.bobbytravis.weatherapp.Model.ForecastLocation;

public interface IListeners
{
        public void onLocationLoaded(ForecastLocation forecastLocation);
        public void onForecastLoaded(Forecast forecast);
}
