/**
 * Print out the names for which 100 or fewer babies were born in a chosen CSV file of baby name data.
 * 
 * @author Duke Software Team 
 */
import edu.duke.*;
import org.apache.commons.csv.*;

import java.io.File;

public class BabyBirths {
	public void printNames () {
		FileResource fr = new FileResource();
		for (CSVRecord record: fr.getCSVParser(false)) {
			System.out.println(
					"Name " + record.get(0) + " Gender " + record.get(1) + " Num Born " + record.get(2)
			);
		}
 	}

 	public void totalBirths(FileResource fr) {
		int count = 0;
		int boysCount = 0;
		int girlsCount = 0;

		for(CSVRecord record: fr.getCSVParser(false)) {
			int currentCount = Integer.parseInt(record.get(2));
			count += currentCount;

			if (record.get(1).equals("M")) {
				boysCount += currentCount;
			} else {
				girlsCount += currentCount;
			}
		}

		System.out.println("Total Birth was: " + count);
		System.out.println("Total Boys: " + boysCount);
		System.out.println("Total Girls: " + girlsCount);
	}

	public int getRank(int year, String name, String gender) {
		int rank = 1;
		String location = "us_babynames/us_babynames_test/yob" + year + "short.csv";
		FileResource resource = new FileResource(location);
		CSVParser parser = resource.getCSVParser(false);

		for(CSVRecord record: parser) {
			String currentName = record.get(0);
			String currentGender = record.get(1);

			if (name.equals(currentName) && gender.equals(currentGender))
				return rank;

			if (gender.equals(currentGender))
				rank++;
		}

		return -1;
	}

	public int getRankInFile(FileResource resource, String name, String gender) {
		int rank = 1;
		CSVParser parser = resource.getCSVParser(false);

		for(CSVRecord record: parser) {
			String currentName = record.get(0);
			String currentGender = record.get(1);

			if (name.equals(currentName) && gender.equals(currentGender))
				return rank;

			if (gender.equals(currentGender))
				rank++;
		}

		return -1;
	}

	public String getName(int year, int rank, String gender) {
		int currentRank = 1;
		FileResource resource = new FileResource("us_babynames/us_babynames_test/yob" + year + "short.csv");
		CSVParser parser = resource.getCSVParser(false);

		for (CSVRecord record: parser) {
			if (currentRank == rank)
				return record.get(0);

			String currentGender = record.get(1);
			if (gender.equals(currentGender))
				currentRank++;
		}

		return "NO NAME";
	}

	public void whatIsNameInYear(String name, int year, int newYear, String gender) {
		int currentRank = getRank(year, name, gender);
		String nameInOtherYear = getName(2014, 3, "F");
		System.out.print(name + " born in " + year + " would be " + nameInOtherYear + " if she was born in " + newYear + ".");
	}

	public int yearOfHighestRank(String name, String gender) {
		int rank = -1;
		String year = "";
		DirectoryResource dr = new DirectoryResource();

		for (File file: dr.selectedFiles()) {
			FileResource resource = new FileResource(file);
			int currentRank = this.getRankInFile(resource, name, gender);

			if (rank == -1) {
				rank = currentRank;
				year = file.getName();
			} else {
				if (currentRank != -1 && currentRank < rank) {
					rank = currentRank;
					year = file.getName();
				}
			}
		}

		return Integer.parseInt(year.substring(3, 7));
	}

	public double getAverageRank(String name, String gender) {
		double avg = -1.0;
		double sum = 0;
		int count = 0;
		DirectoryResource dr = new DirectoryResource();

		for(File file: dr.selectedFiles()) {
			FileResource resource = new FileResource(file);
			int currentRank = this.getRankInFile(resource, name, gender);

			if (currentRank != -1) {
				sum += currentRank;
			}

			count++;
		}

		if (sum != 0 && count != 0)
			avg = sum / count;

		return avg;
	}

	public int getTotalBirthsRankedHigher(int year, String name, String gender) {
		int count = 0;
		FileResource resource = new FileResource("us_babynames/us_babynames_test/yob" + year + "short.csv");
		CSVParser parser = resource.getCSVParser(false);

		// If the name passed is not in file, return 0
		int rankInFile = this.getRankInFile(resource, name, gender);
		if (rankInFile == -1) {
			return 0;
		}

		for(CSVRecord record: parser) {
			String currentName = record.get(0);

			if (currentName.equals(name))
				break;

			String currentGender = record.get(1);
			if (currentGender.equals(gender)) {
				int currentBirthCount = Integer.parseInt(record.get(2));
				count += currentBirthCount;
			}
		}

		return count;
	}

	public void testTotalBirths() {
		FileResource fr = new FileResource("data/us_babynames_test/example-small.csv");
		totalBirths(fr);
	}

	public static void main(String[] args) {
		BabyBirths births = new BabyBirths();
//		System.out.println(births.getRank(2012, "Mason", "M"));
//		System.out.println(births.getName(2012, 2, "M"));
//		births.whatIsNameInYear("Isabella", 2012, 2014, "F");
//			System.out.println(births.yearOfHighestRank("Mason", "M"));
//		System.out.println(births.getAverageRank("Mason", "M"));
//		System.out.println(births.getAverageRank("Jacob", "M"));
		System.out.println(births.getTotalBirthsRankedHigher(2012, "Ethan", "M"));
	}
}
