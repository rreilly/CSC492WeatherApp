package edu.sdsmt.csc492.bobbytravis.weatherapp.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.sdsmt.csc492.bobbytravis.weatherapp.R;

public class FragmentForecast extends Fragment
{
        public static final String LOCATION_KEY = "key_location";
        public static final String FORECAST_KEY = "key_forecast";
        
        private TextView _textViewTemp;
        private TextView _textViewFeelsLikeTemp;
        private TextView _textViewHumidity;
        private TextView _textViewChanceOfPrecip;
        private TextView _textViewAsOfTime;

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
                
                // set values
                _textViewTemp.setText(savedInstanceState.getString("Temp"));
                _textViewFeelsLikeTemp.setText(savedInstanceState.getString("FeelsLikeTemp"));
                _textViewHumidity.setText(savedInstanceState.getString("Humidity"));
                _textViewChanceOfPrecip.setText(savedInstanceState.getString("ChanceOfPrecip"));
                _textViewAsOfTime.setText(savedInstanceState.getString("AsOfTime"));
                
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
}
