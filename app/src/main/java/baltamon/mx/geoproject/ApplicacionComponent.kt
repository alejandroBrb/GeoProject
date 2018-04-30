package baltamon.mx.geoproject

import android.content.Context
import dagger.Component
import io.realm.Realm

/**
 * @author Alejandro Barba on 4/28/18.
 */
@ApplicationScope
@Component(modules = arrayOf(ApplicationModule::class, DatabaseModule::class))
interface ApplicacionComponent {
    fun exposeContext(): Context

    fun exposeRealm(): Realm
}