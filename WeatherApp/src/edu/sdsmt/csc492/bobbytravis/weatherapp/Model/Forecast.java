package edu.sdsmt.csc492.bobbytravis.weatherapp.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import edu.sdsmt.csc492.bobbytravis.weatherapp.IListeners;

public class Forecast implements Parcelable
{

        private static final String TAG = "";
        
        // http://developer.weatherbug.com/docs/read/WeatherBug_API_JSON
        // NOTE: See example JSON in doc folder.
        private static String _URL = "http://i.wxbug.net/REST/Direct/GetForecastHourly.ashx?zip=" + "%s" +
         "&ht=t&ht=i&ht=cp&ht=fl&ht=h" +
         "&api_key=q3wj56tqghv7ybd8dy6gg4e7";
        
        private static final String zipCode = "57701";
        
        // http://developer.weatherbug.com/docs/read/List_of_Icons
                
        private static String _imageURL = "http://img.weather.weatherbug.com/forecast/icons/localized/500x420/en/trans/%s.png";
        
        public Bitmap Image;
        public String Temp;
        public String FeelsLikeTemp;
        public String Humidity;
        public String ChanceOfPrecip;
        public String AsOfTime;
        
        private static Forecast _instance;
        
        public Forecast()
        {
                Image = null;
        }
        
        public static synchronized Forecast getInstance()
        {
        	if (_instance == null)
        	{
        		_instance = new Forecast();
        	}
        	
        	return _instance;
        }

        private Forecast(Parcel parcel)
        {
                Image = parcel.readParcelable(Bitmap.class.getClassLoader());
        }

        @Override
        public int describeContents()
        {
                return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
                dest.writeParcelable(Image, 0);
        }
        
        public static final Parcelable.Creator<Forecast> Creator = new Parcelable.Creator<Forecast>()
        {
                @Override
                public Forecast createFromParcel(Parcel pc)
                {
                        return new Forecast(pc);
                }
                
                @Override
                public Forecast[] newArray(int size)
                {
                        return new Forecast[size];
                }
        };
        
        public void getForecast()
        {
        	new LoadForecast().execute(zipCode);
        }

        public static class LoadForecast extends AsyncTask<String, Void, JSONObject>
        {
                private IListeners _listener;
                private Context _context;

                private int bitmapSampleSize = -1;

                /*public LoadForecast(Context context, IListeners listener)
                {
                        _context = context;
                        _listener = listener;
                }*/

                protected JSONObject doInBackground(String... params)
                {
                        Forecast forecast = null;

                        try
                        {
                                // HINT: You will use the following classes to make API call.
                                //                 URL
                                // InputStreamReader
                                // JsonReader
                        		
                        		StringBuilder stringBuilder = new StringBuilder();
                        		HttpClient client = new DefaultHttpClient();
                        		
                        		HttpResponse response = client.execute(new HttpGet(String.format(_URL, zipCode)));
                        		if( response.getStatusLine().getStatusCode() == 200)
                        		{
                        			HttpEntity entity = response.getEntity();
                        			InputStream content = entity.getContent();
                        			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                        			
                        			String line;
                        			while((line = reader.readLine()) != null)
                        			{
                        				stringBuilder.append(line);
                        			}
                        			// Need to implement this method
                        			//forecastLocation = readJSON(stringBuilder.toString());
                        			JSONTokener tokener = new JSONTokener(stringBuilder.toString());
                        			JSONObject result = new JSONObject(tokener);
                        			
                        			return result;
                        		}
                        		
                        }
                        catch (IllegalStateException e)
                        {
                                Log.e(TAG, e.toString() + params[0]);
                        }
                        catch (Exception e)
                        {
                                Log.e(TAG, e.toString());
                        }

                       return null;
                }

                /*protected void onPostExecute(JSONArray result)
                {
                        return result;
                }*/

                private Bitmap readIconBitmap(String conditionString, int bitmapSampleSize)
                {
                        Bitmap iconBitmap = null;
                        try
                        {
                                URL weatherURL = new URL(String.format(_imageURL, conditionString));

                                BitmapFactory.Options options = new BitmapFactory.Options();
                                if (bitmapSampleSize != -1)
                                {
                                        options.inSampleSize = bitmapSampleSize;
                                }

                                iconBitmap = BitmapFactory.decodeStream(weatherURL.openStream(), null, options);
                        }
                        catch (MalformedURLException e)
                        {
                                Log.e(TAG, e.toString());
                        }
                        catch (IOException e)
                        {
                                Log.e(TAG, e.toString());
                        }
                        catch (Exception e)
                        {
                                Log.e(TAG, e.toString());
                        }

                        return iconBitmap;
                }
        }
}
