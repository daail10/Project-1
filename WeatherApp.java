import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.Scanner;
import org.json.simple.parser.JSONParser;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherApp {

    public static JSONObject getWeatherData(String locationName){

        JSONArray locationData = getLocationData(locationName);
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?"
        +"latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=auto";

        try{
            HttpURLConnection conn = fetchAPIResponse(urlString);

            if(conn.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
                return null;
            }

            StringBuilder resultJSON = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while(scanner.hasNext()){
                resultJSON.append(scanner.nextLine());
            }

            scanner.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultJSONobj = (JSONObject) parser.parse(String.valueOf(resultJSON));
            System.out.println(resultJSONobj.toJSONString());

            JSONObject hourlyData = (JSONObject) resultJSONobj.get("hourly");
            JSONArray time = (JSONArray) hourlyData.get("time");
            int index = findIndexOfCurrentTime(time);

            JSONArray temperatureData = (JSONArray) hourlyData.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            JSONArray weathercode = (JSONArray) hourlyData.get("weather_code");
            String weatherCondition = convertWeatherCode((long) weathercode.get(index));

            JSONArray relativeHumidity = (JSONArray) hourlyData.get("relative_humidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            JSONArray windspeedData = (JSONArray) hourlyData.get("wind_speed_10m");
            double windspeed = (double) windspeedData.get(index);

            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature",temperature);
            weatherData.put("weather_condition",weatherCondition);
            weatherData.put("humidity",humidity);
            weatherData.put("wind_speed",windspeed);

            return weatherData;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;

    }

    public static JSONArray getLocationData(String locationName){

        locationName = locationName.replaceAll(" ","+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        try{
            HttpURLConnection connection = fetchAPIResponse(urlString);

            if(connection.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
                return null;
            }
            else{
                StringBuilder resultJSON = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());

                while(scanner.hasNext()){
                    resultJSON.append(scanner.nextLine());
                }

                scanner.close();
                connection.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultJSONobj = (JSONObject) parser.parse(String.valueOf(resultJSON));
                JSONArray locationData = (JSONArray) resultJSONobj.get("results");
                return locationData;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private static HttpURLConnection fetchAPIResponse(String urlString) {

        HttpURLConnection conn = null;

        try{
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
        }catch(IOException e){
            e.printStackTrace();
        }

        return conn;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList){

        String currentTime = getCurrentTime();

        for(int i = 0;i < timeList.size();i++){
            String time = (String) timeList.get(i);
            if(time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }

        return 0;
    }

    public static String getCurrentTime(){

        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;
        
    }

    private static String convertWeatherCode(long weathercode){

        String weatherCondition = "";
        if(weathercode == 0L){
            weatherCondition = "Clear";
        }else if(weathercode <= 3L && weathercode > 0L){
            weatherCondition = "Cloudy";
        }else if((weathercode >= 51L && weathercode <= 67L ) || (weathercode >= 80L && weathercode <= 99L)){
            weatherCondition = "Rainy";
        }else if(weathercode >= 71L && weathercode <= 77L){
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }
}
