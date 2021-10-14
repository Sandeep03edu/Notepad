package com.example.notepad;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;
    public abstract NoteDao noteDao();
    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).BackgroundTask();
        }
    };

    private static class PopulateDbAsyncTask{

        private NoteDao noteDao;

        public PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }

        public void BackgroundTask() {
            new NoteDatabaseAsyncTask(){
                @Override
                public void doInBackground() {
//                    noteDao.insert(new Note("Title 1", "Description 1", 1));
//                    noteDao.insert(new Note("Title 2", "Description 2", 2));
//                    noteDao.insert(new Note("Title 3", "Description 3", 3));
                    super.doInBackground();
                }
            }.execute();
        }

    }

    public abstract static class NoteDatabaseAsyncTask {

        public NoteDatabaseAsyncTask() {  }

        public void startBackground() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doInBackground();
                    // We don't need to perform postExecute task
//                    activity.runOnUiThread(() -> onPostExecute());
                }
            }).start();
        }

        public void execute() {
            startBackground();
        }

        public void doInBackground() {}

        public void onPostExecute() {}

    }

}