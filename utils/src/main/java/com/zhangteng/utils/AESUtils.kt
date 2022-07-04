package com.zhangteng.utils

import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Swing on 2017/12/6.
 */
object AESUtils {
    /**
     * description 随机生成AES秘钥
     */
    val key: String
        get() = try {
            val kg = KeyGenerator.getInstance("AES")
            kg.init(128)
            val sk = kg.generateKey()
            val b = sk.encoded
            byteToHexString(b)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }

    /**
     * description 使用指定的字符串生成AES秘钥
     * @param keyRaw 任意字符串
     */
    fun getKeyByPass(keyRaw: String): String {
        return try {
            val kg = KeyGenerator.getInstance("AES")
            // kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
            //SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以生成的秘钥就一样。
            kg.init(128, SecureRandom(keyRaw.toByteArray()))
            val sk = kg.generateKey()
            val b = sk.encoded
            byteToHexString(b)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * byte数组转化为16进制字符串
     *
     * @param bytes
     * @return 编码后的字符串
     */
    fun byteToHexString(bytes: ByteArray): String {
        val sb = StringBuffer()
        for (i in bytes.indices) {
            val strHex = Integer.toHexString(bytes[i].toInt())
            if (strHex.length > 3) {
                sb.append(strHex.substring(6))
            } else {
                if (strHex.length < 2) {
                    sb.append("0$strHex")
                } else {
                    sb.append(strHex)
                }
            }
        }
        return sb.toString()
    }

    /**
     * description AES加密，模式AES/CBC/NoPadding
     * @param data 原文
     * @param key 秘钥
     * @param iv 盐，任意16位
     * @return 密文
     */
    @Throws(Exception::class)
    fun encrypt(data: String, key: String, iv: String): String {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val blockSize = cipher.blockSize
        val dataBytes = data.toByteArray()
        var plaintextLength = dataBytes.size
        if (plaintextLength % blockSize != 0) {
            plaintextLength += (blockSize - plaintextLength % blockSize)
        }
        val plaintext = ByteArray(plaintextLength)
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.size)
        val keySpec = SecretKeySpec(key.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(iv.toByteArray())
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        val encrypted = cipher.doFinal(plaintext)
        return Base64Utils.encode(encrypted)
    }

    /**
     * description AES解密，模式AES/CBC/NoPadding
     * @param data 原文
     * @param key 秘钥
     * @param iv 盐，任意16位
     * @return 密文
     */
    @Throws(Exception::class)
    fun decrypt(data: String, key: String, iv: String): String {
        val encrypted1 = Base64Utils.decode(data)
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val keySpec = SecretKeySpec(key.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(iv.toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val original = cipher.doFinal(encrypted1)
        return String(original)
    }
}