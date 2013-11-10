package edu.sdsmt.csc492.bobbytravis.weatherapp;

import edu.sdsmt.csc492.bobbytravis.weatherapp.model.Forecast;
import edu.sdsmt.csc492.bobbytravis.weatherapp.model.ForecastLocation;

public interface IListeners
{
        public void onLocationLoaded(ForecastLocation forecastLocation);
        public void onForecastLoaded(Forecast forecast);
}
