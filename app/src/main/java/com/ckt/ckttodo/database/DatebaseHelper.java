package com.ckt.ckttodo.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DatebaseHelper {

    private static final String TAG = "caokai";

    private static DatebaseHelper mInstance;

    private static Object lock = new Object();

    private Realm mRealm;

    private DatebaseHelper(Context context) {
        mRealm = Realm.getInstance(context);
    }

    public static  DatebaseHelper getInstance(Context context) {
        synchronized (lock) {
            if (mInstance == null) {
                mInstance = new DatebaseHelper(context);
            }
            return mInstance;
        }
    }

    public Realm getRealm() {
        return mRealm;
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
        }catch (Exception e) {
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
        }catch (Exception e) {
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
        }catch (Exception e) {
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
        }catch (Exception e) {
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
        }catch (Exception e) {
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
        }catch (Exception e) {
            e.printStackTrace();
            mRealm.cancelTransaction();
            return false;
        }
    }

    public <T extends RealmObject> T deleteIndex(RealmResults<T> results ,int index) {
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
        }catch (Exception e) {
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
        }catch (Exception e) {
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
}
