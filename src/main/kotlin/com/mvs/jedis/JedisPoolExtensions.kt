package com.mvs.jedis

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.Pipeline
import java.lang.StringBuilder

fun createJedisPool(
    remoteServer: String,
    remotePort: Int,
    maxConnections: Int = 1000,
    testOnBorrow: Boolean = true,
    timeoutMillis: Int = 1000,
    testOnCreate: Boolean = false
): JedisPool {
    val poolConfig = JedisPoolConfig()
    poolConfig.testOnBorrow = testOnBorrow
    poolConfig.testOnReturn = false
    poolConfig.testOnCreate = testOnCreate
    poolConfig.maxTotal = maxConnections
    poolConfig.blockWhenExhausted = false
    return JedisPool(poolConfig, remoteServer, remotePort, timeoutMillis)
}

fun <T> JedisPool.execute(block: (Jedis) -> T): T {
    return this.resource.execute(block)
}

fun <T> Jedis.execute(block: (Jedis) -> T): T {
    try {
        return block.invoke(this)
    } finally {
        this.close()
    }
}

fun JedisPool.pipeline(block: (Pipeline) -> Unit) {
    return this.resource.pipeline(block)
}

fun Jedis.pipeline(block: (Pipeline) -> Unit) {
    val pipeline = this.pipelined()
    try {
        block.invoke(pipeline)
    } finally {
        try { pipeline.sync() }
        finally { this.close() }
    }
}

fun JedisPool.executeSequential(block: (Pipeline) -> Unit) {
    this.resource.executeSequential(block)
}

fun Jedis.executeSequential(block: (Pipeline) -> Unit) {
    val pipeline = this.pipelined()
    try {
        pipeline.multi()
        block.invoke(pipeline)
        pipeline.exec()
    } catch (e: Exception) {
        ignoreEx { pipeline.discard() }
    } finally {
        try { pipeline.sync() }
        finally { this.close() }
    }
}

fun JedisPool.atomic(key: String, retrieveBlock: (Jedis) -> Unit, sequentialBlock: (Pipeline) -> Unit) {
    this.resource.atomic(key, retrieveBlock, sequentialBlock)
}

fun JedisPool.atomic(keyList: List<String>, retrieveBlock: (Jedis) -> Unit, sequentialBlock: (Pipeline) -> Unit) {
    this.resource.atomic(keyList, retrieveBlock, sequentialBlock)
}

fun Jedis.atomic(keyList: List<String>, retrieveBlock: (Jedis) -> Unit, sequentialBlock: (Pipeline) -> Unit) {
    val builder = StringBuilder()
    keyList.forEach { builder.append(it).append(' ') }
    this.atomic(builder.toString(), retrieveBlock, sequentialBlock)
}

fun Jedis.atomic(keys: String, retrieveBlock: (Jedis) -> Unit, sequentialBlock: (Pipeline) -> Unit) {
    var pipeline: Pipeline? = null
    try {
        this.watch(keys)
        retrieveBlock.invoke(this)
        pipeline = this.pipelined()
        pipeline.multi()
        sequentialBlock.invoke(pipeline)
        pipeline.exec()
    } catch (e: Exception) {
        ignoreEx { pipeline?.discard() }
    } finally {
        try { pipeline?.sync() }
        finally { this.close() }
    }
}

inline fun <T> ignoreEx(block: () -> T): T? {
    return ignoreEx(null, block)
}

inline fun <T> ignoreEx(default: T, block: () -> T): T {
    return try {
        block.invoke()
    } catch (ignore: Exception){
        default
    }
}
