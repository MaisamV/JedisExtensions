package com.mvs.jedis

import redis.clients.jedis.JedisPool
import java.util.concurrent.ConcurrentHashMap

object JedisPoolManager {

    private val poolLock: Any = Object()
    private var poolMap: ConcurrentHashMap<RedisAddress, JedisPool> = ConcurrentHashMap()

    fun getPool(redisAddress: RedisAddress): JedisPool {
        if (poolMap[redisAddress] == null) {
            init(redisAddress)
        }
        return poolMap[redisAddress]!!
    }

    fun init(
        redisAddress: RedisAddress,
        maxConnections: Int = 1000,
        testOnBorrow: Boolean = true,
        timeoutMillis: Int = 1000,
        testOnCreate: Boolean = false
    ) {
        synchronized(poolLock) {
            if (poolMap[redisAddress] == null) {
                poolMap[redisAddress] = createJedisPool(
                    redisAddress.redisServerAddress, redisAddress.redisServerPort,
                    maxConnections, testOnBorrow, timeoutMillis, testOnCreate
                )
            }
        }
    }

    fun release(
        redisAddress: RedisAddress
    ): Boolean {
        try {
            poolMap.remove(redisAddress)?.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}
