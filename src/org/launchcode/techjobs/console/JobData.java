package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();
        //Create an ArrayList named values

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);
            //Use the search term to find the HashMap key with that name and put it's value in the aValue variable

            if (!values.contains(aValue)) {
                values.add(aValue);
                //If the value doesn't exist in the values ArrayList, add to it.
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {
        //Takes in a column string as an argument to search and takes in a value string as an argument to search

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        //Declares a HashMap named jobs

        for (HashMap<String, String> row : allJobs) {
            //For each HashMap in "allJobs"...

            String aValue = row.get(column);
            //Search the key names of each HashMap in "allJobs" using the String column argument and add the corresponding value to "aValue"

            if (aValue.toLowerCase().contains(value.toLowerCase())) {
                jobs.add(row);
                //If the value in aValue matches the String argument value then add the key name and value to the HashMap jobs
            }
        }

        return jobs;
    }

    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        loadData();

        ArrayList<HashMap<String, String>> coulombList = new ArrayList<>();

        for (HashMap<String, String> rows : allJobs) {

            for (String row : rows.values()) {

                if (row.toLowerCase().contains(value.toLowerCase()) && !coulombList.contains(rows)) {
                    coulombList.add(rows);
                }

            }

        }

        return coulombList;

    }


    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
