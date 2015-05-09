package jp.techinstitute.ti_055.syllabus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CourseItem item = (CourseItem)parent.getItemAtPosition(position);
        Intent intent = new Intent(this, CourseDetail.class);
        intent.putExtra("title", item.title);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        intent.putExtra("date", dateFormat.format(item.date));
        intent.putExtra("teacher", item.teacher);
        intent.putExtra("detail", item.detail);
        startActivity(intent);
    }

    private class CourseItem {
        Date date;
        String title;
        String teacher;
        String detail;
    }
    private List<CourseItem> itemList;
    private ItemAdapter adapter;
    private ProgressBar progressBar;

    private RequestQueue reqQueue;
    private static final String syllabusUrl = "https://script.google.com/macros/s/AKfycbyibdC1ZESlWsIYRIL4XFLfx7qRVzruWMHI6YFJ4qlUam4-p-Q3/exec?room=tokyo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = new ArrayList<CourseItem>();
        adapter = new ItemAdapter(getApplicationContext(), 0, itemList);
        ListView listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);
        progressBar = (ProgressBar)findViewById(R.id.pregressBar1);
        reqQueue = Volley.newRequestQueue(this);
        getCourseData();
        listView.setOnItemClickListener(this);
    }

    private void getCourseData() {
        progressBar.setVisibility(View.VISIBLE);
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("course");
                    setCourseArray(array);
                    progressBar.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onResponse", "error=" + error);
            }
        };

        JsonObjectRequest jsonReq = new JsonObjectRequest(syllabusUrl, null, listener, errorListener);
        reqQueue.add(jsonReq);
    }

    private void setCourseArray(JSONArray array) throws JSONException {
        int num = array.length();
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < num; i++)  {
            CourseItem item = new CourseItem();
            JSONObject obj = array.getJSONObject(i);
            String dateStr = obj.getString("date");
            Date date = null;
            try {
                date = inputDateFormat.parse(dateStr);
                item.date = date;
                item.title = obj.getString("title");
                item.teacher = obj.getString("teacher");
                item.detail = obj.getString("detail");
                itemList.add(item);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    private class ItemAdapter extends ArrayAdapter<CourseItem> {
        private LayoutInflater inflater;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");

        public ItemAdapter(Context context, int resource, List<CourseItem> objects) {
            super(context, resource, objects);
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        private class ViewHolder {
            TextView date;
            TextView title;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.lecture_row, null, false);
                holder = new ViewHolder();
                holder.date = (TextView)convertView.findViewById(R.id.date);
                holder.title = (TextView)convertView.findViewById(R.id.title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            CourseItem item = getItem(position);
            holder.date.setText(dateFormat.format(item.date));
            holder.title.setText(item.title);
            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
