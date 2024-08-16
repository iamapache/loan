package com.lemon.now.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 *   Lemon Cash
 *  MD5Util.java
 *
 */
object MD5Util {

    private val DigitLower = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6',
        '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    )

    private val DigitUpper = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6',
        '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    )

    @Throws(NoSuchAlgorithmException::class)
    fun getMD5Lower(srcStr: String): String {
        val sign = "lower"
        return processStr(srcStr, sign)
    }

    fun getMD5Upper(srcStr: String): String {
        val sign = "upper"
        return try {
            processStr(srcStr, sign)
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }

    @Throws(NoSuchAlgorithmException::class, NullPointerException::class)
    private fun processStr(srcStr: String, sign: String): String {
        val digest: MessageDigest
        val algorithm = "MD5"
        var result = ""
        digest = MessageDigest.getInstance(algorithm)
        digest.update(srcStr.toByteArray())
        val byteRes = digest.digest()
        val length = byteRes.size
        for (i in 0 until length) {
            result = result + byteHEX(byteRes[i], sign)
        }
        return result
    }

    private fun byteHEX(bt: Byte, sign: String): String {
        var temp: CharArray? = null
        temp = if (sign.equals("lower", ignoreCase = true)) {
            DigitLower
        } else if (sign.equals("upper", ignoreCase = true)) {
            DigitUpper
        } else {
            throw RuntimeException("")
        }
        val ob = CharArray(2)
        ob[0] = temp[bt.toInt() ushr 4 and 0X0F]
        ob[1] = temp[bt.toInt() and 0X0F]
        return ob.toString()
    }

    fun getMD5(content: String): String {
        var messageDigest: MessageDigest? = null
        try {
            messageDigest = MessageDigest.getInstance("MD5")
            messageDigest.reset()
            messageDigest.update(content.toByteArray(charset("UTF-8")))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val byteArray = messageDigest!!.digest()
        val md5StrBuff = StringBuffer()
        for (i in byteArray.indices) {
            if (Integer.toHexString(0xFF and byteArray[i].toInt()).length == 1) md5StrBuff.append("0")
                .append(
                    Integer.toHexString(0xFF and byteArray[i].toInt())
                ) else md5StrBuff.append(Integer.toHexString(0xFF and byteArray[i].toInt()))
        }
        return md5StrBuff.toString()
    }

}