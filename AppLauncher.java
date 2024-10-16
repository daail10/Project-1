import javax.swing.*;

public class AppLauncher {

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run(){
               new WeatherAppGUI().setVisible(true);

            //System.out.println(WeatherApp.getLocationData("Paris"));
            //    System.out.println(WeatherApp.getCurrentTime());
            //    System.out.println(WeatherApp.getWeatherData("Paris"));
            }
        });


    }
}
