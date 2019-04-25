package com.tocydoludoge.wheelersbayclient.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.tocydoludoge.wheelersbayclient.Model.Hire;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "wheel";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Hire> getCarts() {


        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"PlateId", "CarName", "Rate","Duration","Discount"};
        String sqlTable = "HireDetail";


        qb.setTables(sqlTable);

        Cursor c = qb.query(db, sqlSelect, null, null,null,null, null);
        final List<Hire> results = new ArrayList<>();
        if (c.moveToFirst()) {
            do {


                results.add(new Hire(c.getString(c.getColumnIndex("PlateId")),
                        c.getString(c.getColumnIndex("CarName")),
                        c.getString(c.getColumnIndex("Rate")),
                        c.getString(c.getColumnIndex("Duration")),
                        c.getString(c.getColumnIndex("Discount"))
                ));
            } while (c.moveToNext());
       }
        return results;
    }

    public void addToCart(Hire hire) {

        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("INSERT INTO HireDetail    (PlateId,CarName,Duration,Rate,Discount)VALUES('%s','%s','%s','%s','%s');",
                hire.getPlateId(),
                hire.getCarName(),
                hire.getDuration(),
                hire.getRate(),
                hire.getDiscount());


        database.execSQL(query);


    }

    public void removeFromCart(String hire) {

        SQLiteDatabase db = getReadableDatabase();

        String query = String.format("DELETE FROM HireDetail WHERE CarId='" + hire + "'");
        db.execSQL(query);
    }

    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM HireDetail");
        db.execSQL(query);
    }

    //Favorite

    public void addToFavorite(String plateId){

        SQLiteDatabase database=getReadableDatabase();
        String query=String.format("INSERT INTO Favorites(PlateId) VALUES('%s');",plateId);
        database.execSQL(query);


    }

    public void deleteFromFavorite(String plateId){

        SQLiteDatabase database=getReadableDatabase();
        String query=String.format("DELETE FROM Favorites WHERE PlateId='%s';",plateId);
        database.execSQL(query);
    }
    public boolean isFavorite(String plateId)
    {

        SQLiteDatabase database=getReadableDatabase();
        String query=String.format("SELECT * FROM Favorites WHERE PlateId='%s';",plateId);
        Cursor cursor=database.rawQuery(query,null);

        if(cursor.getCount()<=0){

            cursor.close();
            return false;
        }
        cursor.close();
        return true;


    }
}
