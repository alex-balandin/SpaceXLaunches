package com.test.spacexlaunches.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.spacexlaunches.data.model.Launch
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by alex-balandin on 2019-11-21
 */
@Dao
interface LaunchesDao {

    @Query("SELECT * FROM launches ORDER BY flightNumber")
    fun queryAllLaunches(): Single<List<Launch>>

    @Query("SELECT * FROM launches  WHERE flightNumber = :flightNumber")
    fun queryLaunch(flightNumber: Int): Single<Launch>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    fun insertAllAllLaunches(launches: List<Launch>): Completable

    @Query("DELETE FROM launches")
    fun deleteAllLaunches(): Completable
}