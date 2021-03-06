package com.example.conactprovidersharingcontacts;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    Button btnview;
    TextView txtname,txtphno;
    static final int PICK_CONTACT = 1;
    String st;
    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AccessContact();
        btnview = (Button) findViewById(R.id.btnload);
        txtname=(TextView) findViewById(R.id.txtname);
        txtphno=(TextView) findViewById(R.id.txtphno);




    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void AccessContact()

    {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {

                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_MULTIPLE_PERMISSIONS);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
                    if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                                 permissionsList.add(permission);

                        if (shouldShowRequestPermissionRationale(permission)) {
                            return true;
                        }
                        return false;
                    }
           return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
         new AlertDialog.Builder(MainActivity.this)
              .setMessage(message)
              .setPositiveButton("OK", okListener)
              .setNegativeButton("Cancel", null)
              .create()
              .show();
    }


    public void conttact(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }


    public void onActivityResult(int reqCode, int resultCode, Intent data) {
 super.onActivityResult(reqCode, resultCode, data);

 switch (reqCode) {
 case (PICK_CONTACT):
 if (resultCode == Activity.RESULT_OK) {

Uri contactData = data.getData();
Cursor c = managedQuery(contactData, null, null, null, null);
 if (c.moveToFirst()) {

                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        try {
if (hasPhone.equalsIgnoreCase("1")) {
Cursor phones = getContentResolver().query(
                                         ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                         null, null);
 phones.moveToFirst();
 String cNumber = phones.getString(phones.getColumnIndex("data1"));
 System.out.println("number is:" + cNumber);
 txtphno.setText("Phone Number is: "+cNumber); }
 String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
 txtname.setText("Name is: "+name);

                        }
                        catch (Exception ex)
                        {

                        }
                    }
          }
       break;
 }
}




}