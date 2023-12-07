package io.statustracker.trackable

import io.statustracker.config.CacheFactory
import kotlinx.serialization.json.Json
import redis.clients.jedis.params.SetParams
import java.util.UUID

const val TRACKABLE_NAMESPACE = "trackable"

class CacheService {
    fun setTrackable(trackable: Trackable) {
        CacheFactory.client().use { client ->
            try {
                val serializedStatus = Json.encodeToString(Trackable.serializer(), trackable)
                val ttl = if(trackable.status.next == null) {
                    trackable.endTTL
                } else {
                    trackable.deadTTL
                }

                client.set("${TRACKABLE_NAMESPACE}:${trackable.id}", serializedStatus, SetParams.setParams().ex(ttl.toLong()))
            } catch (e: Exception) {
                throw TrackableException("Problem caching trackable", e)
            }
        }
    }

    fun getTrackable(trackableId: UUID): Trackable {
        try {
            CacheFactory.client().use { client ->
                val serializedTrackable = client.get("${TRACKABLE_NAMESPACE}:${trackableId}")
                return Json.decodeFromString(serializedTrackable)
            }
        } catch (e: IllegalStateException) {
            throw TrackableNotFoundException("Unable to find trackable with id: $trackableId", e)
        } catch (e: Exception) {
            throw TrackableException("Problem fetching trackable with id: $trackableId", e)
        }
    }
}