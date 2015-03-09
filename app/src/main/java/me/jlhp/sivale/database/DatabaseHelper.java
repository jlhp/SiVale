package me.jlhp.sivale.database;

/**
 * Created by JOSELUIS on 3/9/14.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.enevasys.autotraffic.R;
import com.enevasys.autotraffic.model.Address;
import com.enevasys.autotraffic.model.Article;
import com.enevasys.autotraffic.model.City;
import com.enevasys.autotraffic.model.Country;
import com.enevasys.autotraffic.model.Device;
import com.enevasys.autotraffic.model.DeviceCategory;
import com.enevasys.autotraffic.model.DocumentType;
import com.enevasys.autotraffic.model.Image;
import com.enevasys.autotraffic.model.Impound;
import com.enevasys.autotraffic.model.Infraction;
import com.enevasys.autotraffic.model.InfractionAlcoholimeter;
import com.enevasys.autotraffic.model.InfractionCommon;
import com.enevasys.autotraffic.model.InfractionImpound;
import com.enevasys.autotraffic.model.InfractionTow;
import com.enevasys.autotraffic.model.InfractionType;
import com.enevasys.autotraffic.model.Location;
import com.enevasys.autotraffic.model.MonitoringLog;
import com.enevasys.autotraffic.model.Offender;
import com.enevasys.autotraffic.model.OffenderDocument;
import com.enevasys.autotraffic.model.PartialMonitoringLog;
import com.enevasys.autotraffic.model.Project;
import com.enevasys.autotraffic.model.State;
import com.enevasys.autotraffic.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "Autotraffic.sqllite";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 28;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Article.class);
            TableUtils.createTableIfNotExists(connectionSource, City.class);
            TableUtils.createTableIfNotExists(connectionSource, State.class);
            TableUtils.createTableIfNotExists(connectionSource, Country.class);
            TableUtils.createTableIfNotExists(connectionSource, Project.class);
            TableUtils.createTableIfNotExists(connectionSource, Address.class);
            TableUtils.createTableIfNotExists(connectionSource, DocumentType.class);
            TableUtils.createTableIfNotExists(connectionSource, InfractionType.class);
            TableUtils.createTableIfNotExists(connectionSource, Location.class);
            TableUtils.createTableIfNotExists(connectionSource, OffenderDocument.class);
            TableUtils.createTableIfNotExists(connectionSource, Offender.class);
            TableUtils.createTableIfNotExists(connectionSource, Image.class);
            TableUtils.createTableIfNotExists(connectionSource, Impound.class);
            TableUtils.createTableIfNotExists(connectionSource, Infraction.class);
            TableUtils.createTableIfNotExists(connectionSource, InfractionAlcoholimeter.class);
            TableUtils.createTableIfNotExists(connectionSource, InfractionCommon.class);
            TableUtils.createTableIfNotExists(connectionSource, InfractionImpound.class);
            TableUtils.createTableIfNotExists(connectionSource, InfractionTow.class);
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Device.class);
            TableUtils.createTableIfNotExists(connectionSource, DeviceCategory.class);
            TableUtils.createTableIfNotExists(connectionSource, MonitoringLog.class);
            TableUtils.createTableIfNotExists(connectionSource, PartialMonitoringLog.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

     /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");

            TableUtils.dropTable(connectionSource, Article.class, true);
            TableUtils.dropTable(connectionSource, City.class, true);
            TableUtils.dropTable(connectionSource, State.class, true);
            TableUtils.dropTable(connectionSource, Country.class, true);
            TableUtils.dropTable(connectionSource, Project.class, true);
            TableUtils.dropTable(connectionSource, Address.class, true);
            TableUtils.dropTable(connectionSource, DocumentType.class, true);
            TableUtils.dropTable(connectionSource, InfractionType.class, true);
            TableUtils.dropTable(connectionSource, Location.class, true);
            TableUtils.dropTable(connectionSource, Offender.class, true);
            TableUtils.dropTable(connectionSource, OffenderDocument.class, true);
            TableUtils.dropTable(connectionSource, Image.class, true);
            TableUtils.dropTable(connectionSource, Impound.class, true);
            TableUtils.dropTable(connectionSource, Infraction.class, true);
            TableUtils.dropTable(connectionSource, InfractionAlcoholimeter.class, true);
            TableUtils.dropTable(connectionSource, InfractionCommon.class, true);
            TableUtils.dropTable(connectionSource, InfractionImpound.class, true);
            TableUtils.dropTable(connectionSource, InfractionTow.class, true);
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Device.class, true);
            TableUtils.dropTable(connectionSource, DeviceCategory.class, true);
            TableUtils.dropTable(connectionSource, MonitoringLog.class, true);
            TableUtils.dropTable(connectionSource, PartialMonitoringLog.class, true);

            TableUtils.createTable(connectionSource, Article.class);
            TableUtils.createTable(connectionSource, City.class);
            TableUtils.createTable(connectionSource, State.class);
            TableUtils.createTable(connectionSource, Country.class);
            TableUtils.createTable(connectionSource, Project.class);
            TableUtils.createTable(connectionSource, Address.class);
            TableUtils.createTable(connectionSource, DocumentType.class);
            TableUtils.createTable(connectionSource, InfractionType.class);
            TableUtils.createTable(connectionSource, Location.class);
            TableUtils.createTable(connectionSource, Offender.class);
            TableUtils.createTable(connectionSource, OffenderDocument.class);
            TableUtils.createTable(connectionSource, Image.class);
            TableUtils.createTable(connectionSource, Impound.class);
            TableUtils.createTable(connectionSource, Infraction.class);
            TableUtils.createTable(connectionSource, InfractionAlcoholimeter.class);
            TableUtils.createTable(connectionSource, InfractionCommon.class);
            TableUtils.createTable(connectionSource, InfractionImpound.class);
            TableUtils.createTable(connectionSource, InfractionTow.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Device.class);
            TableUtils.createTable(connectionSource, DeviceCategory.class);
            TableUtils.createTable(connectionSource, MonitoringLog.class);
            TableUtils.createTable(connectionSource, PartialMonitoringLog.class);

            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
    }
}
