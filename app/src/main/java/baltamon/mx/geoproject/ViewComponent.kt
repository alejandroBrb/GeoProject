package baltamon.mx.geoproject

import baltamon.mx.geoproject.main_activity.MainActivityKt
import dagger.Component

/**
 * @author Alejandro Barba on 4/30/18.
 */
@ViewScope
@Component(dependencies = arrayOf(ApplicacionComponent::class))
interface ViewComponent {
    fun inject(mainActivityView: MainActivityKt)
}