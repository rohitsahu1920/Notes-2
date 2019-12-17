package sahu.rohit.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class notes_list extends AppCompatActivity {

    List<notes_model> list;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ListView notes;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        notes = findViewById(R.id.listview);
        dbHelper = new DbHelper(this);


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
}
