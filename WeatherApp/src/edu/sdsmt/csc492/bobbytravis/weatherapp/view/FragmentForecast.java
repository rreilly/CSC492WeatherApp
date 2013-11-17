package edu.sdsmt.csc492.bobbytravis.weatherapp.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        
        private Forecast _forecast;
        private ForecastLocation _location;
        
        private TextView _textViewLocation;
        private TextView _textViewTempLabel;
        private TextView _textViewTemp;
        private TextView _textViewFeelsLikeTempLabel;
        private TextView _textViewFeelsLikeTemp;
        private TextView _textViewHumidityLabel;
        private TextView _textViewHumidity;
        private TextView _textViewChanceOfPrecipLabel;
        private TextView _textViewChanceOfPrecip;
        private TextView _textViewAsOfTimeLabel;
        private TextView _textViewAsOfTime;
        private ProgressBar _progressBar;
        private TextView _textViewProgressBar;
        private ImageView _imageView;
        
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                
                if(savedInstanceState != null)
                {
                	_forecast = savedInstanceState.getParcelable(FORECAST_KEY);
                	_location = savedInstanceState.getParcelable(LOCATION_KEY);
                }
        }

        @Override
        public void onSaveInstanceState(Bundle savedInstanceStateBundle)
        {
                super.onSaveInstanceState(savedInstanceStateBundle);
                savedInstanceStateBundle.putParcelable(FORECAST_KEY, _forecast);
                savedInstanceStateBundle.putParcelable(LOCATION_KEY, _location);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
                View rootView = inflater.inflate(R.layout.fragment_forecast, null);
                
                // Assign instances of Views from the Layout Resource.
                _textViewLocation = (TextView) rootView.findViewById(R.id.textViewLocation);
                _textViewTempLabel = (TextView) rootView.findViewById(R.id.labelTemp);
                _textViewTemp = (TextView) rootView.findViewById(R.id.textViewTemp);
                _textViewFeelsLikeTempLabel = (TextView) rootView.findViewById(R.id.labelFeelsLikeTemp);
                _textViewFeelsLikeTemp = (TextView) rootView.findViewById(R.id.textViewFeelsLikeTemp);
                _textViewHumidityLabel = (TextView) rootView.findViewById(R.id.labelHumidity);
                _textViewHumidity = (TextView) rootView.findViewById(R.id.textViewHumidity);
                _textViewChanceOfPrecipLabel = (TextView) rootView.findViewById(R.id.labelChanceOfPrecip);
                _textViewChanceOfPrecip = (TextView) rootView.findViewById(R.id.textViewChanceOfPrecip);
                _textViewAsOfTimeLabel = (TextView) rootView.findViewById(R.id.labelAsOfTime);
                _textViewAsOfTime = (TextView) rootView.findViewById(R.id.textViewAsOfTime);
                _progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
                _textViewProgressBar = (TextView) rootView.findViewById(R.id.textViewProgressBar);
                _imageView = (ImageView) rootView.findViewById(R.id.imageForecast);
                
                // Hide the display initially
                _textViewLocation.setVisibility(4);
                _textViewTemp.setVisibility(4);
                _textViewFeelsLikeTemp.setVisibility(4);
                _textViewHumidity.setVisibility(4);
                _textViewChanceOfPrecip.setVisibility(4);
                _textViewAsOfTime.setVisibility(4);
                
                _textViewTempLabel.setVisibility(4);
                _textViewFeelsLikeTempLabel.setVisibility(4);
                _textViewHumidityLabel.setVisibility(4);
                _textViewChanceOfPrecipLabel.setVisibility(4);
                _textViewAsOfTimeLabel.setVisibility(4);
                
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
        
        @Override
        public void onLocationLoaded(ForecastLocation forecastLocation)
        {
        	_location = forecastLocation;
        	if(forecastLocation == null)
        	{
        		Toast.makeText(getActivity(), "Unable to retrieve location", Toast.LENGTH_SHORT).show();
        		return;        		
        	}
        	
        	_textViewLocation.setText(forecastLocation.getCity() + " " + forecastLocation.getState());
        	
        	_textViewLocation.setVisibility(0);
        }
        
        @Override
        public void onForecastLoaded(Forecast forecast)
        {
        	_forecast = forecast;
        	if(forecast == null)
        	{
        		Toast.makeText(getActivity(), "Unable to retrieve forecast", Toast.LENGTH_SHORT).show();
        		return; 
        	}
        	
        	if(forecast.getImage() == null)
        	{
        		Toast.makeText(getActivity(), "Unable to load icon", Toast.LENGTH_SHORT).show();
        	}
        	else // Display icon bitmap
        	{
        		_imageView.setImageBitmap(forecast.getImage());
        	}
        	
            // Fill the text fields
        	_textViewTemp.setText(forecast.Temp + "F");
            _textViewFeelsLikeTemp.setText(forecast.FeelsLikeTemp + "F");
            _textViewHumidity.setText(forecast.Humidity + "%");
            _textViewChanceOfPrecip.setText(forecast.ChanceOfPrecip + "%");
            _textViewAsOfTime.setText(formatDateTime(forecast.AsOfTime));
            
        	// Hide the progress bar
        	_progressBar.setVisibility(4);
        	_textViewProgressBar.setVisibility(4);
        	
        	// Show the other stuff
            _textViewTemp.setVisibility(0);
            _textViewFeelsLikeTemp.setVisibility(0);
            _textViewHumidity.setVisibility(0);
            _textViewChanceOfPrecip.setVisibility(0);
            _textViewAsOfTime.setVisibility(0);
            
            _textViewTempLabel.setVisibility(0);
            _textViewFeelsLikeTempLabel.setVisibility(0);
            _textViewHumidityLabel.setVisibility(0);
            _textViewChanceOfPrecipLabel.setVisibility(0);
            _textViewAsOfTimeLabel.setVisibility(0);
            
        }
        
        // Function courtesy of Andrew Thompson, make sure to give credit when we document this up
		public String formatDateTime(String timestamp)
		{
		    Date date = new Date(Long.valueOf(timestamp));   
		    DateFormat dateFormat = new SimpleDateFormat("EEE MMM d, h:mm a", Locale.US);
		    dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
		    return dateFormat.format(date);
		}
}
