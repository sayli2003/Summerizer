package com.example.mini2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.IOException;
import java.io.OutputStream;


public class ScrollerView extends AppCompatActivity {
    String title;
    String summery;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    FirebaseUser user;
    String noteID;
    TextView SumsView;
    FloatingActionButton bt;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller_view);

        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference(user.getEmail().toString().substring(0, user.getEmail().toString().indexOf("@")));

        if(user == null){
            Intent intent = new Intent(ScrollerView.this,Login.class);
            startActivity(intent);
            finish();
        }else {
            TextView TitleView = findViewById(R.id.textView);
            SumsView = findViewById(R.id.DespSummery);
            Intent intent = getIntent();
            if (intent != null) {
//                title = String.valueOf(intent.getStringExtra("title"));
//                Log.v("GOOD", title);
                summery = String.valueOf(intent.getStringExtra("desp"));
                Log.v("GOOD", summery);
            }
            String inputText=summery;
            ApiRequest.makeApiCall(this, inputText, new ApiRequest.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    //Handle API response here
                    Log.e("API_URL","output is: "+response.toString());
                    String jsonResponseString = response.toString();
                    try {

                        // Assuming jsonResponseString is the JSON response received from the API
                        JSONObject jsonResponse = new JSONObject(jsonResponseString);

                        // Extract the 'output' array from the JSON response
                        JSONArray outputArray = jsonResponse.getJSONArray("output");

                        // Get the first object from the 'output' array (assuming there is only one object)
                        JSONObject outputObject = outputArray.getJSONObject(0);

                        // Extract the 'contents' array from the 'output' object
                        JSONArray contentsArray = outputObject.getJSONArray("contents");

                        // Get the first object from the 'contents' array (assuming there is only one object)
                        JSONObject contentObject = contentsArray.getJSONObject(0);

                        // Extract the 'utterance' from the 'contents' object
                        String utterance = contentObject.getString("utterance");
                        System.out.println(utterance);

                        // Find the TextView by its ID

                        // Set the extracted utterance value to the TextView
                        SumsView.setText(utterance);

                    }catch(JSONException e){
e.printStackTrace();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    // Handle errors here
                    Log.e("API_URL","there is some error"+ error.getLocalizedMessage());
                }
            });
            bt = findViewById(R.id.floatingActionButton2);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = "sayli.borole@cumminscollege.in";
                    final String password = "ABC";
                    Log.v("Good","Good1");
                    String messagetosend = SumsView.getText().toString();
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "587");
                    Log.v("Good","Good2");
                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator(){
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                }
                            });
                    Log.v("Good","Good5");
                    try {
                        Log.v("Good","Good3");
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(username));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail().toString()));
                        message.setSubject("Your summary from our app");
                        message.setText(messagetosend);
                        Transport.send(message);
                        Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG).show();
                    }catch(MessagingException e){
                        e.printStackTrace();
                    }
                }
            });
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    public void SavetoHistory(View view)
    {
        EditText tvtitle=findViewById(R.id.textView);
        TextView SumsView= findViewById(R.id.DespSummery);
        String Title = tvtitle.getText().toString();
        String Sums = SumsView.getText().toString();
//String Sums="Title: The Remarkable Journey of Mitosis: A Tale of Cell Division Introduction:    Mitosis, a fundamental process in the world of cellular biology, plays a pivotal role in the growth, development, and maintenance of all multicellular organisms. It is a complex and tightly regulated mechanism that ensures that each new cell is a precise replica of the original. In this essay, we will explore the fascinating world of mitosis, its stages, functions, and significance in the grand tapestry of life \n I. Understanding Mitosis: Mitosis is the process by which a single eukaryotic cell divides into two identical daughter cells, each possessing a complete and identical set of genetic material. It is a part of the cell cycle, which consists of interphase (growth and preparation) and the mitotic phase (cell division). The main objective of mitosis is to maintain the organism's genetic integrity and ensure the distribution of genetic material to the next generation of cells. II. The Stages of Mitosis:Mitosis can be divided into several distinct stages, each with a specific role in the process: Prophase: In prophase, chromatin condenses into visible chromosomes, each consisting of two sister chromatids held together by a centromere. The nuclear envelope begins to break down, allowing spindle fibers to access the chromosomes.This stage marks the initiation of the actual cell division process. Metaphase:During metaphase, the chromosomes align along the cell's equatorial plane, known as the metaphase plate.The spindle fibers attach to the centromeres of each chromosome, ensuring their even distribution.Anaphase:Anaphase is the stage in which the sister chromatids are pulled apart towards opposite poles of the cell.This separation ensures that each daughter cell will receive an identical set of genetic material.Telophase:In telophase, the separated chromatids reach opposite ends of the cell and begin to de-condense into chromatin. A new nuclear envelope forms around each set of chromatids, resulting in the formation of two distinct nuclei.III. Significance of Mitosis:Mitosis plays a critical role in various biological processes:Growth and Development:    Mitosis is responsible for the increase in the number of cells during the development of an organism from a single fertilized egg into a multicellular individual.Tissue Repair: It is essential for tissue repair and regeneration in adults, helping to replace damaged or dead cells in various organs and tissues.Asexual Reproduction:   Mitosis is the primary method of reproduction in many unicellular and multicellular organisms, allowing them to produce genetically identical offspring.  Genetic Stability:  The process ensures that the genetic information is faithfully preserved and passed on to daughter cells, maintaining the stability of an organism's genetic makeup.Conclusion: Mitosis is a remarkable and intricate process that underlies the foundation of life as we know it. Its significance in the growth, development, and maintenance of organisms cannot be overstated. Through the stages of prophase, metaphase, anaphase, and telophase, mitosis accomplishes the crucial task of creating two genetically identical daughter cells from a single parent cell. This elegant dance of chromosomes ensures that life continues to thrive and adapt, serving as a testament to the incredible complexity of the natural world.";
            noteID = Title;
            // on below line we are passing all data to our modal class.
            Note putnote = new Note(noteID, Title, Sums);
            // on below line we are calling a add value event
            // to pass data to firebase database.
            Log.e("GOOD","not Added yet");
            if(Sums!="" && Title!=""){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // on below line we are setting data in our firebase database.
                    databaseReference.child("Notes").child(noteID).setValue(putnote);
                    // displaying a toast message.
                    Log.e("GOOD","Added");
                    Toast.makeText(ScrollerView.this, "Course Added..", Toast.LENGTH_SHORT).show();
                    // starting a main activity
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // displaying a failure message on below line.
                    Log.e("BAD","Got Cancelled");
                    String errormsg=error.getMessage().toString();
                    Toast.makeText(ScrollerView.this, "Could not Add:"+errormsg, Toast.LENGTH_SHORT).show();
                }
            });
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
                    document.add(new Paragraph(SumsView.getText().toString()));
                    document.close();

                    // Show a toast message after PDF is saved successfully
                    Toast.makeText(this, "PDF saved successfully: " + pdfUri.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }

