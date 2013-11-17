package edu.sdsmt.csc492.bobbytravis.weatherapp.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import edu.sdsmt.csc492.bobbytravis.weatherapp.R;
import edu.sdsmt.csc492.bobbytravis.weatherapp.IListeners;
import edu.sdsmt.csc492.bobbytravis.weatherapp.Model.Forecast;
import edu.sdsmt.csc492.bobbytravis.weatherapp.Model.ForecastLocation;

public class FragmentForecast extends Fragment implements IListeners
{
        public static final String LOCATION_KEY = "key_location";
        public static final String FORECAST_KEY = "key_forecast";
        
        private TextView _textViewTemp;
        private TextView _textViewFeelsLikeTemp;
        private TextView _textViewHumidity;
        private TextView _textViewChanceOfPrecip;
        private TextView _textViewAsOfTime;
        private ProgressBar _progressBar;
        private TextView _progressBarTextView;
        
        @Override
        public void onCreate(Bundle argumentsBundle)
        {
                super.onCreate(argumentsBundle);
                
        }

        @Override
        public void onSaveInstanceState(Bundle savedInstanceStateBundle)
        {
                super.onSaveInstanceState(savedInstanceStateBundle);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
                View rootView = inflater.inflate(R.layout.fragment_forecast, null);
                
                // Assign instances of Views from the Layout Resource.
                _textViewTemp = (TextView) rootView.findViewById(R.id.textViewTemp);
                _textViewFeelsLikeTemp = (TextView) rootView.findViewById(R.id.textViewFeelsLikeTemp);
                _textViewHumidity = (TextView) rootView.findViewById(R.id.textViewHumidity);
                _textViewChanceOfPrecip = (TextView) rootView.findViewById(R.id.textViewChanceOfPrecip);
                _textViewAsOfTime = (TextView) rootView.findViewById(R.id.textViewAsOfTime);
                _progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
                _progressBarTextView = (TextView) rootView.findViewById(R.id.textViewProgressBar);
                
                // Hide the display initially
                _textViewTemp.setVisibility(4);
                _textViewFeelsLikeTemp.setVisibility(4);
                _textViewHumidity.setVisibility(4);
                _textViewChanceOfPrecip.setVisibility(4);
                _textViewAsOfTime.setVisibility(4);
                
                // set values
                /*_textViewTemp.setText(savedInstanceState.getString("Temp"));
                _textViewFeelsLikeTemp.setText(savedInstanceState.getString("FeelsLikeTemp"));
                _textViewHumidity.setText(savedInstanceState.getString("Humidity"));
                _textViewChanceOfPrecip.setText(savedInstanceState.getString("ChanceOfPrecip"));
                _textViewAsOfTime.setText(savedInstanceState.getString("AsOfTime"));*/
                
                return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceStateBundle)
        {
                super.onActivityCreated(savedInstanceStateBundle);
        }

        @Override
        public void onDestroy()
        {
                
                super.onDestroy();
        }
        
        public void onLocationLoaded(ForecastLocation forecastLocation)
        {
        	
        }
        
        public void onForecastLoaded(Forecast forecast)
        {
        	if(forecast == null)
        	{
        		Toast.makeText(getActivity(), "Unable to contact server", Toast.LENGTH_SHORT).show();
        		return; 
        	}
        	_progressBar.setVisibility(4);
        	_progressBarTextView.setVisibility(4);
            _textViewTemp.setVisibility(0);
            _textViewFeelsLikeTemp.setVisibility(0);
            _textViewHumidity.setVisibility(0);
            _textViewChanceOfPrecip.setVisibility(0);
            _textViewAsOfTime.setVisibility(0);
        	_textViewTemp.setText(forecast.Temp);
            _textViewFeelsLikeTemp.setText(forecast.FeelsLikeTemp);
            _textViewHumidity.setText(forecast.Humidity);
            _textViewChanceOfPrecip.setText(forecast.ChanceOfPrecip);
            _textViewAsOfTime.setText(forecast.AsOfTime);
            
        }
}
