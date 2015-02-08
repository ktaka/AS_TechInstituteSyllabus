package jp.techinstitute.ti_055.syllabus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CourseItem item = (CourseItem)parent.getItemAtPosition(position);
        Intent intent = new Intent(this, CourseDetail.class);
        intent.putExtra("title", item.title);
        intent.putExtra("date", item.date);
        intent.putExtra("teacher", item.teacher);
        intent.putExtra("detail", item.detail);
        startActivity(intent);
    }

    private class CourseItem {
        String date;
        String title;
        String teacher;
        String detail;
    }
    private List<CourseItem> itemList;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = new ArrayList<CourseItem>();
        adapter = new ItemAdapter(getApplicationContext(), 0, itemList);
        ListView listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);
        setCourseData();
        listView.setOnItemClickListener(this);
    }

    private void setCourseData() {
        CourseItem item = new CourseItem();
        item.date = "8/28";
        item.title ="ユーティリティによる実践（1）";
        item.teacher ="高橋憲一";
        item.detail = "この講義では一つのアプリとして仕上げることを目指します。";
        itemList.add(item);

        item = new CourseItem();
        item.date = "9/2";
        item.title ="ユーティリティによる実践（2）";
        item.teacher ="高橋憲一";
        item.detail = "一つのアプリとして仕上げることを目指す２回目。";
        itemList.add(item);
    }

    private class ItemAdapter extends ArrayAdapter<CourseItem> {
        private LayoutInflater inflater;

        public ItemAdapter(Context context, int resource, List<CourseItem> objects) {
            super(context, resource, objects);
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.lecture_row, null, false);
            TextView dateView = (TextView)view.findViewById(R.id.date);
            TextView titleView = (TextView)view.findViewById(R.id.title);
            CourseItem item = getItem(position);
            dateView.setText(item.date);
            titleView.setText(item.title);
            return view;
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
