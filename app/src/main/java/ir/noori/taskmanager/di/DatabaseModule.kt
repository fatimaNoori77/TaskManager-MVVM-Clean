package ir.noori.taskmanager.di;

import android.content.Context;
import androidx.room.Room;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import ir.noori.taskmanager.data.local.dao.TaskDao;
import ir.noori.taskmanager.data.local.database.AppDatabase;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public static AppDatabase provideDatabase(@ApplicationContext Context appContext) {
        return Room.databaseBuilder(
                appContext,
                AppDatabase.class,
                "task_db"
        ).build();
    }

    @Provides
    public static TaskDao provideTaskDao(AppDatabase db) {
        return db.taskDao();
    }
}