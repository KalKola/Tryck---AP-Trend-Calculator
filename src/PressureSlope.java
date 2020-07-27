import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

    public static void main(String[] args) {

    }
}
