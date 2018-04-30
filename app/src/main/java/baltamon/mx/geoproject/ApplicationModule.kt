package baltamon.mx.geoproject

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * @author Alejandro Barba on 4/28/18.
 */
@Module
class ApplicationModule constructor(private val mApplication: Application){

    @Provides
    @ApplicationScope
    fun provideContext(): Context {
        return mApplication
    }

    @Provides
    @ApplicationScope
    fun provideApplication(): Application{
        return mApplication
    }
}