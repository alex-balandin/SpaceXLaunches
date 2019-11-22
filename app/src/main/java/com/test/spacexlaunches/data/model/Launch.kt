package com.test.spacexlaunches.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.test.spacexlaunches.data.db.Converters
import com.test.spacexlaunches.data.api.response.RawLaunch

/**
 * Created by alex-balandin on 2019-11-21
 */
@Entity(
    tableName = "launches"
)
data class Launch(

    @PrimaryKey
    @ColumnInfo(name = "flightNumber")
    val flightNumber: Int,

    @ColumnInfo(name = "missionName")
    val missionName: String? = null,

    @ColumnInfo(name = "upcoming")
    val upcoming: Boolean,

    @ColumnInfo(name = "launchDateUnix")
    val launchDateUnix: Long? = null,

    @ColumnInfo(name = "rocketName")
    val rocketName: String? = null,

    @ColumnInfo(name = "details")
    val details: String? = null,

    @ColumnInfo(name = "missionPatch")
    val missionPatch: String? = null,

    @ColumnInfo(name = "missionPatchSmall")
    val missionPatchSmall: String? = null,

    @ColumnInfo(name = "articleLink")
    val articleLink: String? = null,

    @ColumnInfo(name = "flickrImages")
    val flickrImages: List<String>? = null
) {
    constructor(rawLaunch: RawLaunch) : this(
        rawLaunch.flightNumber,
        rawLaunch.missionName,
        rawLaunch.upcoming,
        rawLaunch.launchDateUnix,
        rawLaunch.rocket?.rocketName,
        rawLaunch.details,
        rawLaunch.links?.missionPatch,
        rawLaunch.links?.missionPatchSmall,
        rawLaunch.links?.articleLink,
        rawLaunch.links?.flickrImages
    )

}