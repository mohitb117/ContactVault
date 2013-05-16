/**
 * 
 */
package fproject.cis600.contactvault;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Mohit Bhalla
 *
 */
public class detailContactPage extends Activity 
{

	String currUserName,name,phoneNo,emailAddr,Addr,rowID;
	UsersTable u1;
	
	
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
	 	
	@Override
	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.activity_detail_contact);

	      Intent intent = getIntent();
	      
	      rowID=intent.getStringExtra("rowID");
	      name=intent.getStringExtra("name");
	      phoneNo=intent.getStringExtra("phoneno");
	      emailAddr=intent.getStringExtra("email");
	      Addr=intent.getStringExtra("addr");
	      currUserName=intent.getStringExtra("ownername");
		
	      controlTextBoxState(false);
	    
	      EditText t1=(EditText)findViewById(R.id.modifyName);
		  EditText t2=(EditText)findViewById(R.id.modifyPhon);
		  EditText t3=(EditText)findViewById(R.id.modifyEmail);
		  EditText t4=(EditText)findViewById(R.id.modifyAddr);
			
		  t1.setText(name);
		  t2.setText(phoneNo);
		  t3.setText(emailAddr);
		  t4.setText(Addr);
		  
	}
	
	public void controlTextBoxState(boolean enabled)
	{
		EditText t1=(EditText)findViewById(R.id.modifyName);
		EditText t2=(EditText)findViewById(R.id.modifyPhon);
		EditText t3=(EditText)findViewById(R.id.modifyEmail);
		EditText t4=(EditText)findViewById(R.id.modifyAddr);
		
		t1.setEnabled(enabled);
		t2.setEnabled(enabled);
		t3.setEnabled(enabled);
		t4.setEnabled(enabled);
	}
	
	
	public void checkStatus(View v)
	{
		CheckBox c1=(CheckBox)(findViewById(R.id.allowModify));
		
		if(c1.isChecked())//checked means allow editing the boxes
		{
			controlTextBoxState(true);
		}
		
		else
		{
			controlTextBoxState(false);
		}

	}
	
	public void updateContact(View v)
	{
		CheckBox c1=(CheckBox)(findViewById(R.id.allowModify));

	    EditText t1=(EditText)findViewById(R.id.modifyName);
		EditText t2=(EditText)findViewById(R.id.modifyPhon);
		EditText t3=(EditText)findViewById(R.id.modifyEmail);
		EditText t4=(EditText)findViewById(R.id.modifyAddr);
		
		if(c1.isChecked())//checked means allow editing the boxes
		{
			u1=new UsersTable(this);
			String []newValues=new String[]
										{
										t1.getText().toString(),
										t2.getText().toString(),
										t3.getText().toString(),
										t4.getText().toString(),
										currUserName
										};
			 try
			 {
				if(u1.updateSpecificContact(newValues,rowID))
				{
				//	Toast.makeText(this, "Successfully Updated Contact!!", Toast.LENGTH_SHORT).show();
					generateAlertDialogBox("Detail Contact Page", "Successfully Updated Contact!!");
				//System.exit(0);
				}
				else
				{
					Toast.makeText(this, "Unable to Update Contact!!", Toast.LENGTH_SHORT).show();
				}
			 }
		     catch(Exception e)
			 {
				Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
			 }

	   }
	}
	
	public void deleteContact(View v)
	{
		u1=new UsersTable(this);
		
		if(u1.deleteContact(rowID))
		{
			//Toast.makeText(this, "Successfully Deleted Contact!!", Toast.LENGTH_SHORT).show();
			generateAlertDialogBox("Detail Contact Page", "Successfully Deleted Contact!!");
		}
		
		else
		{
			Toast.makeText(this, "Unable to Delete Contact!!", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	public void exportContact(View v)
	{
		EditText t1=(EditText)findViewById(R.id.modifyName);
		EditText t2=(EditText)findViewById(R.id.modifyPhon);
		EditText t3=(EditText)findViewById(R.id.modifyEmail);
		EditText t4=(EditText)findViewById(R.id.modifyAddr);

		if(saveContact(t1.getText().toString(),t2.getText().toString(),t3.getText().toString(),t4.getText().toString()))
		{
			 Toast.makeText(this, "Successfully Added Contact", Toast.LENGTH_SHORT).show();
		}
		
		else
		{
			 Toast.makeText(this, "Unable to export Contact!", Toast.LENGTH_SHORT).show();
			    
		}
	}
	

	public boolean saveContact(String DisplayName,String MobileNumber,String emailID,String Address)
	{

		 ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

		 ops.add(ContentProviderOperation.newInsert(
		 ContactsContract.RawContacts.CONTENT_URI)
		     .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
		     .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
		     .build());

		 //------------------------------------------------------ Names
		 if (DisplayName != null)
		 {
		     ops.add(ContentProviderOperation.newInsert(
		     ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
		         .withValue(
		     ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
		     DisplayName).build());
		 }

		 //------------------------------------------------------ Mobile Number                     
		 if (MobileNumber != null) 
		 {
		     ops.add(ContentProviderOperation.
		     newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
		         .build());
		 }


		 //------------------------------------------------------ Email
		 if (emailID != null) 
		 {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
		         .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
		         .build());
		 }

		 //------------------------------------------------------ Organization
		 if (!Address.equals("") )
		 {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.StructuredPostal.DATA, Address)
		         .build());
		 }

		 // Asking the Contact provider to create a new contact                 
		 try 
		 {
			 getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);			 
		     return true;
		 } 
		 catch (Exception e) 
		 {
		     e.printStackTrace();
		     Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		     return false;
		 }

	}
	
	public void callContact(View v)
	{
		EditText t3=(EditText)findViewById(R.id.modifyPhon);
		
		startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ t3.getText().toString())));
			
	}
	
	public void emailContact(View v)
	{
		EditText t3=(EditText)findViewById(R.id.modifyEmail);
		
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		  emailIntent.setType("plain/text");

		  emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { t3.getText().toString() });

		  emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Email Subject");

		  emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Email Body");

		  startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}
	
	public void msgContact(View v)
	{
		EditText t3=(EditText)findViewById(R.id.modifyPhon);
	
		startActivity(
				new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+ t3.getText().toString())).
				putExtra(android.content.Intent.EXTRA_TEXT, "Insert Message Here"));
	}
	
}




