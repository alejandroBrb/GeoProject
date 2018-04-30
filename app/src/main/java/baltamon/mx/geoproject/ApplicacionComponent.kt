package baltamon.mx.geoproject

import android.content.Context
import dagger.Component

/**
 * @author Alejandro Barba on 4/28/18.
 */
@ApplicationScope
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicacionComponent {
    fun exposeContext(): Context
}