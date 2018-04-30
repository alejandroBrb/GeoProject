package baltamon.mx.geoproject

import android.content.Context
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * @author Alejandro Barba on 4/30/18.
 */
@Module
class DatabaseModule {

    @Provides
    @ApplicationScope
    fun provideRealmConfiguration(context: Context): RealmConfiguration {
        Realm.init(context)
        return RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .name("geoprojectdb.realm")
                .build()
    }

    @Provides
    @ApplicationScope
    fun provideRealm(realmConfiguration: RealmConfiguration): Realm {
        return Realm.getInstance(realmConfiguration)
    }
}