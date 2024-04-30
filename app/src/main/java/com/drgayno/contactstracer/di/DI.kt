package com.drgayno.contactstracer.di

import android.app.AlarmManager
import android.app.Application
import android.bluetooth.BluetoothManager
import android.os.PowerManager
import androidx.core.content.getSystemService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.room.Room
import com.drgayno.contactstracer.db.*
import com.drgayno.contactstracer.receiver.BatterSaverStateReceiver
import com.drgayno.contactstracer.receiver.BluetoothStateReceiver
import com.drgayno.contactstracer.receiver.LocationStateReceiver
import com.drgayno.contactstracer.ui.MainViewModel
import com.drgayno.contactstracer.bt.BluetoothRepository
import com.drgayno.contactstracer.db.export.CsvExporter
import com.drgayno.contactstracer.ui.confirm.ConfirmationViewModel
import com.drgayno.contactstracer.ui.dashboard.DashboardViewModel
import com.drgayno.contactstracer.ui.data.DataViewModel
import com.drgayno.contactstracer.ui.help.BatteryOptViewModel
import com.drgayno.contactstracer.ui.login.LoginViewModel
import com.drgayno.contactstracer.ui.onboarding.OnboardingViewModel
import com.drgayno.contactstracer.ui.permissions.PermissionDisabledViewModel
import com.drgayno.contactstracer.ui.permissions.onboarding.OnboardingPermissionViewModel
import com.drgayno.contactstracer.ui.sandbox.SandboxViewModel
import com.drgayno.contactstracer.ui.success.SuccessViewModel
import com.drgayno.contactstracer.ui.surveykit.ui.AssessmentViewModel
import com.drgayno.contactstracer.util.CustomTabHelper
import com.drgayno.contactstracer.util.DeviceInfo
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { SandboxViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), androidContext()) }
    viewModel { OnboardingViewModel(androidApplication(), get()) }
    viewModel { DashboardViewModel(get(), get()) }
    viewModel { OnboardingPermissionViewModel(get(), androidApplication()) }
    viewModel { PermissionDisabledViewModel(get(), androidApplication()) }
    viewModel { DataViewModel(get(), get()) }
    viewModel { ConfirmationViewModel(get(), get(), get()) }
    viewModel { SuccessViewModel() }
    viewModel { BatteryOptViewModel() }
    viewModel { AssessmentViewModel() }
}

val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(database: AppDatabase): ScanDataDao {
        return database.scanResultsDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
    single { CsvExporter(get()) }
}

val repositoryModule = module {
    fun provideDatabaseRepository(deviceDao: ScanDataDao): DatabaseRepository {
        return ExpositionRepositoryImpl(deviceDao)
    }
    single { provideDatabaseRepository(get()) }
    single { BluetoothRepository(get(), get(), get(), get()) }
    single { SharedPrefsRepository(get()) }
}

val appModule = module {
    single { LocationStateReceiver() }
    single { BluetoothStateReceiver() }
    single { BatterSaverStateReceiver() }
    single { LocalBroadcastManager.getInstance(androidApplication()) }
    single { androidContext().getSystemService<PowerManager>() }
    single { androidContext().getSystemService<BluetoothManager>() }
    single { androidContext().getSystemService<AlarmManager>() }
    single { DeviceInfo(androidContext()) }
    single { CustomTabHelper(androidContext()) }
}

val allModules = listOf(
    appModule,
    viewModelModule,
    databaseModule,
    repositoryModule
)
