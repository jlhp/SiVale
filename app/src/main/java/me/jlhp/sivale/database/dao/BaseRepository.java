package me.jlhp.sivale.database.dao;

/**
 * Created by JOSELUIS on 7/10/2014.
 */

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import me.jlhp.sivale.database.DatabaseHelper;

public abstract class BaseRepository<T> {
    DatabaseHelper databaseHelper;
    RuntimeExceptionDao<T, Integer> dao;
    Class<T> entityClass;
    Context context;

    public BaseRepository(Context ctx, Class<T> entityClass) {
        try {
            this.context = ctx;
            this.entityClass = entityClass;

            databaseHelper = new DatabaseHelper(ctx);
            TableUtils.createTableIfNotExists(databaseHelper.getConnectionSource(), entityClass);
            dao = RuntimeExceptionDao.createDao(databaseHelper.getConnectionSource(), entityClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int create(T item) {
        try {
            return dao.create(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int createOrUpdate(T item) {
        try {
            return dao.createOrUpdate(item).getNumLinesChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(T item) {
        try {
            return dao.update(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(T item) {
        try {
            return dao.delete(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public T get(int id) {
        try {
            return dao.queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> get(T item) {
        try {
            return dao.queryForMatching(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    public List<T> getAll() {
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    public void removeAll() {
        List<T> items = getAll();

        if(items != null && items.size() > 0) {
            for(T item : items) {
                delete(item);
            }
        }
    }

    public void inserBatch(final List<T> items) {
        try {
            TransactionManager.callInTransaction(databaseHelper.getConnectionSource(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            for (T item : items) {
                                dao.create(item);
                            }
                            return null;
                        }
                    }
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBatchDeleteAllFirst(final List<T> items) {
        try {
            TransactionManager.callInTransaction(databaseHelper.getConnectionSource(),
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            TableUtils.clearTable(databaseHelper.getConnectionSource(), entityClass);

                            for (T item : items) {
                                dao.create(item);
                            }
                            return null;
                        }
                    }
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBatchDeleteAllFirst(final List<T> items, boolean dropTable) {
        try {
            if(dropTable) {
                TableUtils.dropTable(databaseHelper.getConnectionSource(), entityClass, true);
                TableUtils.createTable(databaseHelper.getConnectionSource(), entityClass);
            }

            insertBatchDeleteAllFirst(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RuntimeExceptionDao<T, Integer> getDao(){
        return dao;
    }
}