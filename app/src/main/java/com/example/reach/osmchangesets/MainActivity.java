package com.example.reach.osmchangesets;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    OSMTask downloadTask;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTask();

    }

    public void startTask() {
        if (downloadTask != null) {
            downloadTask.cancel(true);
        } else {
            downloadTask = new OSMTask();
            downloadTask.execute();
        }
    }

    public class OSMTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... voids) {
            String id = getIntent().getExtras().getString("osmid");
            String downloadUrl = "http://api.openstreetmap.org/api/0.6/changesets?display_name=" + id;
            ArrayList<HashMap<String, String>> results = new ArrayList<>();
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                results = processXML(inputStream);
            } catch (Exception e) {
                L.m(e + "");
            }
            return results;
        }

        @Override
        protected void onPostExecute(final ArrayList<HashMap<String, String>> result) {
            super.onPostExecute(result);


            listView = findViewById(R.id.lv);

            listView.setAdapter(new MyAdapter(getApplicationContext(), result));


        }

        public ArrayList<HashMap<String, String>> processXML(InputStream inputStream) throws Exception {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.parse(inputStream);
            Element rootElement = xmlDocument.getDocumentElement();
            L.m("" + rootElement.getTagName());
            NodeList itemList = rootElement.getElementsByTagName("changeset");
            Node currentItem = null;
            ArrayList<HashMap<String, String>> results = new ArrayList<>();
            HashMap<String, String> currentMap = null;
            for (int i = 0; i < itemList.getLength(); i++) {
                currentItem = itemList.item(i);
                currentMap = new HashMap<>();
                //String s=currentItem.getAttributes().getNamedItem("id").getTextContent();
                currentMap.put("id", currentItem.getAttributes().getNamedItem("id").getTextContent());
                //L.m(""+s);
                //String s1=currentItem.getAttributes().getNamedItem("closed_at").getTextContent();
                currentMap.put("closed_at", currentItem.getAttributes().getNamedItem("closed_at").getTextContent());
                // L.m(""+s1);
                if (currentMap != null && !currentMap.isEmpty()) {
                    results.add(currentMap);
                }
            }
            return results;
        }


    }

}

class MyAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> dataSource = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;

    public MyAdapter(Context context, ArrayList<HashMap<String, String>> dataSource) {
        this.dataSource = dataSource;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int i) {
        return dataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View row = view;
        MyHolder holder = null;

        if (row == null) {

            row = layoutInflater.inflate(R.layout.custom_row, viewGroup, false);
            holder = new MyHolder(row);
            row.setTag(holder);
        } else {

            holder = (MyHolder) row.getTag();
        }
        final HashMap<String, String> currentItem = dataSource.get(i);
        final String id = currentItem.get("id");
        final String cl = currentItem.get("closed_at");
        final String osmChaURL = "https://osmcha.mapbox.com/changesets/";
        final String osmAchURL = "https://overpass-api.de/achavi/?changeset=";
        holder.idTextView.setText(id);
        holder.closed_at.setText(cl);

        holder.osmCha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Web.class);
                intent.putExtra("URL", osmChaURL + id);
                context.startActivity(intent);
            }
        });
        holder.osmAchavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context, Web2.class);
                intent1.putExtra("URL", osmAchURL + id);
                context.startActivity(intent1);
            }
        });
           /* osmCha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });*/
             /*   osmCha.setOnClickListener(new View.OnClickListener() {
            @Overridedgdfg
            public void onClick(View view) {

            }
        });*/
        return row;

    }
}

class MyHolder {

    TextView idTextView, closed_at;
    Button osmCha, osmAchavi;

    public MyHolder(View view) {
        idTextView = view.findViewById(R.id.idTextView);
        closed_at = view.findViewById(R.id.clTextView);
        osmCha = (Button) view.findViewById(R.id.OSMCha);
        osmAchavi = view.findViewById(R.id.OSM);

    }


}

