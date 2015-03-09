package me.jlhp.sivale.database;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;

import me.jlhp.sivale.model.client.Card;
import me.jlhp.sivale.model.client.Transaction;

/**
 * Created by JOSELUIS on 3/9/14.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    public static void main(String[] args) throws Exception {
        File file = new File("app/src/main/res/raw", "ormlite_config.txt");

        writeConfigFile(file, new Class[]{
                Card.class,
                Transaction.class
        });
    }
}