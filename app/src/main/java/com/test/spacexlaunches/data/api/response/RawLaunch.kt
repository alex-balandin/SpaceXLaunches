package com.test.spacexlaunches.data.api.response


import com.google.gson.annotations.SerializedName

data class RawLaunch(
    @SerializedName("flight_number")
    val flightNumber: Int,
    @SerializedName("mission_name")
    val missionName: String? = null,
    @SerializedName("mission_id")
    val missionId: List<Any?>? = null,
    @SerializedName("upcoming")
    val upcoming: Boolean? = null,
    @SerializedName("launch_year")
    val launchYear: String? = null,
    @SerializedName("launch_date_unix")
    val launchDateUnix: Int? = null,
    @SerializedName("launch_date_utc")
    val launchDateUtc: String? = null,
    @SerializedName("launch_date_local")
    val launchDateLocal: String? = null,
    @SerializedName("is_tentative")
    val isTentative: Boolean? = null,
    @SerializedName("tentative_max_precision")
    val tentativeMaxPrecision: String? = null,
    @SerializedName("tbd")
    val tbd: Boolean? = null,
    @SerializedName("launch_window")
    val launchWindow: Int? = null,
    @SerializedName("rocket")
    val rocket: Rocket? = null,
    @SerializedName("ships")
    val ships: List<Any?>? = null,
    @SerializedName("telemetry")
    val telemetry: Telemetry? = null,
    @SerializedName("launch_site")
    val launchSite: LaunchSite? = null,
    @SerializedName("launch_success")
    val launchSuccess: Boolean? = null,
    @SerializedName("launch_failure_details")
    val launchFailureDetails: LaunchFailureDetails? = null,
    @SerializedName("links")
    val links: Links? = null,
    @SerializedName("details")
    val details: String? = null,
    @SerializedName("static_fire_date_utc")
    val staticFireDateUtc: String? = null,
    @SerializedName("static_fire_date_unix")
    val staticFireDateUnix: Int? = null,
    @SerializedName("timeline")
    val timeline: Timeline? = null,
    @SerializedName("last_date_update")
    val lastDateUpdate: String? = null,
    @SerializedName("last_ll_launch_date")
    val lastLlLaunchDate: String? = null,
    @SerializedName("last_ll_update")
    val lastLlUpdate: String? = null,
    @SerializedName("last_wiki_launch_date")
    val lastWikiLaunchDate: String? = null,
    @SerializedName("last_wiki_revision")
    val lastWikiRevision: String? = null,
    @SerializedName("last_wiki_update")
    val lastWikiUpdate: String? = null,
    @SerializedName("launch_date_source")
    val launchDateSource: String? = null,
    @SerializedName("crew")
    val crew: List<Any?>? = null
) {
    data class Rocket(
        @SerializedName("rocket_id")
        val rocketId: String? = null,
        @SerializedName("rocket_name")
        val rocketName: String? = null,
        @SerializedName("rocket_type")
        val rocketType: String? = null,
        @SerializedName("first_stage")
        val firstStage: FirstStage? = null,
        @SerializedName("second_stage")
        val secondStage: SecondStage? = null,
        @SerializedName("fairings")
        val fairings: Fairings? = null
    ) {
        data class FirstStage(
            @SerializedName("cores")
            val cores: List<Core?>? = null
        ) {
            data class Core(
                @SerializedName("block")
                val block: Int? = null,
                @SerializedName("reused")
                val reused: Boolean? = null,
                @SerializedName("core_serial")
                val coreSerial: Any? = null,
                @SerializedName("flight")
                val flight: Any? = null,
                @SerializedName("gridfins")
                val gridfins: Any? = null,
                @SerializedName("legs")
                val legs: Any? = null,
                @SerializedName("land_success")
                val landSuccess: Any? = null,
                @SerializedName("landing_intent")
                val landingIntent: Any? = null,
                @SerializedName("landing_type")
                val landingType: Any? = null,
                @SerializedName("landing_vehicle")
                val landingVehicle: Any? = null
            )
        }

        data class SecondStage(
            @SerializedName("block")
            val block: Int? = null,
            @SerializedName("payloads")
            val payloads: List<Payload?>? = null
        ) {
            data class Payload(
                @SerializedName("payload_id")
                val payloadId: String? = null,
                @SerializedName("norad_id")
                val noradId: List<Any?>? = null,
                @SerializedName("reused")
                val reused: Boolean? = null,
                @SerializedName("customers")
                val customers: List<String?>? = null,
                @SerializedName("nationality")
                val nationality: String? = null,
                @SerializedName("manufacturer")
                val manufacturer: String? = null,
                @SerializedName("payload_type")
                val payloadType: String? = null,
                @SerializedName("orbit")
                val orbit: String? = null,
                @SerializedName("orbit_params")
                val orbitParams: OrbitParams? = null,
                @SerializedName("uid")
                val uid: String? = null,
                @SerializedName("payload_mass_kg")
                val payloadMassKg: Any? = null,
                @SerializedName("payload_mass_lbs")
                val payloadMassLbs: Any? = null
            ) {
                data class OrbitParams(
                    @SerializedName("reference_system")
                    val referenceSystem: String? = null,
                    @SerializedName("regime")
                    val regime: String? = null,
                    @SerializedName("longitude")
                    val longitude: Any? = null,
                    @SerializedName("semi_major_axis_km")
                    val semiMajorAxisKm: Any? = null,
                    @SerializedName("eccentricity")
                    val eccentricity: Any? = null,
                    @SerializedName("periapsis_km")
                    val periapsisKm: Any? = null,
                    @SerializedName("apoapsis_km")
                    val apoapsisKm: Any? = null,
                    @SerializedName("inclination_deg")
                    val inclinationDeg: Any? = null,
                    @SerializedName("period_min")
                    val periodMin: Any? = null,
                    @SerializedName("lifespan_years")
                    val lifespanYears: Any? = null,
                    @SerializedName("epoch")
                    val epoch: Any? = null,
                    @SerializedName("mean_motion")
                    val meanMotion: Any? = null,
                    @SerializedName("raan")
                    val raan: Any? = null,
                    @SerializedName("arg_of_pericenter")
                    val argOfPericenter: Any? = null,
                    @SerializedName("mean_anomaly")
                    val meanAnomaly: Any? = null
                )
            }
        }

        data class Fairings(
            @SerializedName("reused")
            val reused: Boolean? = null,
            @SerializedName("recovery_attempt")
            val recoveryAttempt: Boolean? = null,
            @SerializedName("recovered")
            val recovered: Boolean? = null,
            @SerializedName("ship")
            val ship: Any? = null
        )
    }

    data class Telemetry(
        @SerializedName("flight_club")
        val flightClub: Any? = null
    )

    data class LaunchSite(
        @SerializedName("site_id")
        val siteId: String? = null,
        @SerializedName("site_name")
        val siteName: String? = null,
        @SerializedName("site_name_long")
        val siteNameLong: String? = null
    )

    data class LaunchFailureDetails(
        @SerializedName("time")
        val time: Int? = null,
        @SerializedName("altitude")
        val altitude: Int? = null,
        @SerializedName("reason")
        val reason: String? = null
    )

    data class Links(
        @SerializedName("mission_patch")
        val missionPatch: String? = null,
        @SerializedName("mission_patch_small")
        val missionPatchSmall: String? = null,
        @SerializedName("reddit_campaign")
        val redditCampaign: String? = null,
        @SerializedName("reddit_launch")
        val redditLaunch: String? = null,
        @SerializedName("reddit_recovery")
        val redditRecovery: String? = null,
        @SerializedName("reddit_media")
        val redditMedia: String? = null,
        @SerializedName("presskit")
        val presskit: String? = null,
        @SerializedName("article_link")
        val articleLink: String? = null,
        @SerializedName("wikipedia")
        val wikipedia: String? = null,
        @SerializedName("video_link")
        val videoLink: String? = null,
        @SerializedName("youtube_id")
        val youtubeId: String? = null,
        @SerializedName("flickr_images")
        val flickrImages: List<String>? = null
    )

    data class Timeline(
        @SerializedName("webcast_liftoff")
        val webcastLiftoff: Any? = null,
        @SerializedName("go_for_prop_loading")
        val goForPropLoading: Int? = null,
        @SerializedName("stage1_rp1_loading")
        val stage1Rp1Loading: Int? = null,
        @SerializedName("stage1_lox_loading")
        val stage1LoxLoading: Int? = null,
        @SerializedName("stage2_rp1_loading")
        val stage2Rp1Loading: Int? = null,
        @SerializedName("stage2_lox_loading")
        val stage2LoxLoading: Int? = null,
        @SerializedName("engine_chill")
        val engineChill: Int? = null,
        @SerializedName("prelaunch_checks")
        val prelaunchChecks: Int? = null,
        @SerializedName("propellant_pressurization")
        val propellantPressurization: Int? = null,
        @SerializedName("go_for_launch")
        val goForLaunch: Int? = null,
        @SerializedName("ignition")
        val ignition: Int? = null,
        @SerializedName("liftoff")
        val liftoff: Int? = null,
        @SerializedName("maxq")
        val maxq: Int? = null,
        @SerializedName("beco")
        val beco: Int? = null,
        @SerializedName("side_core_sep")
        val sideCoreSep: Int? = null,
        @SerializedName("side_core_boostback")
        val sideCoreBoostback: Int? = null,
        @SerializedName("meco")
        val meco: Int? = null,
        @SerializedName("center_stage_sep")
        val centerStageSep: Int? = null,
        @SerializedName("second_stage_ignition")
        val secondStageIgnition: Int? = null,
        @SerializedName("fairing_deploy")
        val fairingDeploy: Int? = null,
        @SerializedName("side_core_entry_burn")
        val sideCoreEntryBurn: Int? = null,
        @SerializedName("seco-1")
        val seco1: Int? = null,
        @SerializedName("side_core_landing")
        val sideCoreLanding: Int? = null,
        @SerializedName("center_core_entry_burn")
        val centerCoreEntryBurn: Int? = null,
        @SerializedName("center_core_landing")
        val centerCoreLanding: Int? = null,
        @SerializedName("payload_deploy")
        val payloadDeploy: Int? = null,
        @SerializedName("second_stage_restart")
        val secondStageRestart: Int? = null,
        @SerializedName("seco-2")
        val seco2: Int? = null,
        @SerializedName("seco-3")
        val seco3: Int? = null,
        @SerializedName("seco-4")
        val seco4: Int? = null
    )
}