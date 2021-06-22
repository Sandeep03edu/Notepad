package com.example.notepad;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note... notes){
        new NoteRepositoryAsyncTask(){
            @Override
            public void doInBackground() {
                noteDao.insert(notes[0]);
                super.doInBackground();
            }
        }.execute();
    }
    
    public void update(Note... notes){
        new NoteRepositoryAsyncTask(){
            @Override
            public void doInBackground() {
                noteDao.update(notes[0]);
                super.doInBackground();
            }
        }.execute();
    }

    public void delete(Note... notes){
        new NoteRepositoryAsyncTask(){
            @Override
            public void doInBackground() {
                noteDao.delete(notes[0]);
                super.doInBackground();
            }
        }.execute();
    }

    public void deleteAllNodes(Note... notes){
        new NoteRepositoryAsyncTask(){
            @Override
            public void doInBackground() {
                noteDao.deleteAllNotes();
                super.doInBackground();
            }
        }.execute();
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    public abstract static class NoteRepositoryAsyncTask {
        public NoteRepositoryAsyncTask() {  }
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
