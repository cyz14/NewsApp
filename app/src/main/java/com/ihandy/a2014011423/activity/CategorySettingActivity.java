package com.ihandy.a2014011423.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.ihandy.a2014011423.R;
import com.ihandy.a2014011423.adapter.RowAddAdapter;
import com.ihandy.a2014011423.adapter.RowDeleteAdapter;
import com.ihandy.a2014011423.content.CategoryReadWrite;
import com.ihandy.a2014011423.model.CategoryLabel;

import java.util.List;

public class CategorySettingActivity extends AppCompatActivity {
    public static final int ARROW_UP = 0;
    public static final int ARROW_DOWN = 1;
    private static List<CategoryLabel> watchedList;
    private static List<CategoryLabel> unwatchedList;
    private RowDeleteAdapter deleteAdapter;
    private RowAddAdapter addAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_setting);
//        String dataDir = getApplicationInfo().dataDir;
//        Log.d("Path", dataDir);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String label = bundle.getString("label");
                switch (msg.arg1) {
                    case ARROW_UP:
                        for (int i = 0; i < unwatchedList.size(); i++) {
                            if (unwatchedList.get(i).getLabel().equals(label)) {
                                watchedList.add(unwatchedList.get(i));
                                unwatchedList.remove(i);
                                addAdapter.notifyDataSetChanged();
                                deleteAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                        break;
                    case ARROW_DOWN:
                        for (int i = 0; i < watchedList.size(); i++) {
                            if (watchedList.get(i).getLabel().equals(label)) {
                                unwatchedList.add(watchedList.get(i));
                                watchedList.remove(i);
                                deleteAdapter.notifyDataSetChanged();
                                addAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        ListView watchedListView = (ListView) findViewById(R.id.watched_view);
        ListView unwatchedListView = (ListView) findViewById(R.id.unwatched_view);

        watchedList = CategoryReadWrite.getWatchedList();
        unwatchedList = CategoryReadWrite.getUnwatchedList();

        deleteAdapter = new RowDeleteAdapter(this, handler, watchedList);
        watchedListView.setAdapter(deleteAdapter);

        addAdapter = new RowAddAdapter(this, handler, unwatchedList);
        unwatchedListView.setAdapter(addAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CategoryReadWrite.writeLabelsToFile(CategoryReadWrite.watchFileName, watchedList);
        CategoryReadWrite.writeLabelsToFile(CategoryReadWrite.unwatchFileName, unwatchedList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        CategoryReadWrite.writeLabelsToFile(CategoryReadWrite.watchFileName, watchedList);
        CategoryReadWrite.writeLabelsToFile(CategoryReadWrite.unwatchFileName, unwatchedList);
        finish();
    }
}
