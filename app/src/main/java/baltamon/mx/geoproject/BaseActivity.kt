package baltamon.mx.geoproject

import android.support.v7.app.AppCompatActivity

/**
 * @author Alejandro Barba on 4/30/18.
 */
open class BaseActivity : AppCompatActivity() {
    fun getViewComponent() : ViewComponent = (application as GeoApplication).viewComponent
}