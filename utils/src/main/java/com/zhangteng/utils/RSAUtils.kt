package com.zhangteng.utils

import kotlin.Throws
import kotlin.jvm.Synchronized
import android.annotation.SuppressLint
import android.util.Base64
import java.lang.Exception
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.ArrayList
import javax.crypto.Cipher

/**
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br></br>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br></br>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 */
object RSAUtils {
    /**
     * 非对称加密密钥算法
     */
    const val RSA = "RSA"

    /**
     * 加密填充方式
     */
    const val ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding"

    /**
     * 秘钥默认长度
     */
    const val DEFAULT_KEY_SIZE = 1024

    /**
     * 当要加密的内容超过bufferSize，则采用partSplit进行分块加密
     */
    val DEFAULT_SPLIT = "#PART#".toByteArray()

    /**
     * 当前秘钥支持加密的最大字节数
     */
    const val DEFAULT_BUFFERSIZE = DEFAULT_KEY_SIZE / 8 - 11

    /**
     * 随机生成RSA密钥对
     *
     * @param keyLength 密钥长度，范围：512～2048
     * 一般1024
     * @return
     */
    fun generateRSAKeyPair(keyLength: Int): KeyPair? {
        return try {
            val kpg = KeyPairGenerator.getInstance(RSA)
            kpg.initialize(keyLength)
            kpg.genKeyPair()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
    }

    /**
     *
     *
     * 公钥加密
     *
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encryptByPublicKey(data: String, publicKey: String?): String {
        return String(
            Base64.encode(
                encryptByPublicKey(
                    data.toByteArray(),
                    Base64.decode(publicKey, 0)
                ), 0
            )
        ).replace("\n", "").replace("\\", "")
    }

    /**
     * 用公钥对字符串进行加密
     *
     * @param data 原文
     */
    @Throws(Exception::class)
    fun encryptByPublicKey(data: ByteArray?, publicKey: ByteArray?): ByteArray {
        // 得到公钥
        val keySpec = X509EncodedKeySpec(publicKey)
        val kf = KeyFactory.getInstance(RSA)
        val keyPublic = kf.generatePublic(keySpec)
        // 加密数据
        val cp = Cipher.getInstance(ECB_PKCS1_PADDING)
        cp.init(Cipher.ENCRYPT_MODE, keyPublic)
        return cp.doFinal(data)
    }

    /**
     *
     *
     * 私钥加密
     *
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encryptByPrivateKey(data: String, publicKey: String?): String {
        return String(
            Base64.encode(
                encryptByPrivateKey(
                    data.toByteArray(),
                    Base64.decode(publicKey, 0)
                ), 0
            )
        ).replace("\n", "").replace("\\", "")
    }

    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey 密钥
     * @return byte[] 加密数据
     */
    @Throws(Exception::class)
    fun encryptByPrivateKey(data: ByteArray?, privateKey: ByteArray?): ByteArray {
        // 得到私钥
        val keySpec = PKCS8EncodedKeySpec(privateKey)
        val kf = KeyFactory.getInstance(RSA)
        val keyPrivate = kf.generatePrivate(keySpec)
        // 数据加密
        val cipher = Cipher.getInstance(ECB_PKCS1_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, keyPrivate)
        return cipher.doFinal(data)
    }

    /**
     *
     *
     * 公钥解密
     *
     *
     * @param encryptedData 已加密数据(BASE64编码)
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decryptByPublicKey(encryptedData: String?, publicKey: String?): String {
        return String(
            decryptByPublicKey(
                Base64.decode(encryptedData, 0),
                Base64.decode(publicKey, 0)
            )
        )
    }

    /**
     * 公钥解密
     *
     * @param data      待解密数据
     * @param publicKey 密钥
     * @return byte[] 解密数据
     */
    @Throws(Exception::class)
    fun decryptByPublicKey(data: ByteArray?, publicKey: ByteArray?): ByteArray {
        // 得到公钥
        val keySpec = X509EncodedKeySpec(publicKey)
        val kf = KeyFactory.getInstance(RSA)
        val keyPublic = kf.generatePublic(keySpec)
        // 数据解密
        val cipher = Cipher.getInstance(ECB_PKCS1_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, keyPublic)
        return cipher.doFinal(data)
    }

    /**
     * <P>
     * 私钥解密
    </P> *
     *
     * @param encryptedData 已加密数据(BASE64编码)
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decryptByPrivateKey(encryptedData: String?, privateKey: String?): String {
        return String(
            decryptByPrivateKey(
                Base64.decode(encryptedData, 0),
                Base64.decode(privateKey, 0)
            )
        )
    }

    /**
     * 使用私钥进行解密
     */
    @Throws(Exception::class)
    fun decryptByPrivateKey(
        encrypted: ByteArray?,
        privateKey: ByteArray?
    ): ByteArray {
        // 得到私钥
        val keySpec =
            PKCS8EncodedKeySpec(privateKey)
        val kf =
            KeyFactory.getInstance(RSA)
        val keyPrivate = kf.generatePrivate(keySpec)

        // 解密数据
        val cp =
            Cipher.getInstance(ECB_PKCS1_PADDING)
        cp.init(Cipher.DECRYPT_MODE, keyPrivate)
        return cp.doFinal(encrypted)
    }

    /**
     * 用公钥对字符串进行分段加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encryptByPublicKeyForSpilt(data: String, publicKey: String?): String {
        return String(
            Base64.encode(
                encryptByPublicKeyForSpilt(
                    data.toByteArray(),
                    Base64.decode(publicKey, 0)
                ), 0
            )
        ).replace("\n", "").replace("\\", "")
    }

    /**
     * 用公钥对字符串进行分段加密
     */
    @Throws(Exception::class)
    fun encryptByPublicKeyForSpilt(data: ByteArray, publicKey: ByteArray?): ByteArray {
        val dataLen = data.size
        if (dataLen <= DEFAULT_BUFFERSIZE) {
            return encryptByPublicKey(data, publicKey)
        }
        val allBytes: MutableList<Byte> = ArrayList(2048)
        var bufIndex = 0
        var subDataLoop = 0
        var buf: ByteArray? = ByteArray(DEFAULT_BUFFERSIZE)
        for (i in 0 until dataLen) {
            buf!![bufIndex] = data[i]
            if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                subDataLoop++
                if (subDataLoop != 1) {
                    for (b in DEFAULT_SPLIT) {
                        allBytes.add(b)
                    }
                }
                val encryptBytes = encryptByPublicKey(buf, publicKey)
                for (b in encryptBytes) {
                    allBytes.add(b)
                }
                bufIndex = 0
                buf = if (i == dataLen - 1) {
                    null
                } else {
                    ByteArray(Math.min(DEFAULT_BUFFERSIZE, dataLen - i - 1))
                }
            }
        }
        val bytes = ByteArray(allBytes.size)
        run {
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
        }
        return bytes
    }

    /**
     *
     *
     * 私钥分段加密
     *
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encryptByPrivateKeyForSpilt(data: String, publicKey: String?): String {
        return String(
            Base64.encode(
                encryptByPrivateKeyForSpilt(
                    data.toByteArray(),
                    Base64.decode(publicKey, 0)
                ), 0
            )
        ).replace("\n", "").replace("\\", "")
    }

    /**
     * 分段加密
     *
     * @param data       要加密的原始数据
     * @param privateKey 秘钥
     */
    @Throws(Exception::class)
    fun encryptByPrivateKeyForSpilt(data: ByteArray, privateKey: ByteArray?): ByteArray {
        val dataLen = data.size
        if (dataLen <= DEFAULT_BUFFERSIZE) {
            return encryptByPrivateKey(data, privateKey)
        }
        val allBytes: MutableList<Byte> = ArrayList(2048)
        var bufIndex = 0
        var subDataLoop = 0
        var buf: ByteArray? = ByteArray(DEFAULT_BUFFERSIZE)
        for (i in 0 until dataLen) {
            buf!![bufIndex] = data[i]
            if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                subDataLoop++
                if (subDataLoop != 1) {
                    for (b in DEFAULT_SPLIT) {
                        allBytes.add(b)
                    }
                }
                val encryptBytes = encryptByPrivateKey(buf, privateKey)
                for (b in encryptBytes) {
                    allBytes.add(b)
                }
                bufIndex = 0
                buf = if (i == dataLen - 1) {
                    null
                } else {
                    ByteArray(Math.min(DEFAULT_BUFFERSIZE, dataLen - i - 1))
                }
            }
        }
        val bytes = ByteArray(allBytes.size)
        run {
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
        }
        return bytes
    }

    /**
     * 公钥分段解密
     *
     * @param encryptedData 已加密数据(BASE64编码)
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decryptByPublicKeyForSpilt(encryptedData: String?, publicKey: String?): String {
        return String(
            decryptByPublicKeyForSpilt(
                Base64.decode(encryptedData, 0),
                Base64.decode(publicKey, 0)
            )
        )
    }

    /**
     * 公钥分段解密
     *
     * @param encrypted 待解密数据
     * @param publicKey 密钥
     */
    @Throws(Exception::class)
    fun decryptByPublicKeyForSpilt(encrypted: ByteArray, publicKey: ByteArray?): ByteArray {
        val splitLen = DEFAULT_SPLIT.size
        if (splitLen <= 0) {
            return decryptByPublicKey(encrypted, publicKey)
        }
        val dataLen = encrypted.size
        val allBytes: MutableList<Byte> = ArrayList(1024)
        var latestStartIndex = 0
        var i = 0
        while (i < dataLen) {
            val bt = encrypted[i]
            var isMatchSplit = false
            if (i == dataLen - 1) {
                // 到data的最后了
                val part = ByteArray(dataLen - latestStartIndex)
                System.arraycopy(encrypted, latestStartIndex, part, 0, part.size)
                val decryptPart = decryptByPublicKey(part, publicKey)
                for (b in decryptPart) {
                    allBytes.add(b)
                }
                latestStartIndex = i + splitLen
                i = latestStartIndex - 1
            } else if (bt == DEFAULT_SPLIT[0]) {
                // 这个是以split[0]开头
                if (splitLen > 1) {
                    if (i + splitLen < dataLen) {
                        // 没有超出data的范围
                        for (j in 1 until splitLen) {
                            if (DEFAULT_SPLIT[j] != encrypted[i + j]) {
                                break
                            }
                            if (j == splitLen - 1) {
                                // 验证到split的最后一位，都没有break，则表明已经确认是split段
                                isMatchSplit = true
                            }
                        }
                    }
                } else {
                    // split只有一位，则已经匹配了
                    isMatchSplit = true
                }
            }
            if (isMatchSplit) {
                val part = ByteArray(i - latestStartIndex)
                System.arraycopy(encrypted, latestStartIndex, part, 0, part.size)
                val decryptPart = decryptByPublicKey(part, publicKey)
                for (b in decryptPart) {
                    allBytes.add(b)
                }
                latestStartIndex = i + splitLen
                i = latestStartIndex - 1
            }
            i++
        }
        val bytes = ByteArray(allBytes.size)
        run {
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
        }
        return bytes
    }

    /**
     * 使用私钥分段解密
     *
     * @param encryptedData 已加密数据(BASE64编码)
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decryptByPrivateKeyForSpilt(encryptedData: String?, privateKey: String?): String {
        return String(
            decryptByPrivateKeyForSpilt(
                Base64.decode(encryptedData, 0),
                Base64.decode(privateKey, 0)
            )
        )
    }

    /**
     * 使用私钥分段解密
     */
    @Throws(Exception::class)
    fun decryptByPrivateKeyForSpilt(encrypted: ByteArray, privateKey: ByteArray?): ByteArray {
        val splitLen = DEFAULT_SPLIT.size
        if (splitLen <= 0) {
            return decryptByPrivateKey(encrypted, privateKey)
        }
        val dataLen = encrypted.size
        val allBytes: MutableList<Byte> = ArrayList(1024)
        var latestStartIndex = 0
        var i = 0
        while (i < dataLen) {
            val bt = encrypted[i]
            var isMatchSplit = false
            if (i == dataLen - 1) {
                // 到data的最后了
                val part = ByteArray(dataLen - latestStartIndex)
                System.arraycopy(encrypted, latestStartIndex, part, 0, part.size)
                val decryptPart = decryptByPrivateKey(part, privateKey)
                for (b in decryptPart) {
                    allBytes.add(b)
                }
                latestStartIndex = i + splitLen
                i = latestStartIndex - 1
            } else if (bt == DEFAULT_SPLIT[0]) {
                // 这个是以split[0]开头
                if (splitLen > 1) {
                    if (i + splitLen < dataLen) {
                        // 没有超出data的范围
                        for (j in 1 until splitLen) {
                            if (DEFAULT_SPLIT[j] != encrypted[i + j]) {
                                break
                            }
                            if (j == splitLen - 1) {
                                // 验证到split的最后一位，都没有break，则表明已经确认是split段
                                isMatchSplit = true
                            }
                        }
                    }
                } else {
                    // split只有一位，则已经匹配了
                    isMatchSplit = true
                }
            }
            if (isMatchSplit) {
                val part = ByteArray(i - latestStartIndex)
                System.arraycopy(encrypted, latestStartIndex, part, 0, part.size)
                val decryptPart = decryptByPrivateKey(part, privateKey)
                for (b in decryptPart) {
                    allBytes.add(b)
                }
                latestStartIndex = i + splitLen
                i = latestStartIndex - 1
            }
            i++
        }
        val bytes = ByteArray(allBytes.size)
        run {
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
        }
        return bytes
    }
}