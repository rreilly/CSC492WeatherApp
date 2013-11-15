package edu.sdsmt.csc492.bobbytravis.weatherapp.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.util.JsonToken;
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
        public static String Temp;
        public static String FeelsLikeTemp;
        public static String Humidity;
        public static String ChanceOfPrecip;
        public static String AsOfTime;
        
        private static Forecast _instance;
        
        public Forecast()
        {
                Image = null;
                _instance = this;
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
        
        public void getForecast(Fragment viewFragment)
        {
        	new LoadForecast( this, (IListeners) viewFragment).execute(zipCode);
        }

        public static class LoadForecast extends AsyncTask<String, Void, List<String>>
        {
                private IListeners _listener;
                //private Context _context;

                private int bitmapSampleSize = -1;

                public LoadForecast(Forecast forecast, IListeners listener)
                {
                        //_context = forecast;
                        _listener = listener;
                }

                protected List<String> doInBackground(String... params)
                {
                        //Forecast forecast = null;
                        //char quote = '"';

                        try
                        {
                                // HINT: You will use the following classes to make API call.
                                //                 URL
                                // InputStreamReader
                                // JsonReader
                        		
                        		StringBuilder stringBuilder = new StringBuilder();
                        		HttpClient client = new DefaultHttpClient();
                        		JsonToken type = null;
                        		
                        		HttpResponse response = client.execute(new HttpGet(String.format(_URL, zipCode)));
                        		if( response.getStatusLine().getStatusCode() == 200)
                        		{
                        			HttpEntity entity = response.getEntity();
                        			InputStream content = entity.getContent();
                        			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                        			
                        			try
                        			{
                        				JsonReader jreader = new JsonReader(new InputStreamReader(content, "UTF-8"));
                        				List<String> message = new ArrayList<String>();
                        				
                        				jreader.beginObject();
                        				//while loop to go through json object
                        	            while (jreader.hasNext()) {
                        	            	type = jreader.peek();
                        	            	switch(type){
                        	            	case STRING:
                        	            		message.add(jreader.nextString());
                        	            		break;
                        	            	case NAME:
                        	            		message.add(jreader.nextName());
                        	            		break;
                        	            	case NUMBER:
                        	            		long number = jreader.nextLong();
                        	            		message.add(String.valueOf(number));
                        	            		break;
                        	            	case BEGIN_ARRAY:
                        	            		jreader.beginArray();
                        	            		break;
                        	            	case END_ARRAY:
                        	            		jreader.endArray();
                        	            		break;
                        	            	case BEGIN_OBJECT:
                        	            		jreader.beginObject();
                        	            		break;
                        	            	case END_OBJECT:
                        	            		jreader.endObject();
                        	            		break;
                        	            	case NULL:
                        	            		jreader.skipValue();
                        	            		break;
                        	            	}
                        	            }
                        	            jreader.endObject();
                        	            jreader.close();
                        	            
                        	            return message;                        	            
                        			}
                        			catch( Exception e)
                        			{
                        				
                        			}
                        			
                        			//old style, need to see if JsonReader will work better
                        			/*String line;
                        			while((line = reader.readLine()) != null)
                        			{
                        				stringBuilder.append(line);
                        			}
                        			// Need to implement this method
                        			//forecastLocation = readJSON(stringBuilder.toString());
                        			JSONTokener tokener = new JSONTokener(stringBuilder.toString());
                        			line = tokener.nextString(quote);
                        			
                        			int a = 1;*/
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

                protected void onPostExecute(List<String> result)
                {
                	Iterator<String> iterator;
                	
                    //load values to forecast variables
                	iterator = result.listIterator(result.indexOf("temperature"));
                    _instance.Temp = iterator.next();
                        	            
                    iterator = result.listIterator(result.indexOf("feelsLike"));
                    _instance.FeelsLikeTemp = iterator.next();
                        	            
                    iterator = result.listIterator(result.indexOf("humidity"));
                    _instance.Humidity = iterator.next();
                        	            
                    iterator = result.listIterator(result.indexOf("chancePrecip"));
                    _instance.ChanceOfPrecip = iterator.next();
                        	            
                    iterator = result.listIterator(result.indexOf("dateTime"));
                    _instance.AsOfTime = iterator.next();
                    
                    _listener.onForecastLoaded(_instance);
                }

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
