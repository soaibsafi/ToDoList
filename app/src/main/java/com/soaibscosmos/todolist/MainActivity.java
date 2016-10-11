package com.soaibscosmos.todolist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.soaibscosmos.todolist.db.TaskContract;
import com.soaibscosmos.todolist.db.TaskDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView)findViewById(R.id.list_todo);
/**
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
                int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
        }
        cursor.close();
        db.close();
 **/
        updateUI();
    }

    @Override
    //inflate menu resource (defined in XML) into the Menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //This method passes the selected MenuItem thant can identified by grtItemId() method
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_add_task:
                addTask();
                return true;
            case R.id.action_info:
                contact();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addTask(){
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Add A Task?")
                .setMessage("What do you want to do next?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        SQLiteDatabase db = mHelper.getWritableDatabase();    //Create and/or open a database that will be used for reading and writing.
                        ContentValues values = new ContentValues();     //Creates an empty set of values using the default initial size
                        values.put(TaskContract.TaskEntry.COL_TASK_TITLE,task); //put the text that get from dialog box in the DB
                        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        updateUI();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void contact(){
        final TextView contactTextView = new TextView(this);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("SOAIB")
                .setMessage("American International University - Bangladesh (AIUB)\n BSc.CSE")
                .setView(contactTextView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }

    private void updateUI(){
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();  //Create and/or open a database.
        //'Cursor' interface provides random read-write access to the result set returned by a database query
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while(cursor.moveToNext()) {    //Move the cursor to the next row.
            //getColumnIndex() Returns the zero-based index for the given column name, or -1 if the column doesn't exist.
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));    //getString() Returns the value of the requested column as a String.
        }

        if (mAdapter == null){
                mAdapter = new ArrayAdapter<>(this,
                        R.layout.item_todo,     // what view to use for the items
                        R.id.task_title,        // where to put the String of data
                        taskList);              // where to get all the data
                mTaskListView.setAdapter(mAdapter);     // set it as the adapter of the ListView instance
            }else{
                mAdapter.clear();   //Remove all elements from the list.
                mAdapter.addAll(taskList);  //Adds the specified items at the end of the array.
                mAdapter.notifyDataSetChanged();    /*Notifies the attached observers that the underlying
                                                    * data has been changed and any View reflecting the
                                                    * data set should refresh itself. */
            }
        cursor.close();
        db.close();
    }

    public void deleteTask(View view){
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " =? ",
                new String[]{task});
        db.close();
        updateUI();
    }
}
