package com.example.xmlparserexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set the user interface
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            ArrayList<HashMap<String, String>> userList = new ArrayList<>();
            HashMap<String, String> user = new HashMap<>();

            ListView lv = (ListView) findViewById(R.id.user_list);

            // Assign input stream to the xml file in assets folder
            InputStream istream = getAssets().open("userdetails.xml");

            // Create instance of XMLPullParserFactory and XMLPullParser
            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            // Set parser to input Stream
            parser.setInput(istream, null);

            String tag = "", text = "";
            int event = parser.getEventType();

            // Continue until the end of the document is reached.
            while (event != XmlPullParser.END_DOCUMENT) {

                // get tag
                tag = parser.getName();
                if (tag != null) Log.v("myLog", String.format("eventtype %d", event) + " : " + tag );

                switch (event) {
                    case XmlPullParser.START_TAG:
                        // create new user
                        if (tag.equals("user"))
                            user = new HashMap<>();
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        // pick up text - against - name or location or designation
                        if (text != null) Log.v("myLog", text);
                        break;

                    case XmlPullParser.END_TAG:
                        switch (tag) {
                            case "name":
                                user.put("name", text);
                                break;
                            case "designation":
                                user.put("designation", text);
                                break;
                            case "location":
                                user.put("location", text);
                                break;
                            case "user":
                                if (user != null)
                                    userList.add(user); // add user to userList
                                break;
                        }
                        break;
                }
                // advance the parser to the next event.
                event = parser.next();
            }

            ListAdapter adapter = new SimpleAdapter(MainActivity.this, // context
                    userList,  // data - list of maps, each entry in list corresponds to one row
                    R.layout.list_row, // resource id
                    new String[]{"name", "designation", "location"}, // from - list of column names
                    new int[]{R.id.name, R.id.designation, R.id.location}); // to - id's of the view
            lv.setAdapter(adapter);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
