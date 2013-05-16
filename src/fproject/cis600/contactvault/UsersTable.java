/**
 * 
 */
package fproject.cis600.contactvault;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * @author Mohit Bhalla
 *
 */
public class UsersTable extends SQLiteOpenHelper
{

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	static final String usrCol="username";
	static final String passCol="password";
	static final String TableName="userAccessTable";
	public static final String tableID = "_id";

	static final String [] ownerColumns={usrCol,passCol};
	
	
	//////////////////////////////////////////////////////////////	
	
	static public final String dbName="contactsDB";

	//////////////////////////////////////////////////////////////
	static final String ContactsTable="contactsTable";
	
	public static final String contactID="_id";	
	static final String contactName="cName";
	static final String phoneNo="cPhoneNo";
	static final String emailAddr="cEmail";
	static final String Address="cAddress";
	static final String ownerID="cOwner";

	
	static final String [] ContactsColumns={contactName,phoneNo,emailAddr,Address,ownerID};
	//////////////////////////////////////////////////////////////
	
	static String[] getContactTableColumns4ListView()
	{
		return new String[]{contactID,contactName,phoneNo};
	}
	
	///////////////////////////////////////////////////////////////
	
	public UsersTable(Context context)
	{
		super(context, dbName, null,117);	
	}
	
//	public UsersTable(Context context, CursorFactory factory,int version) 
//	{
//		super(context, dbName, factory, version);
//	}

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @param errorHandler
	 */
	
//	public UsersTable(Context context, CursorFactory factory,int version, DatabaseErrorHandler errorHandler) 
//	{
//		super(context, dbName, factory, version, errorHandler);
//	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		//create table for the contact owners
		
		db.execSQL("CREATE TABLE "+TableName+" ("+tableID+" INTEGER Primary key AUTOINCREMENT, " +
						""+usrCol+" TEXT,"+passCol+" TEXT );");	
		
		//create table for the contacts 
		
		db.execSQL("CREATE TABLE "+ContactsTable+" " +
						"("+contactID+" INTEGER Primary key AUTOINCREMENT, " 
						  +contactName+" TEXT,"
						  +phoneNo  +" TEXT, " 
					 	  +emailAddr+" TEXT, "               
						  +Address  +" TEXT, "
						  +ownerID  +" TEXT );");
		}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) 
	{
		// TODO Auto-generated method stub
	}
	
	

	public boolean AddNewUserPassword(String username,String password)
	{
	   if(checkIfUserExists(username))
			   return false;
	   else
	   {
			SQLiteDatabase db=this.getWritableDatabase();	
			ContentValues cv=new ContentValues();
			
		    cv.put(usrCol,username);
		    cv.put(passCol,password);
			 
			db.insert(TableName,null, cv);
			db.close();
			return true;
	   }

	}
	
	public boolean confirmUserPassword(String username,String password)
	{	 
	 SQLiteDatabase db=this.getReadableDatabase();
	 
	 String [] columns=new String[]{tableID,usrCol,passCol};

	  // Cursor c=db.query(TableName, columns, passCol+"=?", new String[]{password}, null, null, null);
	 
	  Cursor c=db.rawQuery(" Select * from "+TableName,new String[]{});   //+" Where "+passCol+" =? ", new String[]{password});
	   
	  c.moveToFirst();
	   
	   do//iterate through all the columns
	   {
		     int usrColumn=c.getColumnIndex(usrCol);
		     int paswordColumn=c.getColumnIndex(passCol);
			   
			 String usr=c.getString(usrColumn);
			   
			 String pass=c.getString(paswordColumn);
	   
			 if(usr.compareTo(username)==0 && pass.compareTo(password)==0)
				 return true;
	   }
	   while(c.moveToNext());
	   
		   return false;
	   
	}
	
	
	public boolean checkIfUserExists(String user)
	{
		  SQLiteDatabase db=this.getReadableDatabase();
		 
		  Cursor c=db.rawQuery(" Select * from "+TableName+" Where "+usrCol+"=?",new String[]{user});
		   
  	      c.moveToFirst();
		   
  	      if(c.getCount()!=0)
  	      {
			     int usrColumn=c.getColumnIndex(usrCol);
				 String usr=c.getString(usrColumn);
				   
				 if(usr.compareTo(user)==0)
					 return true;
		   }
  	    	  return false;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public boolean AddNewContact(String [] attributes)
	{
			SQLiteDatabase db=this.getWritableDatabase();	
			ContentValues cv=new ContentValues();
			
			int ctr=0;
			//populate the contentValues and push them to the database 
			try
			{
				for (String string : attributes) 
				{
					cv.put(ContactsColumns[ctr], string);
					ctr++;
				}
			     
				db.insert(ContactsTable,null, cv);
				db.close();
				return true;
				
			}
			catch(Exception e)
			{
				
			}
			
			return false;
	
	}
/////////////////////////////////////////////////////////////////////////
	public Cursor getCursorForContactsOwner(String user)
	{
		  SQLiteDatabase db=this.getReadableDatabase();
		 
		  Cursor c=db.rawQuery(" Select * from "+ContactsTable+" Where "+ownerID+"=?",new String[]{user});
		   
  	      c.moveToFirst();
		 
  	      return c;
  	     
	}

	public String[] getContactfromRowID(String id)
	{
		 SQLiteDatabase db=this.getReadableDatabase();
		 
		 Cursor c=db.rawQuery(" Select * from "+ContactsTable+" Where "+contactID+"=?",new String[]{id});
		 
		 c.moveToFirst();
	
		int []columns=new int[c.getColumnCount()];
		
		int ctr=0;
		
		for (String  column : c.getColumnNames()) 
		{
			columns[ctr++]=c.getColumnIndex(column);
		}
		
		return new String[]{
				c.getString(columns[1]),c.getString(columns[2]),
				c.getString(columns[3]),c.getString(columns[4]),};
		 
	}
	
	
	
	public boolean updateSpecificContact(String [] newValues,String rowID)//contains all the key value pairs
	{
		
		 SQLiteDatabase db=this.getWritableDatabase();
		 ContentValues cv=new ContentValues();

			int ctr=0;

			for (String string : newValues) 
			{
					cv.put(ContactsColumns[ctr], string);
				    ctr++;
			}
		
			if(db.update(ContactsTable, cv,contactID+"=?", new String []{rowID})>0)
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	
	public boolean deleteContact(String rowID)//contains all the key value pairs
	{
			 SQLiteDatabase db=this.getWritableDatabase();		 
			 if(db.delete(ContactsTable,contactID+"=?", new String [] {rowID})>0)
			 {
				 db.close();
				 return true;
			 }
			 
			 else
			 {
				 db.close();
				 return false;
			 }
	}
}

