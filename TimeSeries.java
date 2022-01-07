package test;
import java.io.*;
import java.util.*;

public class TimeSeries {
	HashMap<String,Vector<Float>> timeTable = new HashMap<>();
	int numberOfColumns;
	int totalTimeSteps;

	public TimeSeries(String csvFileName){
		Scanner scanner;
		String fullLine = null;
		String[] headers = null;
		String[] numbers = null;
		try{
			scanner = new Scanner(new File(csvFileName));
			//reading the first line of the csv file -> seperating the columns and saving the names in a string array.
			//fullLine = scanner.next();
			headers = scanner.next().split(",");

			this.numberOfColumns = headers.length; //updating the number of columns according to the number of headlines...

			for(int i = 0; i < numberOfColumns; i++){
				Vector<Float> vec = new Vector<>();
				timeTable.put(headers[i],vec);
			} //iniziallize all the collumns of the maps with the name and empty vector.

			while(scanner.hasNext()){
				//fullLine = scanner.next();
				numbers = scanner.next().split(",");
				for(int i = 0; i < numberOfColumns; i++){
					timeTable.get(headers[i]).add(Float.parseFloat(numbers[i]));
				}
			}
			totalTimeSteps = timeTable.get("A").size();
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
// delete or not delete the fullLine?!