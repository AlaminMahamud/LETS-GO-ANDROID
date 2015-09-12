package com.example.letsgogooglemap.Maps.library;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.letsgogooglemap.Maps.POJO.FriendPOJO;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	/**
	 * 01. All Static Variables 02. Database Versions 03. Database Name 04.
	 * Table Name 05. Table Column Names 06. OnCreate 07. onUpgrade 08. DATABASE
	 * FUNCTIONS a. Storing User Details in DATABASE b. Getting User Data from
	 * DATABASE c. Getting User login Status d. ReCreate Database e.
	 * DeleteAlltables And Create Them Again
	 * 
	 * 
	 * 
	 */

	// 01. All Static Variables
	// 02. Database Versions
	private static final int DATABASE_VERSION = 1;
	// 03. Database Name
	private static final String DATABASE_NAME = "cloud_contacts";
	// 04. Table Name
	private static final String TABLE_LOGIN = "login";
	private static final String TABLE_FRIENDS = "friend";
	// 05. Table Column Names
	// Login Table
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PHONE = "phone";
	// Friends Table
	private static final String F_KEY_ID = "id";
	private static final String F_KEY_NAME = "name";
	private static final String F_KEY_EMAIL = "email";
	private static final String F_KEY_PHONE = "phone";
	private static final String F_KEY_USERNAME = "username";

	// CREATE TABLE STATEMENTS
	private static final String CREATE_LOGIN_TABLE = "CREATE TABLE "
			+ TABLE_LOGIN + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
			+ " TEXT," + KEY_EMAIL + " TEXT UNIQUE," + KEY_USERNAME + " TEXT,"
			+ KEY_PHONE + " TEXT" + ")";
	private static final String CREATE_FRIENDS_TABLE = "CREATE TABLE "
			+ TABLE_FRIENDS + "(" + F_KEY_ID + " INTEGER PRIMARY KEY,"
			+ F_KEY_NAME + " TEXT," + F_KEY_EMAIL + " TEXT UNIQUE,"
			+ F_KEY_USERNAME + " TEXT," + F_KEY_PHONE + " TEXT" + ")";

	private static final String DROP_TABLE_LOGIN = "DROP TABLE IF EXISTS "
			+ TABLE_LOGIN;
	private static final String DROP_TABLE_FRIENDS = "DROP TABLE IF EXISTS "
			+ TABLE_FRIENDS;

	// Queries
	String selectQueryUser = "SELECT  * FROM " + TABLE_LOGIN;
	String selectQueryFriends = "SELECT * FROM" + TABLE_FRIENDS;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 06. Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_LOGIN_TABLE);
		db.execSQL(CREATE_FRIENDS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_LOGIN);
		db.execSQL(DROP_TABLE_FRIENDS);
		onCreate(db);
	}

	// 08. Storing User details in Database Table
	public void addUser(String name, String email, String username, String phone) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_USERNAME, username); // UserName
		values.put(KEY_PHONE, phone); // phone

		// Inserting Row
		db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection

	}

	// 09. Storing Friends details in Database Table
	public void addFriend(String name, String email, String username,
			String phone) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(F_KEY_NAME, name); // Name
		values.put(F_KEY_EMAIL, email); // Email
		values.put(F_KEY_USERNAME, username); // UserName
		values.put(F_KEY_PHONE, phone); // phone

		// Inserting Row
		db.insert(TABLE_FRIENDS, null, values);
		db.close(); // Closing database connection
	}

	// 10. Getting User Data from Database
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQueryUser, null);
		// Move to first Row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("username", cursor.getString(3));
			user.put("phone", cursor.getString(4));
		}
		cursor.close();
		db.close();
		return user;
	}

	// 11. Getting Friend's Data from Database
	public HashMap<String, String> getFriendDetails() {
		HashMap<String, String> friend = new HashMap<String, String>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQueryFriends, null);
		// Move to first Row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			friend.put("name", cursor.getString(1));
			friend.put("email", cursor.getString(2));
			friend.put("username", cursor.getString(3));
			friend.put("phone", cursor.getString(4));
		}
		cursor.close();
		db.close();
		return friend;
	}

	// 12. Getting User Login Status - Return trues if rows are there in table
	public int getUsersRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}

	// 13. Getting Friends Row Count - return trues if there are rows in table
	public int getFriendsRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_FRIENDS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}

	// 14. ReCreate Database - Delete All Tables and create them again
	public void resetUserTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();
	}

	public void resetFriendsTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_FRIENDS, null, null);
		db.close();
	}

	// 15. Get All Phone Numbers
	public ArrayList<String> getAllPhoneNumbers() {
		ArrayList<String> phone = new ArrayList<String>();
		String phoneQuery = "SELECT phone FROM " + TABLE_FRIENDS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(phoneQuery, null);
		if (cursor.getCount() > 0) {
			for (int i = 0; i < getFriendsRowCount(); i++) {
				phone.add(cursor.getString(4));
			}
		}
		return phone;
	}

	// 16. GET All Friends
	public ArrayList<FriendPOJO> getAllFriends() {
		ArrayList<FriendPOJO> friends = new ArrayList<FriendPOJO>();
		String friendsQuery = "SELECT * FROM " + TABLE_FRIENDS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(friendsQuery, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			for (int i = 0; i < getFriendsRowCount(); i++) {
				friends.add(new FriendPOJO(cursor.getString(1), cursor
						.getString(2), cursor.getString(3)));
			}
		}
		return friends;
	}

	// 17. isFriendExisted
	public boolean isFriendExisted(String phone_data) {
		String friendQuery = " SELECT * FROM " + TABLE_FRIENDS + " WHERE "
				+ F_KEY_PHONE + " = '" + phone_data + " ' ";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(friendQuery, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			return true;
		}
		return false;
	}
}
