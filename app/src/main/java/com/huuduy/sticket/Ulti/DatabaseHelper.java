package com.huuduy.sticket.Ulti;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.huuduy.sticket.Tickets.TicketModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huudu on 26/12/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ticketsManager2";

    // Contacts table name
    private static final String TABLE_TICKETS = "tickets";

    // Tickets Table Columns names
    private static final String KEY_ID_TICKET = "idTicket";
    private static final String KEY_ID_USER = "idUser";
    private static final String KEY_ID_DEVICE = "idDevice";
    private static final String KEY_ID_EVENT = "idEvent";
    private static final String KEY_TITLE = "title";
    private static final String KEY_INFORMATION = "information";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: ");
        String CREATE_TICKETS_TABLE = "CREATE TABLE " + TABLE_TICKETS + "("
                + KEY_ID_TICKET + " TEXT PRIMARY KEY,"
                + KEY_ID_USER + " INTEGER,"
                + KEY_ID_DEVICE + " TEXT,"
                + KEY_ID_EVENT + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_INFORMATION + " TEXT" + ")";
        db.execSQL(CREATE_TICKETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKETS);

        // Create tables again
        onCreate(db);
    }

    public void create() {
        String CREATE_TICKETS_TABLE = "CREATE TABLE " + TABLE_TICKETS + "("
                + KEY_ID_TICKET + " TEXT PRIMARY KEY,"
                + KEY_ID_USER + " INTEGER,"
                + KEY_ID_DEVICE + " TEXT,"
                + KEY_ID_EVENT + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_INFORMATION + " TEXT" + ")";
        this.getWritableDatabase().execSQL(CREATE_TICKETS_TABLE);
    }

    public void clear() {
        String selectQuery = "SELECT  * FROM " + TABLE_TICKETS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Delete ticket
                db.delete(TABLE_TICKETS, KEY_ID_TICKET + " = ?",
                        new String[]{String.valueOf(cursor.getString(0))});
            } while (cursor.moveToNext());
        }
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addTicket(TicketModel ticket) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_TICKET, ticket.getIdTicket());
        values.put(KEY_ID_USER, ticket.getIdUser());
        values.put(KEY_ID_DEVICE, ticket.getDevice());
        values.put(KEY_ID_EVENT, ticket.getIdEvent());
        values.put(KEY_TITLE, ticket.getTitle());
        values.put(KEY_INFORMATION, ticket.getInformation());

        // Inserting Row
        db.insert(TABLE_TICKETS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single ticket
    TicketModel getATicket(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TICKETS, new String[]{KEY_ID_TICKET, KEY_ID_USER, KEY_ID_DEVICE, KEY_ID_EVENT,
                KEY_TITLE, KEY_INFORMATION}, KEY_ID_TICKET + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TicketModel ticket = new TicketModel(
                cursor.getString(0), Integer.parseInt(cursor.getString(1)),
                cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(5), cursor.getString(5));
        return ticket;
    }

    // Getting All Contacts
    public List<TicketModel> getAllTickets() {
        List<TicketModel> ticketsList = new ArrayList<TicketModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TICKETS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TicketModel ticket = new TicketModel();
                ticket.setIdTicket(cursor.getString(0));
                ticket.setIdUser(Integer.parseInt(cursor.getString(1)));
                ticket.setDevice(cursor.getString(2));
                ticket.setIdEvent(cursor.getString(3));

                ticket.setTitle(cursor.getString(4));
                ticket.setInformation(cursor.getString(5));
                // Adding contact to list
                ticketsList.add(ticket);
            } while (cursor.moveToNext());
        }

        // return contact list
        return ticketsList;
    }

    // Updating single contact
    public int updateTicket(TicketModel ticket) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_USER, ticket.getIdUser());
        values.put(KEY_ID_DEVICE, ticket.getDevice());
        values.put(KEY_ID_EVENT, ticket.getIdEvent());
        values.put(KEY_TITLE, ticket.getTitle());
        values.put(KEY_INFORMATION, ticket.getInformation());

        // updating row
        return db.update(TABLE_TICKETS, values, KEY_ID_TICKET + " = ?",
                new String[]{String.valueOf(ticket.getIdTicket())});
    }

    // Deleting single contact
    public void deleteTicket(TicketModel ticket) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TICKETS, KEY_ID_TICKET + " = ?",
                new String[]{String.valueOf(ticket.getIdTicket())});
        db.close();
    }


    // Getting contacts Count
    public int getTicketsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TICKETS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
