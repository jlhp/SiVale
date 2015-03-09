package me.jlhp.sivale.database;

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
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;

/**
 * Created by JOSELUIS on 3/9/14.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    public static void main(String[] args) throws Exception {
        File file = new File("autotraffic/src/main/res/raw", "ormlite_config.txt");

        writeConfigFile(file, new Class[]{
            Article.class,
            City.class,
            State.class,
            Country.class,
            Project.class,
            Address.class,
            DocumentType.class,
            Device.class,
            DeviceCategory.class,
            MonitoringLog.class,
            InfractionType.class,
            Location.class,
            Offender.class,
            OffenderDocument.class,
            Image.class,
            Impound.class,
            Infraction.class,
            InfractionAlcoholimeter.class,
            InfractionCommon.class,
            InfractionImpound.class,
            InfractionTow.class,
            User.class,
            PartialMonitoringLog.class
        });
    }
}