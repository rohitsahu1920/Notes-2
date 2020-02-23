package rohit.rohit.notes;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class notes_show extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE_ID = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    EditText body,title;
    FloatingActionButton floatingActionButton;
    Toolbar toolbar;
    Button save;
    ImageView image;
    DbHelper dbHelper;
    int image_len;
    String image_path;
    Bitmap image2;
    byte[] image1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_notes);

        body = findViewById(R.id.body);
        title = findViewById(R.id.title);
        floatingActionButton = findViewById(R.id.float_button);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        save = findViewById(R.id.save);
        image = findViewById(R.id.image);
        dbHelper = new DbHelper(this);

        setdata();

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title1 = title.getText().toString();
                String body1 = body.getText().toString().trim();
                image1 = imageViewToByte(image);
                if(!title1.isEmpty() && !body1.isEmpty())
                {
                    boolean ins = dbHelper.insert(title1,body1,image1);
                    if(ins)
                    {
                        Toast.makeText(getApplicationContext(),"Hurry Data Saved.....:)",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Title and Body Can not be empty",Toast.LENGTH_LONG).show();
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission())
                {
                    Intent id_button = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(id_button,RESULT_LOAD_IMAGE_ID);
                }
                else
                {
                    requestPermission();
                }
            }
        });

    }

    private byte[] imageViewToByte(ImageView image)
    {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,10,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE_ID && resultCode == RESULT_OK && data != null)
        {
            Uri selectedImage = data.getData();
            String[] path = {MediaStore.Images.Media.DATA};
            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage,path,null,null,null);
            assert cursor != null;
            cursor.moveToFirst();
            int columeIndex = cursor.getColumnIndex(path[0]);
            image_path = cursor.getString(columeIndex);
            cursor.close();
            image.setImageBitmap(BitmapFactory.decodeFile(image_path));
        }
    }

    private boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void requestPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(this, "We do not have Permission Please allow us in App Setting", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_list_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(),notes_list.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setdata() {
        notes_model Notes_model = (notes_model) getIntent().getExtras().getSerializable("notes");
        title.setText(Notes_model.getTitle());
        body.setText(Notes_model.getBody());
        image_len = Notes_model.getImage().length;
        image1 = Notes_model.getImage();
        image2 = BitmapFactory.decodeByteArray(image1, 0, image_len);
        image.setImageBitmap(image2);
    }
}
