/*
File for study for use to create app that can import a .CSV from the /res
Also, for study to create an app that can import a .TXT from internal
storage for the app in data/android.<packageName>.<appName>/

App example for creating a user defined class to store CSV file date from /res/raw.
Shows how store app data in internal device storage for app.

Use Device Explorer and find:
data/data/com.packageName.projectName

Project reads and writes to io_file.txt
 */

package com.example.csv_to_hashmap;

import androidx.appcompat.app.AppCompatActivity;

//import android.media.MediaPlayer;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow;

//Maybe Used Imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

//Essential Imports
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //APPLICATION SPECIFIC INSTANCE VARIABLE DECLARATIONS

    //Declaring objects to store audio files
    MediaPlayer honkOne;
    MediaPlayer honkTwo;

    //Declaration and inititalization of String Array with two elements
    //Great for stubs to create a data source from a not yet connected subsystem
    static String[] strArr3 = {"hello", "there"};

    //Declaration of ArrayList of type Integer with initialCapacity of 5 elements
    //that are not initialized, but with memory reserved.
    //Also great for stubs to creat a data source from a not yet connected subsystem
    static ArrayList<Integer> intArrList = new ArrayList<Integer>(5);

    //Create for stub or testing to use in String[] tupArr
    //Strings in an array are much like what a BufferedReader gets
    String tuple1 = "line1-word1 line1-word2 line1-word3";
    String tuple2 = "line2-word1 line2-word2 line2-word3";

    //use for stub or testing to simulate a BufferedReader
    //Declaring and inititializing a String Array
    String[] tupArr = {tuple1, tuple2};

    //Storing a file name in string variable
    String MY_FILE_NAME = "io_file.txt";

    //Use for displaying rows within Table
    //Declaring two table row objects
    TableRow row1;
    TableRow row2;

    //TableRow[] array that holds the rows
    TableRow[] tableRows;

    //Declaring a TextView object
    TextView text1;

    //Use for capturing text from user to save to a file
    //Declaring a, EditView object
    EditText editText;

    //Declare HashMap for use to store Key-Value pairs of key to a ArrayList of data
    //HashMap cities_hm has Strings for keys and a separate ArrayList for each value.
    HashMap<String,ArrayList> cities_hm = new HashMap<String, ArrayList>();

    @Override
    //Notice we do not use: "throws IOException" for this method
    //that contains other method calls that do use "throws IOException"
    protected void onCreate(Bundle savedInstanceState) {
        //REQUAIRED ANDROID LIFECYCLE INITIALIZATION
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //APPLICATION-SPECIFIC INSTANCE VARIABLE ASSIGNMENT

        //The two sounds are assiged values from the res folder inside of raw
        honkOne = MediaPlayer.create(this, R.raw.honk_one);
        honkTwo = MediaPlayer.create(this, R.raw.honk_two);

        //The two row objects are assigned values by ID
        row1 = findViewById(R.id.row1);
        row2 = findViewById(R.id.row2);



        //Practice displaying rows using these below
        //The data transfer to rows must occur in method
        //Code here only assigns .XML objects to TableRow objects
        TableRow row3 = findViewById(R.id.row3);
        TableRow row4 = findViewById(R.id.row4);
        TableRow row5 = findViewById(R.id.row5);
        TableRow row6 = findViewById(R.id.row6);

        //The TableRow Array is now given a size and then each element
        //is initialized.
        tableRows = new TableRow[6];
        //The first two are declared as instance variables with class scope
        tableRows[0] = row1;
        tableRows[1] = row2;
        //These four below are declared local variables with method scope
        //Point was to show both are possible, depending on need
        tableRows[2] = row3;
        tableRows[3] = row4;
        tableRows[4] = row5;
        tableRows[5] = row6;

        //Assigning the EditText UML object to a EditText Java object
        editText = findViewById(R.id.editText);

        //BUTTON LISTENER SETUP

        Button button1 = findViewById(R.id.button1);
        //Button 1 click listener with anonynmous class
        button1.setOnClickListener(new View.OnClickListener()
                                   {   @Override
                                   public void onClick(View view)
                                   {
                                       //Plays sound in honkOne
                                       honkOne.start();

                                       //Need try/catch around method that has a throws
                                       try {
                                           readCityData();
                                           displayCityData();
                                       } catch (IOException e) {
                                           throw new RuntimeException(e);
                                       }
                                   }
                                   }
        );

        Button button2 = findViewById(R.id.button2);
        //Button 2 click listener with anonynmous class
        button2.setOnClickListener(new View.OnClickListener()
                                   {  @Override
                                   public void onClick(View view)
                                   {
                                       //Need try/catch around method that has a throws
                                       try {
                                           readInputFile();
                                       } catch (IOException e) {
                                           throw new RuntimeException(e);
                                       }
                                        //Plays sound in honkTwo
                                       honkTwo.start();
                                   }
                                   }
        );


        Button button3 = findViewById(R.id.button3);
        //Button 3 click listener with anonynmous class
        button3.setOnClickListener(new View.OnClickListener()
                                   {  @Override
                                   public void onClick(View view)
                                   {
                                       //Loads XML object by ID into Java object
                                       TextView textView = findViewById(R.id.textView1);
                                       //Gets string value in the editText object and
                                       //sets textView text to same value as editText
                                       textView.setText(editText.getText());

                                       //Need try/catch around method that has a throws
                                       try {
                                           writeOutputFile();
                                       } catch (IOException e) {
                                           throw new RuntimeException(e);
                                       }
                                   }
                                   }
        );
    }

    //Needs to use throws IOException because of try for in InputStream object
    private void readCityData() throws IOException {
        //Needs try/catch because always possible file is missing
        //Get CSV file in res/raw and store in InputStream object.
        //Rembmer, all Stream classes are one-time use, one way, either I or O
        try (InputStream stream = getResources().openRawResource(R.raw.cities_data))
        {
            //Log.i("XXX", "Stream works.");

            //Converts stream to UTF_8 and stores into BufferedReader object
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream, StandardCharsets.UTF_8)
            );

            //Using line counter to skip over header lines in .CSV
            int lineCounter = 0;
            String line;
            //Runs until readLine() method returns a null, happens at last line.
            while((line = reader.readLine()) != null)
            {   //tempData used to temporarily copy data in and out of
                //value of the key-value pair where the value is an
                //ArrayList type that holds multiple items of different data types
                ArrayList tempData;
                //Start from line 2 because first two lines are column label headers
                if (lineCounter >= 2) {
                    //Track each line in Logcat
                    Log.i("XXX", line);
                    //Split line by delimiter ',' into array of String
                    String[] words = line.split(",");
                    //Load all data to class object
                    //Create key and value with a blank ArrayList
                    //Skipped index 0 to avoid header of data types
                    //put() is the instance method to insert a new mapping
                    //into the HashMap. We are inserting a key-value pairing
                    //Here, 2nd element words[] is key, an empty arrayList is value
                    cities_hm.put(words[1],new ArrayList());
                    //Temporarily copy tempData for data transfers
                    tempData = cities_hm.get(words[1]);
                    //Used index 0 for the headers
                    tempData.add(words[0]);
                    //Skipped index 1 to avoid header of data labels
                    //These are string type and need no parsing
                    tempData.add(words[2]);
                    tempData.add(words[3]);
                    //These are number types and must be parsed from strings
                    tempData.add(Integer.parseInt(words[4]));
                    tempData.add(Double.parseDouble(words[5]));
                    tempData.add(Double.parseDouble(words[6]));
                    tempData.add(Integer.parseInt(words[7]));
                    tempData.add(Integer.parseInt(words[8]));

                    //Check content of each city object in Log
                    //get().toString takes the array of string and converts
                    //the who array back to a single string. The + concatenates
                    //the cityName which at words[1]. words[0] is the indexNum
                    Log.i("XXX", words[1] + cities_hm.get(words[1]).toString());
                }
                //Increment line counter to go to next line
                lineCounter++;
            }

        }catch(IOException exception)
        {
            exception.printStackTrace();
            String msg = exception.toString();
            Toast.makeText(getApplicationContext(),"Trouble with IO with input." + msg,
                    Toast.LENGTH_SHORT).show();
            Log.wtf("XXX", "Trouble with IO with input." + msg);
            //Use return if needed
            //return e.toString();
        }
    }

    boolean displayCityDataToggle = false;
    public void displayCityData()
    {
        if(!displayCityDataToggle) {
            //Use as temporary string holder
            String tempStr;
            //Use to pass TextView object into row
            TextView text;
            //Use to copy data in and out of ArrayList value of KV pair
            ArrayList cityData;

            int loopCounter = 0;
            for (String key : cities_hm.keySet()) {
                if (loopCounter < tableRows.length) {
                    cityData = cities_hm.get(key);
                    //Row 1 -------
                    //Row 1 data loading by column
                    //Row 1-Col 1 data loading: idNum, where idNum is an ordered int from key
                    text = new TextView(this);
                    String msg = cityData.toString();
                    tempStr = cityData.get(0).toString();
                    text.append(tempStr);
                    tableRows[loopCounter].addView(text);
                    //Copy all cityData items
                    //Row 1-Col 2 data loading: cityName
                    text = new TextView(this);
                    tempStr = key;
                    text.append(tempStr + " ");
                    tableRows[loopCounter].addView(text);
                    //Row 1-Col 3 data loading: country
                    text = new TextView(this);
                    tempStr = (String) cityData.get(1);
                    text.append(tempStr + " ");
                    tableRows[loopCounter].addView(text);
                    //Row 1-Col 4 data loading: continent
                    /*
                    text = new TextView(this);
                    tempStr = (String) cityData.get(2);
                    text.append(tempStr + " ");
                    tableRows[loopCounter].addView(text);
                    */
                    //Row 1-Col 5 data loading: population
                    text = new TextView(this);
                    tempStr = Integer.toString((Integer) cityData.get(3));
                    text.append(tempStr + " ");
                    tableRows[loopCounter].addView(text);
                    //Row 1-Col 6 data loading: Latitude
                    text = new TextView(this);
                    tempStr = Double.toString(Math.round((Double) cityData.get(4)));
                    text.append(tempStr + " ");
                    tableRows[loopCounter].addView(text);
                    //Row 1-Col 6 data loading: Longitude
                    text = new TextView(this);
                    tempStr = Double.toString(Math.round((Double) cityData.get(5)));
                    text.append(tempStr + " ");
                    tableRows[loopCounter].addView(text);

                    //Prevents more entries than XML rows
                    loopCounter++;
                }
            }
            displayCityDataToggle = true;
        }else
        {
            //Message to communicate data is already displayed
            Toast.makeText(getApplicationContext(),
                    "City data is alreadt displayed.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void readInputFile() throws IOException
    {
        MY_FILE_NAME = "io_file.txt";
        //Gets the internal path of the file
        File path = getApplicationContext().getFilesDir();
        //Converts the text file to a File object
        File readFrom = new File(path, MY_FILE_NAME);
        //Creates a byte[] array of the correct number of indices
        byte[] content = new byte[(int) readFrom.length()];

        try
        {
            //Creates a new file input stream using the file, readFrom
            FileInputStream fileis = new FileInputStream(readFrom);
            //Reads the fileis stream into the content byte[] array
            fileis.read(content);
            //Creates a string object from the byte[] array object
            String stringContent = new String(content);

            //Copy .XML TextView object to a Java TextView object
            text1 = findViewById(R.id.textView1);
            //Copy text content in string form to XML object
            text1.setText(stringContent);
            //Report to Toast
            Toast.makeText(getBaseContext(), stringContent,
                    Toast.LENGTH_LONG).show();

            //Use return if needed
            //return new String(content);

        } catch (Exception exception) {
            exception.printStackTrace();
            String msg = exception.toString();
            Toast.makeText(getApplicationContext(),"Trouble with IO with input." + msg,
                    Toast.LENGTH_SHORT).show();
            Log.wtf("XXX", "Trouble with IO with input." + msg);
            //Use return if needed
            //return e.toString();
        }
    }

    public void writeOutputFile() throws IOException
    {
        //Assign name of file to write
        MY_FILE_NAME = "io_file.txt";
        //Establish output path
        File path = getApplicationContext().getFilesDir();
        //Report to Toast the path
        Toast.makeText(getApplicationContext(),path.toString(), Toast.LENGTH_LONG).show();

        //Build continuous string with line breaks "\n" from Array
        StringBuilder output = new StringBuilder();
        String userInput = editText.getText().toString();
        //Append content of editText  object to output StringBuilder
        output.append(userInput);

        try
        {
            //Create a new file output file stream
            FileOutputStream writer = new FileOutputStream(new File(path,MY_FILE_NAME));
            //Write to file, but must do so by converting byte code
            writer.write(output.toString().getBytes());
            //Close file output file stream
            writer.close();
            //Report to Toast
            Toast.makeText(getApplicationContext(),"Wrote " +
                    MY_FILE_NAME, Toast.LENGTH_SHORT).show();
            //Report to Log
            Log.i("XXX", "Wrote " + MY_FILE_NAME);
        }catch(IOException exception)
        {
            //Report errors to Toast and Log
            exception.printStackTrace();
            String msg = exception.toString();
            Toast.makeText(getApplicationContext(),"Trouble with IO with output." + msg,
                    Toast.LENGTH_SHORT).show();
            Log.wtf("XXX", "Trouble with IO with output." + msg);
        }
    }
}