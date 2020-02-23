package rohit.rohit.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

public class notes_list extends AppCompatActivity {

    List<notes_model> list;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ListView notes;
    DbHelper dbHelper;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        notes = findViewById(R.id.listview);
        dbHelper = new DbHelper(this);
        floatingActionButton = findViewById(R.id.flow_button);


        retrive();

        notes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                notes_model notes_model = list.get(i);
                final String title = notes_model.getTitle();
                final String body = notes_model.getBody();

                builder.setMessage("Note:- Data will be permanently delete..!")
                        .setCancelable(true)
                        .setTitle("Do you really want to Delete ?")
                        .setIcon(R.drawable.ic_warning)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Boolean delete = dbHelper.delete(title,body);
                                if(delete == true)
                                {
                                    Toast.makeText(getApplicationContext(),"Record Deleted Successfully",Toast.LENGTH_LONG).show();
                                    retrive();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Problem in deleting Record",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alert11 = builder.create();
                alert11.show();
                return false;
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),body_of_notes.class));
            }
        });

        notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                notes_model Notes_model = list.get(i);
                Intent intent = new Intent(getApplicationContext(),notes_show.class);
                intent.putExtra("notes",Notes_model);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),notes_list.class));
                        break;

                    case R.id.setting:
                        startActivity(new Intent(getApplicationContext(),setting.class));
                        Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_LONG).show();
                        break;

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(),app_info.class));
                        break;

                    case R.id.sync:
                        retrive();
                        Toast.makeText(getApplicationContext(),"Sync Successfully",Toast.LENGTH_LONG).show();
                        break;

                    case R.id.share:
                        share();
                        break;

                    case R.id.contact:
                        startActivity(new Intent(getApplicationContext(),report_bug.class));
                        break;

                    case R.id.stats:
                        startActivity(new Intent(getApplicationContext(),statistics.class));
                        break;
                }

                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_list_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;

            case R.id.delete:
                delete_all();
                Toast.makeText(getApplicationContext(), "Delete Function Will work", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void retrive()
    {
        list = dbHelper.getdetails();
        ArrayAdapter<notes_model> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        notes.setAdapter(arrayAdapter);
    }

    public void share()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Notes");
        intent.putExtra(Intent.EXTRA_TEXT,"link will be available soon");
        startActivity(Intent.createChooser(intent,"Share Application Link"));
    }

    public void delete_all()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Note:- Data will be permanently delete all DATA...!")
                .setCancelable(true)
                .setTitle("Do you really want to Delete ?")
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean delete = dbHelper.delete_all();
                        if(delete)
                        {
                            Toast.makeText(getApplicationContext(),"Record Deleted Successfully",Toast.LENGTH_LONG).show();
                            retrive();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Problem in deleting Record",Toast.LENGTH_LONG).show();
                        }
                    }
                });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }
}
