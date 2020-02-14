package com.example.sapbusinessuser.model;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ScoreRealmManager {
    Realm realm;
    Context context;
    public ScoreRealmManager(Context context){
        this.context = context;
        Realm.init(context);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(configuration);
        realm = Realm.getDefaultInstance();
    }
    public void RecordScore(final Scores scores){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(scores);
            }
        });
    }

    public RealmResults<Scores> realmResults(){
        return realm.where(Scores.class).findAll();

    }
    public RealmResults<Scores> realmResults(String topicName){
        return realm.where(Scores.class).equalTo("topic_id",topicName).findAll();
    }

}