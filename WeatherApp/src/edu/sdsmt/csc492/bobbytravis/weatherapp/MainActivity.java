package edu.sdsmt.csc492.bobbytravis.weatherapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import edu.sdsmt.csc492.bobbytravis.weatherapp.Model.Forecast;
import edu.sdsmt.csc492.bobbytravis.weatherapp.Model.ForecastLocation;
import edu.sdsmt.csc492.bobbytravis.weatherapp.view.FragmentForecast;

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
                
                //MOVED THIS TO WITHIN showForecast
                //CAN USE THE ON CREATE METHOD TO PASS BUNDLE
                /*
                // If the fragment is not found, create it.
                _fragmentForecast = (FragmentForecast) _fragmentManager.findFragmentByTag(FRAGMENT_FORECAST_TAG);
                {
                        _fragmentForecast = new FragmentForecast();
                }
                
                // Only add/replace the list fragment if the bundle is empty; otherwise,
                // the activity is being re-created so keep the fragment that is already
                // displayed.
                if (savedInstanceState == null)
                {
                        _fragmentManager.beginTransaction()
                         .replace(R.id.fragmentContainerFrame, _fragmentForecast, FRAGMENT_FORECAST_TAG)
                         .commit();
                }*/

                _forecast = Forecast.getInstance();
                _forecastLocation = ForecastLocation.getInstance();
                
                // By default, first element is "favorite" city, go get location.
                // TextUtils.split() takes a regular expression and in the case
                // of a pipe delimiter, it needs to be escaped.
                showForecast(savedInstanceState);
                
                
        }

        private void showForecast(Bundle savedInstanceState)
        {
        		Bundle bundle = new Bundle();
                // HINT: Use bundle to pass arguments to fragment.
                //
                //                Bundle bundle = new Bundle();
                //                bundle.putString("key", "value");
                //                ForecastFragment.setArguments(bundle);
                
                // HINT: FragmentManager().beginTransaction()
        		/*bundle.putString("Temp", _forecast.Temp);
        		bundle.putString("FeelsLikeTemp", _forecast.FeelsLikeTemp);
        		bundle.putString("Humidity", _forecast.Humidity);
        		bundle.putString("ChanceOfPrecip", _forecast.ChanceOfPrecip);
        		bundle.putString("AsOfTime", _forecast.AsOfTime);*/
                
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
                }
        		_forecastLocation.getForecastLocation(_fragmentForecast);
        		_forecast.getForecast(_fragmentForecast);
                
        }
}
