package com.ckt.ckttodo.database;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Realm.Transaction;
import io.realm.exceptions.RealmMigrationNeededException;

public class DatabaseHelper {

    private static final String TAG = "DatabaseHelper";

    private static DatabaseHelper mInstance;

    private static Object lock = new Object();

    private Realm mRealm;

    private DatabaseHelper(Context context) {
       try {
           Log.e(TAG,"DatabaseHelper configuration ");
           RealmConfiguration configuration = new RealmConfiguration.Builder(context)
                   .name(RealmConfiguration.DEFAULT_REALM_NAME)
                   .schemaVersion(3)
                   .deleteRealmIfMigrationNeeded()
                   .build();
           Realm.migrateRealm(configuration, new RealmMigration() {
               @Override
               public long execute(Realm realm, long version) {
                   // version 0
                   if (0 == version) {
                       Log.e(TAG,"migrateRealm version = "+version);
                       // do some chang
                       version++;
                   }
                   return version;
               }
           });
            mRealm = Realm.getInstance(configuration);
        } catch (RealmMigrationNeededException e) {
           Log.e(TAG,"RealmMigrationNeededException e = "+e.getMessage());
//            mRealm = null;
//
//            mRealm = Realm.getInstance(context);
        }
    }

    public static DatabaseHelper getInstance(Context context) {
        synchronized (lock) {
            if (mInstance == null) {
                mInstance = new DatabaseHelper(context);
            }
            return mInstance;
        }
    }

    public Realm getRealm() {
        return mRealm;
    }

    public void executeTransaction(final Transaction transaction) {
        checkNotNullObject(mRealm);
        mRealm.executeTransaction(transaction);
    }

    public RealmAsyncTask executeTransaction(final Transaction transaction, final Transaction.Callback callback) {
        checkNotNullObject(mRealm);
        return mRealm.executeTransaction(transaction, callback);
    }

    public <T extends RealmObject> RealmResults<T> findAll(Class<T> clazz) {
        return mRealm.where(clazz).findAll();
    }

    public <T extends RealmObject> RealmQuery<T> find(Class<T> clazz) {
        return mRealm.where(clazz);
    }

    public <T extends RealmObject> T insert(T t) {
        try {
            checkNotNullObject(t);
            mRealm.beginTransaction();
            T result = mRealm.copyToRealm(t);
            mRealm.commitTransaction();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            mRealm.cancelTransaction();
            return null;
        }
    }

    public <T extends RealmObject> List<T> insert(Iterable<T> objects) {
        try {
            if (objects == null) {
                return new ArrayList<T>();
            }
            mRealm.beginTransaction();
            List<T> reuslts = mRealm.copyToRealm(objects);
            mRealm.commitTransaction();
            return reuslts;
        } catch (Exception e) {
            e.printStackTrace();
            mRealm.cancelTransaction();
            return new ArrayList<T>();
        }
    }

    public <T extends RealmObject> T update(T t) {
        try {
            checkNotNullObject(t);
            mRealm.beginTransaction();
            T result = mRealm.copyToRealmOrUpdate(t);
            mRealm.commitTransaction();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            mRealm.cancelTransaction();
            return null;
        }
    }

    public <T extends RealmObject> List<T> update(Iterable<T> objects) {
        try {
            if (objects == null) {
                return new ArrayList<T>();
            }
            mRealm.beginTransaction();
            List<T> reuslts = mRealm.copyToRealmOrUpdate(objects);
            mRealm.commitTransaction();
            return reuslts;
        } catch (Exception e) {
            e.printStackTrace();
            mRealm.cancelTransaction();
            return new ArrayList<T>();
        }
    }

    public <T extends RealmObject> boolean delete(T t) {
        try {
            checkNotNullObject(t);
            mRealm.beginTransaction();
            t.removeFromRealm();
            mRealm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            mRealm.cancelTransaction();
            return false;
        }
    }

    public <T extends RealmObject> boolean deleteAll(RealmResults<T> results) {
        try {
            checkNotNullObject(results);
            mRealm.beginTransaction();
            results.clear();
            mRealm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            mRealm.cancelTransaction();
            return false;
        }
    }

    public <T extends RealmObject> T deleteIndex(RealmResults<T> results, int index) {
        try {
            checkNotNullObject(results);
            mRealm.beginTransaction();
            if (index < 0 || index >= results.size()) {
                mRealm.cancelTransaction();
                return null;
            }
            T result = results.remove(index);
            mRealm.commitTransaction();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            mRealm.cancelTransaction();
            return null;
        }
    }

    public <T extends RealmObject> boolean deleteLast(RealmResults<T> results) {
        try {
            checkNotNullObject(results);
            mRealm.beginTransaction();
            results.removeLast();
            mRealm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            mRealm.cancelTransaction();
            return false;
        }
    }

    private void checkNotNullObject(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Null objects cannot be copied into Realm.");
        } else if (mRealm == null) {
            throw new IllegalArgumentException("Realm object cannot be null.");
        }
    }

    //Get the realm PrimaryKey id by UUID
    public static String getPrimaryKeyId() {
        return UUID.randomUUID().toString();
    }
}
