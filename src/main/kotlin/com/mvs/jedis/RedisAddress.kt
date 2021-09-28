package com.mvs.jedis

class RedisAddress(val redisServerAddress: String, val redisServerPort: Int) {
    val address = "$redisServerAddress:$redisServerPort"

    override fun toString(): String {
        return address
    }

    override fun hashCode(): Int {
        return 7591 + address.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is RedisAddress) return false
        return this.address.equals(other.address)
    }
}
