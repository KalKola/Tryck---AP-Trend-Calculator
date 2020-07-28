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

    // create our data arraylist.
    ArrayList<DataFormatter> dataList = new ArrayList<>();

    // method for reading in the specified files and saving the necessary data.
    public void readData(String fileName) {
        DateFormat dF = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");

        try {
            // create a buffered reader for reading in the files.
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            String[] wordArray;
            boolean check = true;

            // loop through the data files, skipping the header line, and saving the necessary info to our wordArray
            // and ArrayList.
            while ((line = br.readLine()) != null)
            {
                if (check) {
                    check = false;
                } else {
                    wordArray = line.split("\t");
                    DataFormatter entry = new DataFormatter();
                    entry.pressure = Float.parseFloat(wordArray[2]);
                    entry.humidity = Float.parseFloat(wordArray[4]);
                    entry.date = dF.parse(wordArray[0]);
                    dataList.add(entry);
                }
            }
            br.close();
        } catch (IOException | ParseException e) {
            // print error in case of IO exception or date parsing failure
            e.printStackTrace();
        }
    }

    // method for calculating the slope based on Barometric Pressure
    public String calculateSlope(String startDate, String endDate) {
        DateFormat dF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date parsedStartDate = null;
        Date parsedStopDate = null;
        try {
            // formatting the dates using a Simple Dat Formatter.
            parsedStartDate = dF.parse(startDate);
            parsedStopDate = dF.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = "From " + dF.format(parsedStartDate) + " to " +dF.format(parsedStopDate)
                + ".\n------------------------------------------------\n";

        DataFormatter y1 = null;
        DataFormatter y2 = null;
        int idx = 0, x1 = 0, x2 = 0;

        // loop through the ArrayList to gather data between start/stop dates.
        for (DataFormatter x: dataList) {
            if ((y1 == null) && x.date.compareTo(parsedStartDate) >= 0) {
                y1 = x;
                x1 = idx;
            }
            if (x.date.compareTo(parsedStopDate) >= 0) {
                x2 = idx;
                y2 = x;
                break;
            }
            idx++;
        }

        // calculate slope using y2 - y1 / x2 - x1 formula.
        float slope = (y2.pressure - y1.pressure) / (x2 - x1);

        //concatenate result into final string.
        result = result + " Barometric Pressure Slope: "
                + String.format("%.6f", slope) + ".";

        if (slope < 0) result = result + "Pressure is dropping.\n";
        if (slope == 0) result = result + "Pressure is remaining constant";
        if (slope > 0) result = result + "Pressure is rising.\n";

        return result;
    }

    // main method
    public static void main(String[] args) {

        // create new PressureSlope class
        PressureSlope slopeCalculator = new PressureSlope();

        // load data from .txt files using readData method
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

        // Scan user input for start:stop dates/time.
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

        // call calculateSlope method to print results to terminal.
        System.out.println(slopeCalculator.calculateSlope(startDate, endDate));

    }
}
