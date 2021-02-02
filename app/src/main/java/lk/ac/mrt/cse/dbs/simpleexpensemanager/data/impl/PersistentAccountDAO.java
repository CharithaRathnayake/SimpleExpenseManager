package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHelper databaseHelper;

    public PersistentAccountDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ DatabaseHelper.COL_ACCOUNT_NO +" FROM " + DatabaseHelper.TABLE_ACCOUNT, null);

        // looping through all rows and adding to the accountNumberList
        if (cursor.moveToFirst()) {
            do {
                // Adding account to the accountNumberList
                accountNumbersList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ACCOUNT, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Double.parseDouble(cursor.getString(3))
                );
                // Adding account to list
                accountList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ACCOUNT + " WHERE " + DatabaseHelper.COL_ACCOUNT_NO +" = " + accountNo, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Account account = new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Double.parseDouble(cursor.getString(3))
            );
            cursor.close();
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ACCOUNT_NO, account.getAccountNo()); // Account No
        values.put(DatabaseHelper.COL_BANK_NAME, account.getBankName()); // Bank Name
        values.put(DatabaseHelper.COL_ACCOUNT_HOLDER_NAME, account.getAccountHolderName()); // Holder Name
        values.put(DatabaseHelper.COL_BALANCE, account.getBalance()); // Balance

        // Inserting Row
        db.insert(DatabaseHelper.TABLE_ACCOUNT, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_ACCOUNT, DatabaseHelper.COL_ACCOUNT_NO + " = ?",
                new String[] { accountNo });
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = this.getAccount(accountNo);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(DatabaseHelper.COL_BALANCE, account.getBalance() - amount);
                break;
            case INCOME:
                values.put(DatabaseHelper.COL_BALANCE, account.getBalance() + amount);
                break;
        }
        // updating row
        db.update(DatabaseHelper.TABLE_ACCOUNT, values, DatabaseHelper.COL_ACCOUNT_NO + " = ?",
                new String[] { accountNo });
    }

}
