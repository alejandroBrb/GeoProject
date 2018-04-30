package baltamon.mx.geoproject

import android.app.Application

/**
 * @author Alejandro Barba on 4/28/18.
 */
class GeoApplication : Application() {

    lateinit var viewComponent: ViewComponent

    override fun onCreate() {
        super.onCreate()
        initializeDaggerComponents()
    }

    private fun initializeDaggerComponents() {
        val applicationComponent = DaggerApplicacionComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()

        viewComponent = DaggerViewComponent.builder()
                .applicacionComponent(applicationComponent)
                .build()
    }

}