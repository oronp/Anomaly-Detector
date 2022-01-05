package test;
import java.io.*;
import java.util.*;

public class TimeSeries {
	HashMap<String,Vector<Float>> timeTable = new HashMap<>();
	int numberOfColumns;

	public TimeSeries(String csvFileName){
		Scanner scanner;
		String fullLine = null;
		String[] headers = null;
		String[] numbers = null;
		try{
			scanner = new Scanner(new File(csvFileName));
			//reading the first line of the csv file -> seperating the columns and saving the names in a string array.
			fullLine = scanner.next();
			headers = fullLine.split(",");
			this.numberOfColumns = headers.length; //updating the number of columns according to the number of headlines...

			for(int i = 0; i < numberOfColumns; i++){
				Vector<Float> vec = new Vector<>();
				timeTable.put(headers[i],vec);
			} //iniziallize all the collumns of the maps with the name and empty vector.

			while(scanner.hasNext()){
				fullLine = scanner.next();
				numbers = fullLine.split(",");
				for(int i = 0; i < numberOfColumns; i++){
					timeTable.get(headers[i]).add(Float.parseFloat(numbers[i]));
				}
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}