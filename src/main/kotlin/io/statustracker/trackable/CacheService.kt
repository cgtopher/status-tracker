package io.statustracker.trackable

import io.statustracker.config.CacheFactory
import kotlinx.serialization.json.Json
import redis.clients.jedis.params.SetParams
import java.util.UUID

const val TRACKABLE_NAMESPACE = "trackable"

class CacheService {
    fun setTrackable(trackable: Trackable) {
        CacheFactory.clent().use { client ->
            val serializedStatus = Json.encodeToString(Trackable.serializer(), trackable)
            val ttl = if(trackable.status.next == null) {
                trackable.endTTL
            } else {
                trackable.deadTTL
            }

            client.set("${TRACKABLE_NAMESPACE}:${trackable.id}", serializedStatus, SetParams.setParams().ex(ttl.toLong()))
        }
    }

    fun getTrackable(trackableId: UUID): Trackable {
        CacheFactory.clent().use { client ->
            val serializedTrackable = client.get("${TRACKABLE_NAMESPACE}:${trackableId}")
            client.disconnect()
            return Json.decodeFromString(serializedTrackable)
        }
    }
}