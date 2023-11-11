package com.example.mini2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity3 extends AppCompatActivity {
    String title;
    String summery;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    FirebaseUser user;
    FloatingActionButton bt;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        TextView TitleView = findViewById(R.id.textView);
        TextView SumsView = findViewById(R.id.DespSummery);
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            summery = intent.getStringExtra("summery");
        }

        TitleView.setText(title);
        SumsView.setText(summery);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.

        databaseReference = firebaseDatabase.getReference(user.getEmail().toString().substring(0, user.getEmail().toString().indexOf("@"))).child("Notes");
        if (user == null) {
            Intent intent2 = new Intent(MainActivity3.this, Login.class);
            startActivity(intent2);
            finish();
        } else {
            bt = findViewById(R.id.floatingActionButton2);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = "sayli.borole@cumminscollege.in";
                    final String password = "ABC";
                    Log.v("Good", "Good1");
                    String messagetosend = SumsView.getText().toString();
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "587");
                    Log.v("Good", "Good2");
                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                }
                            });
                    Log.v("Good", "Good5");
                    try {
                        Log.v("Good", "Good3");
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(username));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail().toString()));
                        message.setSubject("Your summary from our app");
                        message.setText(messagetosend);
                        Transport.send(message);
                        Log.v("Good", "Good4");
                        Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG).show();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }


    public void createPdf(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter PDF Name");

        // Set up the input
        final EditText input = new EditText(this);
        input.setText(title);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pdfFileName = input.getText().toString() + ".pdf";
                savePdf(pdfFileName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void savePdf(String pdfFileName) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);

        Uri pdfUri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);

        try {
            if (pdfUri != null) {
                OutputStream outputStream = getContentResolver().openOutputStream(pdfUri);
                if (outputStream != null) {
                    PdfWriter writer = new PdfWriter(outputStream);
                    PdfDocument pdf = new PdfDocument(writer);
                    Document document = new Document(pdf);
                    document.add(new Paragraph(summery));
                    document.close();

                    // Show a toast message after PDF is saved successfully
                    Toast.makeText(this, "PDF saved successfully: " + pdfUri.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void  DeleteFromDB(View view){
        databaseReference.child(title).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("Remove","removed");
                        Toast.makeText(MainActivity3.this,"Deleted successfully",Toast.LENGTH_LONG);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to delete the item
                        // Handle any errors, e.g., show an error message
                        Log.e("Remove","not removed: Failure");
                        String errormsg=e.getMessage().toString();
                        Toast.makeText(MainActivity3.this,"Deleted Unsuccessfully:"+errormsg,Toast.LENGTH_LONG);
                    }
                });
    }

}