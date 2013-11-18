package edu.sdsmt.csc492.bobbytravis.weatherapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import edu.sdsmt.csc492.bobbytravis.weatherapp.Model.Forecast;
import edu.sdsmt.csc492.bobbytravis.weatherapp.Model.ForecastLocation;
import edu.sdsmt.csc492.bobbytravis.weatherapp.view.FragmentForecast;

/**
 * Base activity to start the app. Begins view fragment and forecast data models.
 * @author Bobby Reilly
 *
 */
public class MainActivity extends Activity
{
		private final static String FRAGMENT_FORECAST_TAG = "ForecastTag";
	
        private FragmentManager _fragmentManager;
        private FragmentForecast _fragmentForecast;
        private Forecast _forecast;
        private ForecastLocation _forecastLocation;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                
                // Get City array from resources.
                // Get a reference to the fragment manager to
                // be used for adding/replacing fragments.
                _fragmentManager = getFragmentManager();
                
                // get forecast and location
                _forecast = Forecast.getInstance();
                _forecastLocation = ForecastLocation.getInstance();
                
                // load fragment and start data retrieval
                showForecast(savedInstanceState);
        }

        /**
         * @author Bobby Reilly
         * @param savedInstanceState Existing bundle
         */
        private void showForecast(Bundle savedInstanceState)
        {
        		Bundle bundle = new Bundle();
                                
                // If the fragment is not found, create it.
                _fragmentForecast = (FragmentForecast) _fragmentManager.findFragmentByTag(FRAGMENT_FORECAST_TAG);
                {
                        _fragmentForecast = new FragmentForecast();
                }
                _fragmentForecast.setArguments(bundle);
                
                // Only add/replace the list fragment if the bundle is empty; otherwise,
                // the activity is being re-created so keep the fragment that is already
                // displayed.
                if (savedInstanceState == null)
                {
                        _fragmentManager.beginTransaction()
                         .replace(R.id.fragmentContainerFrame, _fragmentForecast, FRAGMENT_FORECAST_TAG)
                         .commit();
                        _forecastLocation.getForecastLocation(_fragmentForecast);
                		_forecast.getForecast(_fragmentForecast);
                }                
        }
}
