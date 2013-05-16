package fproject.cis600.contactvault;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ContactViewerPage extends ListActivity 
{
	public String currentUserName; 
	public UsersTable u1;

	private static String[] FROM;
	
	private static int[] TO = {R.id.RowID, R.id.contactNameQuantum,R.id.contactNumberQuantum };
	public Cursor c;
	
	
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
	 public void onCreate(Bundle savedInstanceState) 
	 {
		  super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_contact_viewer_page);
	
		  u1=new UsersTable(this);
		  
		  FROM=UsersTable.getContactTableColumns4ListView();
	     
		  //TextView t1=(TextView)findViewById(R.id.userLabel);
		  
	      Intent intent = getIntent();
	      
	      currentUserName=(String)intent.getCharSequenceExtra("OwnerName");
	      
	      setTitle("Welcome "+currentUserName+"!!");
	      
	 	 /*
		 * 	 Populate the list of contacts from the userTable Database 
		 *   with the property that the contacts are owned by the currentUser who logged in from the main page
		 * 
		 *   step 1: 
		 * 
		 */
		try
		{
			      PopulateUserSpecificContacts();
		}
		
		catch(Exception e)
		{
			Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
		}
	 }
	
	 public void signOut(View v)
	 {
		 generateAlertDialogBox("Sign Out", "You have been Successfully Signed Out!");
	 }
	 
	@SuppressWarnings("deprecation")
	public void PopulateUserSpecificContacts()
	 {
		 c=u1.getCursorForContactsOwner(currentUserName);
		 // Fill Cursor 
		 if(c.getCount()>0)
		 {	
			 SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.listviewquantum,c, FROM, TO);
			 setListAdapter(adapter);
		 }
	 }
	 
	 public void AddNewContactButton(View v)
	 {
	    	Intent intent = new Intent(this, AddContactPage.class);
	    	intent.putExtra("OwnerName",currentUserName);
	    	startActivity(intent);			    	
	 }

	 public void exportAllContacts(View v)
	 {
		 try
		 {
				 c=u1.getCursorForContactsOwner(currentUserName);
				 /*
				  * step 1: for each user in the cursor
				  * step 2: extract a particular rowID
				  * step 3: push contents for the particular contact!!
				  * 
				  */
				 int count=c.getCount();
				 do
				 {
					 String  rowID=c.getString(
							       c.getColumnIndex(
									   UsersTable.getContactTableColumns4ListView()[0]));
				
					 String [] details=u1.getContactfromRowID(rowID);
					 
					 exportContact(details[0],details[1], details[2], details[3]);
					 
				 }
				 while(c.moveToNext());
				 
			 Toast.makeText(this,"Number of Contacts Exported=>"+count, Toast.LENGTH_SHORT).show();
						 
		 }
		 	
		 catch(Exception e)
		{
				Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
		}
	 }
	 
	 @Override
	 protected void onListItemClick(ListView l, View v, int position, long id)
	 {
	        // TODO Auto-generated method stub
	        super.onListItemClick(l, v, position, id);
	        
	        /*
	         * get the row id as the unique identifier for the 
	         * contact which was clicked in the listView
	         * 
	         */

	        try
	        {
		        String [] transferDetails=u1.getContactfromRowID(String.valueOf(id));
		        
		        Intent intent = new Intent(this, detailContactPage.class);
		        
		        intent.putExtra("rowID",String.valueOf(id));
		        intent.putExtra("name",transferDetails[0]);
		        intent.putExtra("phoneno",transferDetails[1]);
		        intent.putExtra("email",transferDetails[2]);
		        intent.putExtra("addr",transferDetails[3]);
		        intent.putExtra("ownername",currentUserName);
		    	
		    	startActivity(intent);			    	
	        }
	    	
	    	catch(Exception e)
			{
				Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
			}
		
    }
	 
	 
	 	public boolean exportContact(String DisplayName,String MobileNumber,String emailID,String Address)
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

	 
	
}


//private static String[] FROM = { _ID, TIME, TITLE, };
//private static int[] TO = { R.id.rowid, R.id.time, R.id.title, };
//private static String ORDER_BY = TIME + " DESC";
//
//
//@Override
//public void onCreate(Bundle savedInstanceState) 
//{
//   super.onCreate(savedInstanceState);
//   setContentView(R.layout.main);
//   addEvent("Hello, Android!");
//   
//   Cursor cursor = getEvents();
//   showEvents(cursor);
//}
//
//private void addEvent(String string) 
//{
//   // Insert a new record into the Events data source.
//   // You would do something similar for delete and update.
//   ContentValues values = new ContentValues();
//   values.put(TIME, System.currentTimeMillis());
//   values.put(TITLE, string);
//   getContentResolver().insert(CONTENT_URI, values);
//}
//
//private Cursor getEvents() 
//{
//   // Perform a managed query. The Activity will handle closing
//   // and re-querying the cursor when needed.
//   return managedQuery(CONTENT_URI, FROM, null, null, ORDER_BY);
//}
//
//private void showEvents(Cursor cursor) 
//{
//   // Set up data binding
//   SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.item, cursor, FROM, TO);
//   setListAdapter(adapter);
//}
