package com.zhangteng.baselibrary

import com.zhangteng.utils.StateViewHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class MainModule {

    @Provides
    fun provideStateViewHelper(): StateViewHelper {
        return StateViewHelper()
    }

}