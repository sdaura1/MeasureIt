package com.example.shaheed.mtesting;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListData extends AppCompatActivity {

    ListView dataListView;
    ArrayAdapter arrayAdapter;
    SQLiteDatabase databaseRead;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    DocumentReference documentReference;
    GoogleSignInOptions googleSignInOptions;
    ArrayList<com.google.firebase.firestore.DocumentSnapshot> arrayList;
    private static final String TAG = "DebugMessage";
    SQLiteHelper dbHelper;
    public Users[] mUsers;
    SQLiteHelper sqLiteHelper;
    FirebaseFirestore dbFirestore;
    private static final int REQUEST_INTERNET = 1;
    ArrayList<String> values;
    Cursor cursor;
    ContentValues contentValues;
    ArrayList<String> databaseNameArrayList;
    FloatingActionButton myFab;
    Map<String, String> firestoredata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        firebaseAuth = FirebaseAuth.getInstance();
        dbHelper = new SQLiteHelper(this);
        databaseRead = dbHelper.getReadableDatabase();
        dataListView = findViewById(R.id.dataListView);
        myFab = findViewById(R.id.fab);
        values = new ArrayList<>();
        sqLiteHelper = new SQLiteHelper(this);
        databaseNameArrayList = new ArrayList<>();
        contentValues = new ContentValues();
        dbFirestore = FirebaseFirestore.getInstance();
        documentReference = FirebaseFirestore.getInstance().document("");
        firestoredata = new HashMap<>();
        cursor = databaseRead.rawQuery("SELECT * FROM " + SQLiteHelper.TB_NAME,
                null);

        getValues();
        getFirestoreData();

        if (ActivityCompat.checkSelfPermission(ListData.this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ListData.this,
                    new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);
        }

        googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1082465148584-1bph26dnqql3vicmcsf63em82v5eho4c.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String mName = (String) dataListView.getItemAtPosition(position);
                int idName = (int) dataListView.getItemIdAtPosition(position);

                Intent mIntent = new Intent(ListData.this, DetailedActivity.class);
                mIntent.putExtra("name", mName);
                mIntent.putExtra("id", (idName + 1));
                startActivity(mIntent);
            }
        });

        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(ListData.this, MainActivity.class);
                startActivity(mIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                updateData(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }

            /*
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()){
                GoogleSignInAccount gso = result.getSignInAccount();

                if (cursor.moveToFirst()) {
                    do {
                        firestoredata.put("Name", cursor.getString(cursor.getColumnIndex("name")));
                        firestoredata.put("Shirt", cursor.getString(cursor.getColumnIndex("shirt")));
                        firestoredata.put("Shoulder", cursor.getString(cursor.getColumnIndex("shoulder")));
                        firestoredata.put("Trouser", cursor.getString(cursor.getColumnIndex("trouser")));
                        firestoredata.put("Hand", cursor.getString(cursor.getColumnIndex("hand")));
                        firestoredata.put("Phone_number", cursor.getString(cursor.getColumnIndex("phone_number")));


                    dbFirestore.collection(gso.getDisplayName())
                            .document(cursor.getString(cursor.getColumnIndex("name")))
                            .set(firestoredata);
                    } while (cursor.moveToNext());
                }
            }
            */
        }
    }

    public ArrayList<String> getValues() {

        if(cursor.moveToFirst()) {
            do {
                values.add(cursor.getString(cursor.getColumnIndex("name")));
            }while(cursor.moveToNext());
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, values);
        dataListView.setAdapter(arrayAdapter);
        dbHelper.close();
        return values;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sync:
                Intent signIn = googleSignInClient.getSignInIntent();
                startActivityForResult(signIn, RC_SIGN_IN);

            case R.id.getData:
                getFirestoreData();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        updateData(signInAccount);
    }

    private void updateData(@Nullable GoogleSignInAccount account) {
        if (account != null) {

            if (cursor.moveToFirst()) {

                do {
                    firestoredata.put("Name", cursor.getString(cursor.getColumnIndex("name")));
                    firestoredata.put("Shirt", cursor.getString(cursor.getColumnIndex("shirt")));
                    firestoredata.put("Shoulder", cursor.getString(cursor.getColumnIndex("shoulder")));
                    firestoredata.put("Trouser", cursor.getString(cursor.getColumnIndex("trouser")));
                    firestoredata.put("Hand", cursor.getString(cursor.getColumnIndex("hand")));
                    firestoredata.put("Phone_number", cursor.getString(cursor.getColumnIndex("phone_number")));


                    dbFirestore.collection(account.getDisplayName())
                            .document(cursor.getString(cursor.getColumnIndex("name")))
                            .set(firestoredata);

                } while (cursor.moveToNext());

                Toast.makeText(this, "Data Synced!", Toast.LENGTH_LONG).show();

            }
        }
    }


    public void getFirestoreData(){

        final GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        dbFirestore.collection("Shahid Sani").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot: task.getResult()){
                        String mName = documentSnapshot.getString("Name");
                        String mPh_num = documentSnapshot.getString("Phone_number");

                        Log.d(TAG, "onComplete: " + mName + " & " + mPh_num);
                    }
                }
            }
        });
        /*
        dbFirestore.collection(signInAccount.getDisplayName()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null){

                    for (DocumentSnapshot docSnap: documentSnapshots) {

                        docSnap.getString("Name");
                        String userName = docSnap.getString("Name");

                        Log.d(TAG, "Name: " + userName);

                        }
                    }


                        String hand = String.valueOf(users.getHand());
                        String shoulder = String.valueOf(users.getShoulder());
                        String shirt = String.valueOf(users.getShirt());
                        String name = String.valueOf(users.getName());
                        String trouser = String.valueOf(users.getTrouser());
                        long phone = users.getPhone();


                       // values.add(users);

                       // boolean Insert = sqLiteHelper.insertContact(String.valueOf(name_val),
                       //         Integer.valueOf(shoulder_val), Integer.valueOf(trouser_val),
                       //         Integer.valueOf(shirt_val), Integer.valueOf(hand_val),
                       //         Long.valueOf(ph_val));

                }
        });
*/
    }
}