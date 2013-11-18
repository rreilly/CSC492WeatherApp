package edu.sdsmt.csc492.bobbytravis.weatherapp.Model;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import edu.sdsmt.csc492.bobbytravis.weatherapp.IListeners;

/**
 * ForecastLocation object to retrieve the location the forecast is for.
 * @author Travis Larson
 *
 */
public class ForecastLocation implements Parcelable
{

        private static final String TAG = "";
        
        private static ForecastLocation _instance;
        
        private static final String ZipCode = "57701";
        public String City;
        public String State;
        // http://developer.weatherbug.com/docs/read/WeatherBug_API_JSON
        // NOTE: See example JSON in doc folder.
        private static final String _URL = "http://i.wxbug.net/REST/Direct/GetData.ashx?zip=" + "%s" +
                         "&dt=l&api_key=ud28nxu36cn6hxg84xgc6t6d";
        

        public ForecastLocation()
        {
                City = null;
                State = null;

        }
        
        private ForecastLocation(Parcel parcel)
        {
        	City = parcel.readString();
        	State = parcel.readString();
        }
        
        public static synchronized ForecastLocation getInstance()
        {
        	if (_instance == null)
        	{
        		_instance = new ForecastLocation();
        	}
        	
        	return _instance;
        }
        
		@Override
		public int describeContents() 
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) 
		{
			dest.writeString(City);
			dest.writeString(State);
		}
		
		/**
         * CREATOR field to let us implement the class as parcelable
         * @author Travis Larson
         */
		public static final Parcelable.Creator<ForecastLocation> CREATOR = new Parcelable.Creator<ForecastLocation>()
		{
			public ForecastLocation createFromParcel(Parcel pc)
			{
				return new ForecastLocation(pc);
			}
			
			public ForecastLocation[] newArray(int size)
			{
				return new ForecastLocation[size];
			}
		};
        
		/**
         * Setup function to call the async task.
         * @author Travis Larson
         * @param viewFragment Fragment that the result will return to.
         */
        public void getForecastLocation(Fragment viewFragment)
        {
        	new LoadForecastLocation( this, (IListeners) viewFragment).execute(ZipCode);
        }
        
        public String getCity()
        {
        	return City;
        }
        
        public String getState()
        {
        	return State;
        }
        
        /**
         * Class extending AsyncTask to prevent locking the UI thread while
         * waiting for data retrieval.
         * @author Travis Larson
         * 
         */
        public static class LoadForecastLocation extends AsyncTask<String, Void, List<String>>
        {
                private static IListeners _listener;

                /**
                 * Set the listener so callback can be made.
                 * @author Travis Larson
                 * @param listener Fragment implementing the IListeners interface
                 */
                public LoadForecastLocation(ForecastLocation forecastLocation, IListeners listener)
                {
                        _listener = listener;
                }

                /**
                 * Async task that occurs on a seperate string to prevent the
                 * app from hanging. Retrieves and parses the json object.
                 * @author Travis Larson
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
                        	            while (jreader.hasNext()) 
                        	            {
                        	            	type = jreader.peek();
                        	            	switch(type)
                        	            	{
                        	            	case STRING:
                        	            		message.add(jreader.nextString());
                        	            		break;
                        	            	case NAME:
                        	            		message.add(jreader.nextName());
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
                        	            Log.e("Test", "got here");
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
                 * @author Travis Larson
                 */
                protected void onPostExecute(List<String> result)
                {	
                	if( result == null )
                	{
                		_listener.onLocationLoaded(null);
                		return;
                	}
                	
                	Iterator<String> iterator;
                	
                    //load values to forecast variables 
                	iterator = result.listIterator(result.indexOf("city")+1);
                    _instance.City = iterator.next();
                    
                    iterator = result.listIterator(result.indexOf("state")+1);
                    _instance.State = iterator.next();
                    
                    _listener.onLocationLoaded(_instance);
                }
        }
}