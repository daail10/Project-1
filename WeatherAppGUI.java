import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.Cursor;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import org.json.simple.JSONObject;

public class WeatherAppGUI extends JFrame{

        private JSONObject weatherData;

        public WeatherAppGUI(){
            super("Weather App");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(450,650);
            setLocationRelativeTo(null);
            setLayout(null);
            setResizable(false);

            addGUIComponents();

        }

        private void addGUIComponents(){

            JTextField searchTextField = new JTextField();
            searchTextField.setBounds(15,15,351,45);
            searchTextField.setFont(new Font("Times New Roman",Font.PLAIN,24));
            add(searchTextField);

            JLabel weatherconditionImage = new JLabel(loadImage("src/Assets/weatherapp_images/cloudy.png"));
            weatherconditionImage.setBounds(0,125,450,217);
            add(weatherconditionImage);

            JLabel temperatureText = new JLabel("10 C");
            temperatureText.setBounds(0,350,450,54);
            temperatureText.setFont(new Font("Times New Roman",Font.BOLD,48));
            temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
            add(temperatureText);

            JLabel weatherconditionDesc = new JLabel("Cloudy");
            weatherconditionDesc.setBounds(0,405,450,36);
            weatherconditionDesc.setFont(new Font("Times New Roman",Font.PLAIN,32));
            weatherconditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
            add(weatherconditionDesc);

            JLabel humidityImage = new JLabel(loadImage("src/Assets/weatherapp_images/humidity.png"));
            humidityImage.setBounds(15,500,74,66);
            add(humidityImage);

            JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
            humidityText.setBounds(90,500,85,55);
            humidityText.setFont(new Font("Times New Roman",Font.PLAIN,16));
            add(humidityText);

            JLabel windspeedImage = new JLabel(loadImage("src/Assets/weatherapp_images/windspeed.png"));
            windspeedImage.setBounds(220,500,74,66);
            add(windspeedImage);

            JLabel windspeedText = new JLabel("<html><b>Wind Speed</b> 15km/h</html>");
            windspeedText.setBounds(310,500,85,55);
            windspeedText.setFont(new Font("Times New Roman",Font.PLAIN,16));
            add(windspeedText);

            JButton searchButton = new JButton(loadImage("src/Assets/weatherapp_images/search.png"));
            searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            searchButton.setBounds(375,13,47,45);
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    String userInput = searchTextField.getText();

                    if(userInput.replaceAll("\\s","").length() <= 0){
                        return;
                    }

                    weatherData = WeatherApp.getWeatherData(userInput);
                    String weatherCondition = (String) weatherData.get("weather_condition");

                    switch(weatherCondition){
                        case "Clear":
                            weatherconditionImage.setIcon(loadImage("src/Assets/weatherapp_images/clear.png"));
                            break;
                        case "Rain":
                            weatherconditionImage.setIcon(loadImage("src/Assets/weatherapp_images/rain.png"));
                            break;
                        case "Cloudy":
                            weatherconditionImage.setIcon(loadImage("src/Assets/weatherapp_images/cloudy.png"));
                            break;
                        case "Snow":
                            weatherconditionImage.setIcon(loadImage("src/Assets/weatherapp_images/snow.png"));
                            break;
                    }

                    double temperature = (double) weatherData.get("temperature");
                    temperatureText.setText(temperature + " C");

                    weatherconditionDesc.setText(weatherCondition);

                    long humidity = (long) weatherData.get("humidity");
                    humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                    double windspeed = (double) weatherData.get("wind_speed");
                    windspeedText.setText("<html><b>Wind Speed</b> " + windspeed + "km/h</html>");

                }
            });
            add(searchButton);


        }

        private ImageIcon loadImage(String ResourcePath){

            try{
                BufferedImage image = ImageIO.read(new File(ResourcePath));
                return new ImageIcon(image);
            }catch(IOException e){
                e.printStackTrace();
            }

            System.out.println("Could not find resource");
            return null;

        }





}
