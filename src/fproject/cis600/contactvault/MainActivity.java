package fproject.cis600.contactvault;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import fproject.cis600.contactvault.*;
public class MainActivity extends Activity 
{

	UsersTable u1;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		u1=new UsersTable(this);
	}
	@Override
	protected void  onResume() 
	{
		super.onResume();
		
		EditText t1=(EditText)findViewById(R.id.usrName);
		EditText t2=(EditText)findViewById(R.id.usrPass);
		
		t1.setText("");
		t2.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void SignUpPage(View v)
	{
    	Intent intent = new Intent(this, SignUpPage.class);
        startActivity(intent);	
	}

	public void SignInPage(View v)
	{
		EditText t1=((EditText)findViewById(R.id.usrName));
		Editable userName=t1.getText();
		
		t1=((EditText)findViewById(R.id.usrPass));
	    Editable userPass=t1.getText();
	    
	    /*
	     * Pass they key value pair to a model database and check if the password exists or not
	     *
	     *
	     *if user/password is OK, then ContactViewerPage
	     *
	     *else AlertDialog to show invalid password
	     *
	     */
	  
	    try
	    {
		    if(u1.confirmUserPassword(userName.toString(), userPass.toString()))
		    {
		    	Toast.makeText(this, "Access Allowed", Toast.LENGTH_SHORT).show();
		    
		    	Intent intent = new Intent(this, ContactViewerPage.class);
		    	intent.putExtra("OwnerName", userName.toString());
		    	startActivity(intent);			    	
		    }
		    else
		    {
		    	Toast.makeText(this, "Access Denied, username/password mismatch", Toast.LENGTH_SHORT).show();	
		    }
	    }
	    catch(Exception e)
	    {
	    	Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void generateAlertDialogBox(String title,String message)
    {
        // prepare the alert box                   
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
       
        alertbox.setTitle(title);
        
        // set the message to display
        alertbox.setMessage(message);
        
        alertbox.show();
    }
	
	public void AboutMe(View v)
	{
		String message="The Purpose of the application is to create \n" +
				"1. a contacts utility which can partition viewability of contacts " +
				 " to keep from prying eyes on your android phone.\n" +
				"2. The application allows to export contacts  \n " +
				"3. Reach the specified contact using his details\n" +
				"Made By Mohit Bhalla SUID 919718909";
		generateAlertDialogBox("About Contacts Vault!", message);
	}
	
}
