package edu.sdsmt.csc492.bobbytravis.weatherapp.Model;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import edu.sdsmt.csc492.bobbytravis.weatherapp.IListeners;

/**
 * Forecast object to retrieve the forecast data
 * @author Bobby Reilly and Travis Larson
 *
 */
public class Forecast implements Parcelable
{

        private static final String TAG = "";
        
        // http://developer.weatherbug.com/docs/read/WeatherBug_API_JSON
        // NOTE: See example JSON in doc folder.
        private static String _URL = "http://i.wxbug.net/REST/Direct/GetForecastHourly.ashx?zip=" + "%s" +
         "&ht=t&ht=i&ht=cp&ht=fl&ht=h" +
         "&api_key=ud28nxu36cn6hxg84xgc6t6d";
        
        private static final String zipCode = "57701";
        
        // http://developer.weatherbug.com/docs/read/List_of_Icons
                
        private static String _imageURL = "http://img.weather.weatherbug.com/forecast/icons/localized/500x420/en/trans/%s.png";
        
        public static Bitmap Image;
        public String Temp;
        public String FeelsLikeTemp;
        public String Humidity;
        public String ChanceOfPrecip;
        public String AsOfTime;
        public String Icon;
        
        private static Forecast _instance;
        
        public Forecast()
        {
                Image = null;
                _instance = this;
        }
        
        /**
         * CREATOR field to let us implement the class as parcelable
         * @author Travis Larson
         */
        public static final Parcelable.Creator<Forecast> CREATOR = new Parcelable.Creator<Forecast>()
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
        
        public static synchronized Forecast getInstance()
        {
        	if (_instance == null)
        	{
        		_instance = new Forecast();
        	}
        	
        	return _instance;
        }
        
        public Bitmap getImage()
        {
        	return Image;
        }

        private Forecast(Parcel parcel)
        {
                Image = parcel.readParcelable(Bitmap.class.getClassLoader());
                Temp = parcel.readString();
                AsOfTime = parcel.readString();
                ChanceOfPrecip = parcel.readString();
                FeelsLikeTemp = parcel.readString();
                Humidity = parcel.readString();
                Icon = parcel.readString();
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
                dest.writeString(Temp);
                dest.writeString(AsOfTime);
                dest.writeString(ChanceOfPrecip);
                dest.writeString(FeelsLikeTemp);
                dest.writeString(Humidity);
                dest.writeString(Icon);
        }
        
        /**
         * Setup function to call the async task.
         * @author Bobby Reilly
         * @param viewFragment Fragment that the result will return to.
         */
        public void getForecast(Fragment viewFragment)
        {
        	new LoadForecast( (IListeners) viewFragment).execute(zipCode);
        }

        /**
         * Class extending AsyncTask to prevent locking the UI thread while
         * waiting for data retrieval.
         * @author Bobby Reilly and Travis Larson
         * 
         */
        public static class LoadForecast extends AsyncTask<String, Void, List<String>>
        {
                private static IListeners _listener;

                /**
                 * Set the listener so callback can be made.
                 * @author Bobby Reilly
                 * @param listener Fragment implementing the IListeners interface
                 */
                public LoadForecast( IListeners listener)
                {
                        _listener = listener;
                }

                /**
                 * Async task that occurs on a seperate string to prevent the
                 * app from hanging. Retrieves and parses the json object.
                 * @author Bobby Reilly and Travis Larson
                 */
                protected List<String> doInBackground(String... params)
                {
                        try
                        {
                        		HttpClient client = new DefaultHttpClient();
                        		JsonToken type = null;
                        		
                        		HttpResponse response = client.execute(new HttpGet(String.format(_URL, params[0])));
                        		if( response.getStatusLine().getStatusCode() == 200)
                        		{
                        			HttpEntity entity = response.getEntity();
                        			InputStream content = entity.getContent();
                        			
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
                        	            		message.add(String.valueOf(jreader.nextLong()));
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
											default:
												jreader.skipValue();
												break;
                        	            	}
                        	            }
                        	            jreader.endObject();
                        	            jreader.close();
                        	            
                        	            Image = readIconBitmap(message.listIterator(message.indexOf("icon")+1).next(),1);
                        	            
                        	            return message;                        	            
                        			}
                        			catch( Exception e)
                        			{

                        			}
                        		}
                        }
                        catch (IllegalStateException e)
                        {
                                Log.e(TAG, e.toString() + params[0]);
                				return null;
                        }
                        catch (Exception e)
                        {
                                Log.e(TAG, e.toString());
                				return null;
                        }

                        return null;
                }

                /**
                 * Called after doInBackground, assigns instance variables and
                 * triggers listener.
                 * @author Bobby Reilly and Travis Larson
                 */
                protected void onPostExecute(List<String> result)
                {	
                	if( result == null )
                	{
                		_listener.onForecastLoaded(null);
                		return;
                	}
                	Iterator<String> iterator;
                	
                    //load values to forecast variables 
                	iterator = result.listIterator(result.indexOf("temperature")+1);
                    _instance.Temp = iterator.next();
                        	            
                    iterator = result.listIterator(result.indexOf("feelsLike")+1);
                    _instance.FeelsLikeTemp = iterator.next();
                        	            
                    iterator = result.listIterator(result.indexOf("humidity")+1);
                    _instance.Humidity = iterator.next();
                        	            
                    iterator = result.listIterator(result.indexOf("chancePrecip")+1);
                    _instance.ChanceOfPrecip = iterator.next();
                        	            
                    iterator = result.listIterator(result.indexOf("dateTime")+1);
                    _instance.AsOfTime = iterator.next();
                    
                    _listener.onForecastLoaded(_instance);
                }

                /**
                 * Code provided by Brian Butterfield to retrieve the weather icon
                 * @author Brian Butterfield
                 * @param conditionString Condition number based on forecast information
                 * @param bitmapSampleSize Size to make the bitmap
                 * @return
                 */
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
                                return null;
                        }
                        catch (IOException e)
                        {
                                Log.e(TAG, e.toString());
                                return null;
                        }
                        catch (Exception e)
                        {
                                Log.e(TAG, e.toString());
                                return null;
                        }

                        return iconBitmap;
                }
                
        }
}
