package com.zhangteng.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MD5Util {
    /**
     * 32位MD5加密
     */
    fun md5Decode32(content: String): String {
        val hash: ByteArray
        hash = try {
            MessageDigest.getInstance("MD5").digest(content.toByteArray(StandardCharsets.UTF_8))
        } catch (e: NoSuchAlgorithmException) {
            return ""
        }
        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            if (b.toInt() and 0xFF < 0x10) {
                hex.append("0")
            }
            hex.append(Integer.toHexString(b.toInt() and 0xFF))
        }
        return hex.toString()
    }
}