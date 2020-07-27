import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFormatter {
    Date date;
    float pressure;
    float humidity;

    public String toString() {
        DateFormat dF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        return ("Date: " + dF.format(date)
                + " Barometric Pressure: " + Float.toString(pressure)
                + " Humidity: " + Float.toString(humidity));
    }
}
