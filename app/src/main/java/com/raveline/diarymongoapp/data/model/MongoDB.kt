package com.raveline.diarymongoapp.data.model

import com.raveline.diarymongoapp.common.utlis.Constants
import com.raveline.diarymongoapp.data.repository.MongoRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration

object MongoDB : MongoRepository {
    private val app = App.create(Constants.MONGO_API_KEY)
    private val user = app.currentUser
    private lateinit var realm: Realm
    override fun configureRealDatabase() {
        if (user != null) {
            val config = SyncConfiguration.Builder(
                user = user,
                setOf(DiaryModel::class),
            ).initialSubscriptions { subscription ->
                add(
                    query = subscription.query<DiaryModel>("ownerId == $0", user.id),
                    name = "User's Diaries"
                )
            }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
        }
    }
}