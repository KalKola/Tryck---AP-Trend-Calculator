import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PressureSlope {

    ArrayList<DataFormatter> collectedData = new ArrayList<>();

    public void readData(String fileName) {
        DateFormat dF = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            String[] wordArray;
            boolean check = true;

            while(true) {
                line = br.readLine();
                if (check) {
                    check = false;
                    continue;
                }
                else if (line == null) {
                    break;
                } else {
                    wordArray = line.split("\t");
                    DataFormatter entry = new DataFormatter();
                    entry.pressure = Float.valueOf(wordArray[2]);
                    entry.humidity = Float.valueOf(wordArray[4]);
                    entry.date = dF.parse(wordArray[0]);
                    collectedData.add(entry);
                }
            }
            br.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    public String calculateSlope(String startDate, String endDate) {
        DateFormat dF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = dF.parse(startDate);
            d2 = dF.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = "From " + dF.format(d1) + " to " +dF.format(d2) + ".";

        DataFormatter y1 = null;
        DataFormatter y2 = null;
        int idx = 0, x1 = 0, x2 = 0;

        for (DataFormatter x: collectedData) {
            if ((y1 == null) && x.date.compareTo(d1) >= 0) {
                y1 = x;
                x1 = idx;
            }
            if (x.date.compareTo(d2) >= 0) {
                x2 = idx;
                y2 = x;
                break;
            }
            idx++;
        }

        float slope = (y2.pressure - y1.pressure) / (x2 - x1);

        result = result + " The barometric pressure slope is "
                + String.format("%.6f", slope) + "\nthe forecast is: ";

        if (slope < 0) result = result + "inclement weather is closing in\n";
        if (slope == 0) result = result + "current conditions are likely to persist";
        if (slope > 0) result = result + "conditions are improving\n";

        return result;
    }

    public static void main(String[] args) {

    }
}
