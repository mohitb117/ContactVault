/**
 * 
 */
package fproject.cis600.contactvault;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Mohit Bhalla
 *
 */
public class AddContactPage extends Activity
{
	public static String currentUserName;
	UsersTable u1;
	
	/*
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contacts);
		
		u1=new UsersTable(this);
		
		Intent intent = getIntent();  
		currentUserName=(String)intent.getCharSequenceExtra("OwnerName");
	 
	}
	
 	public void generateAlertDialogBox(String title,String message)
    {
        // prepare the alert box                   
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
       
        alertbox.setTitle(title);
        
        // set the message to display
        alertbox.setMessage(message);
        alertbox.setCancelable(false);
        // add a neutral button to the alert box and assign a click listener
       
        alertbox.setPositiveButton("Resume", new DialogInterface.OnClickListener() 
        {
	        public void onClick(DialogInterface arg0, int arg1) 
	        {
	        	System.exit(0);    	
		    }
        });
        
        alertbox.show();
    }

	
	public void SaveContact(View v)
	{
		try
		{
			EditText name=(EditText)findViewById(R.id.contactNameRes);
			EditText number=(EditText)findViewById(R.id.contactNumRes);
			EditText email=(EditText)findViewById(R.id.contactEmailRes);
			EditText addr=(EditText)findViewById(R.id.contactAddrRes);
			
			String t1=(String)name.getText().toString();
			String t2=(String)number.getText().toString();
			String t3=(String)email.getText().toString();
			String t4=(String)addr.getText().toString();
		
		    if(u1.AddNewContact(new String[]{t1,t2,t3,t4,currentUserName}))
			{
			
				generateAlertDialogBox("Add Contact Page", "Contact Was Added Successfully to your Account!");
			}
			else
			{
				Toast.makeText(this, "Unable to Add Contact to your account!", Toast.LENGTH_SHORT).show();
			}
		    
		    		    	
		}
		
		catch(Exception e)
		{
			Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

}
