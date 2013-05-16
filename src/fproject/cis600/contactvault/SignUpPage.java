package fproject.cis600.contactvault;


import fproject.cis600.contactvault.UsersTable;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignUpPage extends Activity 
{
	
	public UsersTable u1;
	
	
	public void generateAlertDialogBox(String title,String message)
    {
        // prepare the alert box                   
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
       
        alertbox.setTitle(title);
        alertbox.setCancelable(false);
        // set the message to display
        alertbox.setMessage(message);
   
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

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_page);
		
		u1=new UsersTable(this);
		
		Button button= (Button) findViewById(R.id.AddUser2DB);
        
        button.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
            	EditText t1=(EditText)findViewById(R.id.AddUserName);
            	EditText t2=(EditText)findViewById(R.id.password);
            	EditText t3=(EditText)findViewById(R.id.confirmPassword);
            	
            	Editable s1=t1.getText();
            	Editable s2=t2.getText();
            	Editable s3=t3.getText();

           	try
         	{
           			if(s1.toString()!="" && s2.toString().compareTo(s3.toString())==0)
	            	{
	            		if(u1.AddNewUserPassword(s1.toString(), s2.toString()))
	            		{
	            			//Toast.makeText(getApplicationContext(),"Successfully added User to Database", Toast.LENGTH_SHORT).show();
	            			generateAlertDialogBox("New User Added", "Successfully added User to Database");
	            		}
	            
	            		else
	            		{
	            			Toast.makeText(getApplicationContext(),"User Already Exists in Database", Toast.LENGTH_SHORT).show();
	            		}
	            	}
           			
           			else
           			{
           				Toast.makeText(getApplicationContext(),"The Passwords Don't match!!", Toast.LENGTH_SHORT).show();
           			}
             }
	    	 catch(Exception e)
	    	 {
	    	    	Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
	    	 }

            }
            
        });
        
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up_page, menu);
		return true;
	}
	
	

}
