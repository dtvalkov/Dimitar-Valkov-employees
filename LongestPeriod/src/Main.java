import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Main {

	static List<Project> database = new ArrayList<Project>(); // contains all the project info from the text file

	public static void main(String[] args) throws ParseException {

		
		readFile("data.txt");
		List<Triplet<Employee, Employee, Integer>> pairs = getPairs();
		pairs = CombinePairs(pairs);
		getResults(pairs);

	}

	private static void readFile(String path) throws ParseException {
		// function to read the data from the txt file

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(path));

			String line = new String();

			while (line != null) {

				boolean flag = false;
				Project b = new Project();

				line = reader.readLine();

				if (line != null) {

					String[] values = line.split(",");//splitting the line into words

					Employee a = new Employee();
					a.setID(Integer.parseInt(values[0].trim()));

					b.setPrId(Integer.parseInt(values[1].trim()));

					String date1 = values[2].trim();
					String date2 = values[3].trim();

					if (date2.equalsIgnoreCase("NULL"))
						date2 = LocalDate.now().format(formatter);

					for (Project pr : database)
						if (pr.getPrId() == b.getPrId()) {// find if project# exists in DB already
							pr.addElement(new Triplet<Employee, String, String>(a, date1, date2));
							flag = true;
						}

					if (!flag) {
						b.addElement(new Triplet<Employee, String, String>(a, date1, date2));
						database.add(b);
					}

				}

			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	} // end readFile

	private static int getDays(LocalDate date1, LocalDate date2)// function to calculate and return the days between 2
																// dates
	{
		// calculating number of days in between
		int noOfDaysBetween = (int) ChronoUnit.DAYS.between(date1, date2);

		return noOfDaysBetween;
	}

	private static List<Triplet<Employee, Employee, Integer>> getPairs() {
		List<Triplet<Employee, Employee, Integer>> pairs = new ArrayList<>();
		
		/* function to create a collection of Triplet objects
		 * which are an Employee-Employee pair + the days they've worked together
		 */
		for (Project pr : database) {

			List<Triplet<Employee, String, String>> list = pr.getInfo();

			for (int i = 0; i < list.size(); i++)
				for (int j = i + 1; j < list.size(); j++) {
					
					LocalDate date1 = LocalDate.parse(list.get(i).y);
					LocalDate date2 = LocalDate.parse(list.get(j).y);
					LocalDate firstDayTogether;
					LocalDate lastDayTogether;

					if (date1.compareTo(date2) > 0)
						firstDayTogether = date1;

					else
						firstDayTogether = date2;

					date1 = LocalDate.parse(list.get(i).z);
					date2 = LocalDate.parse(list.get(j).z);

					if (date1.compareTo(date2) > 0)
						lastDayTogether = date2;

					else
						lastDayTogether = date1;

					int daysTogether = getDays(firstDayTogether, lastDayTogether);

					if (daysTogether > 0)
						pairs.add(new Triplet<Employee, Employee, Integer>(list.get(i).x, list.get(j).x, daysTogether));

				}

		}

		return pairs;

	}// end getPairs

	private static List<Triplet<Employee, Employee, Integer>> CombinePairs(
			List<Triplet<Employee, Employee, Integer>> list) {

		/*
		 * function to combine the data if 2 employees have worked together on 2 or more
		 * different projects
		 */

		for (int i = 0; i < list.size(); i++)
			for (int j = i + 1; j < list.size(); j++) {
				Employee emp1 = list.get(i).x;
				Employee emp2 = list.get(i).y;

				Employee nextEmp1 = list.get(j).x;
				Employee nextEmp2 = list.get(j).y;

				if (emp1.getID() == nextEmp1.getID() || emp1.getID() == nextEmp2.getID())
					if (emp2.getID() == nextEmp2.getID() || emp2.getID() == nextEmp1.getID()) { 
						list.get(i).z += list.get(j).z;

						list.remove(j); //removing the objects that aren't needed anymore
						j--;
					}

			}

		return list;

	} // end CombinePairs

	private static void getResults(List<Triplet<Employee, Employee, Integer>> list) {
		// function to print the result - which pair has worked together the most

		int max = 0;
		Triplet<Employee, Employee, Integer> result = new Triplet();

		for (Triplet<?, ?, ?> tr : list) {
			int days = (int) tr.z;
			if (days > max) {
				max = days;
				result = (Triplet<Employee, Employee, Integer>) tr;
			}
		}

		System.out.println("Employee# " + result.x.getID() + " and Employee# " + result.y.getID()
				+ " have worked together " + (int) result.z + " days in total!");

	}

} // end class Main
