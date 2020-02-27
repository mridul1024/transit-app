package com.zuk0.gaijinsmash.riderz.di.module.activity.fragment

import android.content.Context
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.BsaDao
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.EtdDao
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.StationDao
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.TripDao
import com.zuk0.gaijinsmash.riderz.data.local.room.database.BsaDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.EtdDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.StationDatabase
import com.zuk0.gaijinsmash.riderz.data.local.room.database.TripDatabase
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

/**
 * place all fragment scoped injections here
 */
@Module
class FragmentModule {

}