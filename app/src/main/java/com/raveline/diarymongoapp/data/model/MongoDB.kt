package com.raveline.diarymongoapp.data.model

import android.util.Log
import com.raveline.diarymongoapp.common.utlis.Constants.MONGO_API_KEY
import com.raveline.diarymongoapp.data.stateModel.RequestState
import com.raveline.diarymongoapp.common.utlis.toInstant
import com.raveline.diarymongoapp.data.repository.Diaries
import com.raveline.diarymongoapp.data.repository.MongoRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.mongodb.syncSession
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.ZoneId

object MongoDB : MongoRepository {
    private val TAG: String = MongoDB::class.simpleName.toString()
    private val app = App.create(MONGO_API_KEY)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureRealmDatabase()
    }

    override fun configureRealmDatabase() {
        if (user != null) {
            val config = SyncConfiguration.Builder(
                user = user,
                setOf(DiaryModel::class),
            )
                .log(LogLevel.INFO)
                .initialSubscriptions { subscription ->
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

    override fun getAllDiaries(): Flow<Diaries> {

        return if (user != null) {

            try {
                Log.d(
                    TAG,
                    "getAllDiaries: ${realm.syncSession.user.id} ${realm.syncSession.connectionState.name}"
                )
                realm.query<DiaryModel>("ownerId == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->

                        RequestState.Success(
                            data = result.list.groupBy { diary ->
                                diary.date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                        )

                    }
            } catch (e: Exception) {
                flow {
                    emit(RequestState.Error(UserNotAuthenticatedException("All Diaries Exception: ${e.message}")))
                }
            }
        } else {
            flow {
                emit(RequestState.Error(UserNotAuthenticatedException("Nothing to show here")))
            }
        }
    }

    override fun getSelectedDiary(diaryId: ObjectId): Flow<RequestState<DiaryModel>> {
        return if (user != null) {
            try {
                realm.query<DiaryModel>(query = "_id == $0", diaryId).asFlow().map {
                    RequestState.Success(data = it.list.first())
                }

            } catch (e: Exception) {
                flow {
                    emit(
                        RequestState.Error(e)
                    )
                }
            }
        } else {
            flow {
                emit(
                    RequestState.Error(UserNotAuthenticatedException())
                )
            }
        }
    }

    override suspend fun insertDiary(diaryModel: DiaryModel): RequestState<DiaryModel> {
        return if (user != null) {
            try {
                val addedDiary = realm.write {
                    //Setting the ownerId into the diary
                    this.copyToRealm(
                        diaryModel.apply {
                            ownerId = user.id
                        }
                    )
                }
                RequestState.Success(data = addedDiary)
            } catch (e: Exception) {
                RequestState.Error(e)
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException("Wasn't possible to add this new diary"))
        }
    }

    override suspend fun deleteDiary(id: ObjectId): RequestState<DiaryModel> {
        return if (user != null) {

            realm.write {

                val diary = query<DiaryModel>(
                    query = "_id == $0 AND ownerId == $1", id, user.id
                ).first().find()

                if (diary != null) {
                    try {

                        delete(diary)
                        RequestState.Success(data = diary)

                    } catch (e: Exception) {
                        RequestState.Error(e)
                    }
                } else {
                    RequestState.Error(UserNotAuthenticatedException("Wasn't possible to update this diary. It doest exists."))
                }

            }

        } else {
            RequestState.Error(UserNotAuthenticatedException("Wasn't possible to update this diary"))
        }
    }

    override suspend fun updateDiary(diaryModel: DiaryModel): RequestState<DiaryModel> {
        return if (user != null) {
            try {
                realm.write {
                    val queriedDiary = query<DiaryModel>(
                        query = "_id == $0", diaryModel._id
                    ).first().find()

                    if (queriedDiary != null) {
                        queriedDiary.title = diaryModel.title
                        queriedDiary.description = diaryModel.description
                        queriedDiary.images = diaryModel.images
                        queriedDiary.mood = diaryModel.mood
                        queriedDiary.date = diaryModel.date

                        RequestState.Success(data = queriedDiary)
                    } else {
                        RequestState.Error(error = Exception("Diary doesn't exists"))
                    }
                }

            } catch (e: Exception) {
                RequestState.Error(e)
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException("Wasn't possible to update this diary"))
        }
    }
}

private class UserNotAuthenticatedException(msg: String? = "User is not Logged in.") :
    Exception(msg)









