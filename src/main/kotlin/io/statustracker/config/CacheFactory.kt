package io.statustracker.config

import io.statustracker.ApplicationException
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool

object CacheFactory {
    private lateinit var jedisPool: JedisPool
    fun init(host: String, port: Int) {
        this.jedisPool = JedisPool(host, port)
    }

    fun client(): Jedis {
        return try {
            this.jedisPool.resource
        } catch (e: Exception) {
            throw ApplicationException("Unable to obtain jedis resource", e)
        }
    }
}