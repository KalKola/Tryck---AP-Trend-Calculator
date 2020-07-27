import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

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
                    entry.pressure = Float.parseFloat(wordArray[2]);
                    entry.humidity = Float.parseFloat(wordArray[4]);
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

        PressureSlope slopeCalculator = new PressureSlope();
        System.out.println("Loading data...");
        slopeCalculator.readData("data/Environmental_Data_Deep_Moor_2012.txt");
        slopeCalculator.readData("data/Environmental_Data_Deep_Moor_2013.txt");
        slopeCalculator.readData("data/Environmental_Data_Deep_Moor_2014.txt");
        slopeCalculator.readData("data/Environmental_Data_Deep_Moor_2015.txt");
        System.out.println("Data successfully loaded.");

        /* test cases
        String startDate = "2012/01/01 00:30:00";
        String endDate = "2012/01/01 02:30:00";
        System.out.println(slopeCalculator.calculateSlope(startDate, endDate));

        startDate = "2012/01/01 00:30:00";
        endDate = "2013/01/01 02:30:00";
        System.out.println(slopeCalculator.calculateSlope(startDate, endDate));
        */

        Scanner sc = new Scanner(System.in);
        System.out.println("The following program uses data collected in Lake Pend Oreille, Idaho from between 2012 and 2015.");
        System.out.println("When entering a date please use the following format: YYYY/MM/DD and HH:mm:ss");
        System.out.print("Enter a start date: ");
        String startDate = sc.next();
        System.out.print("Enter a start time: ");
        startDate = startDate + " " + sc.next();

        System.out.print("Enter an end date: ");
        String endDate = sc.next();
        System.out.print("Enter an end time: ");
        endDate = endDate + " " + sc.next();
        System.out.println(slopeCalculator.calculateSlope(startDate, endDate));

    }
}
