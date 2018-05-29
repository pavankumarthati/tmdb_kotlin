package com.careem.tmdb.careemapp

import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class MainScheduler


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoScheduler
