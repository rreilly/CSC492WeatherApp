package edu.sdsmt.csc492.bobbytravis.weatherapp;

import org.json.JSONArray;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.TextUtils;
import edu.sdsmt.csc492.bobbytravis.weatherapp.Model.Forecast;
import edu.sdsmt.csc492.bobbytravis.weatherapp.Model.ForecastLocation;
import edu.sdsmt.csc492.bobbytravis.weatherapp.view.FragmentForecast;

public class MainActivity extends Activity implements IListeners
{
		private final static String FRAGMENT_FORECAST_TAG = "ForecastTag";
	
        private String[] _citiesArray;
        private FragmentManager _fragmentManager;
        private FragmentForecast _fragmentForecast;
        private Forecast _forecast;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                
                // Get City array from resources.
                _citiesArray = getResources().getStringArray(R.array.cityArray);
                
                // Get a reference to the fragment manager to
                // be used for adding/replacing fragments.
                _fragmentManager = getFragmentManager();
                
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
                }

                _forecast = Forecast.getInstance();
                
                // By default, first element is "favorite" city, go get location.
                // TextUtils.split() takes a regular expression and in the case
                // of a pipe delimiter, it needs to be escaped.
                showForecast(TextUtils.split(_citiesArray[0], "\\|")[0]);
                
                
        }

        private void showForecast(String zipCode)
        {
        		_forecast.getForecast();
                // HINT: Use bundle to pass arguments to fragment.
                //
                //                Bundle bundle = new Bundle();
                //                bundle.putString("key", "value");
                //                ForecastFragment.setArguments(bundle);
                
                // HINT: FragmentManager().beginTransaction()
                Bundle bundle = new Bundle();
                bundle.putString("ZipCode", zipCode);
                _fragmentForecast.setArguments(bundle);
                
                
        }

		@Override
		public void onLocationLoaded(ForecastLocation forecastLocation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onForecastLoaded(Forecast forecast) {
			// TODO Auto-generated method stub
			
		}

}
