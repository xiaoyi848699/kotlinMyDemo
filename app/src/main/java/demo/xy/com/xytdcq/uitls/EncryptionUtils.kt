package demo.xy.com.xytdcq.uitls

import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


/**
 * 加密工具类
 * @author xy
 */
object EncryptionUtils {
    val CHARSET = "UTF-8"

    val RSA_ALGORITHM = "RSA"
    val CIPHER_RSA_ALGORITHM = "RSA/ECB/PKCS1Padding"//Cipher.getInstance不用这个会出现多出乱码问题，或者无法解析问题

    val publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyZS4oR05xeYtlPh9voiwth44bEM0Tq2CZ+daRoV0FNPX4Qb9vzy441OpQbyDbpCP5ZTuUZXI5NYCkw5V7sH18zp2TmZ4XzPVADsXINmk0Om+fQwkfoEG6FpeTJEc74PMdP82kd74vOV0xjbAT5E7qOkVnH2hLnZn7c/MZt3IahQIDAQAB"

    val privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALJlLihHTnF5i2U+H2+iLC2HjhsQzROrYJn51pGhXQU09fhBv2/PLjjU6lBvINukI/llO5Rlcjk1gKTDlXuwfXzOnZOZnhfM9UAOxcg2aTQ6b59DCR+gQboWl5MkRzvg8x0/zaR3vi85XTGNsBPkTuo6RWcfaEudmftz8xm3chqFAgMBAAECgYEAnohL/H9htnXM6qvKQ+paWBuerW7A0Dn8HtlnVQORJo9qBrRm63WyHc8Ya2JWUNoNHhRYXgfI+g4RxFoK/WBaIpAv2QY5oa8S9/XCaaIwKtFve5SSHWgUTZlolltIfDJKThv9ke3MK0xpv2GcPKD1IIGDu4w0kpAxNvwcoBbNagECQQD0mBEjIZ9V7N7ax6rZo1uyE93t7mS7gtFhBVz9xXQidd+j94SUfo+sQtzqfJU0Ews5SL26wbK/6vX2oE+Fi+DfAkEAurbXLvMC+eVTGUrG1jBoj46d3lPmPfyQLJyxEdXKlItOCcfbVizyJpw/fRoLFCokP9fSZUiAXsrXJ29cQSv9GwJAbcUhS8b3HzTi/wTE5E+wA0dvPizTAGRBW21wwmMvL5f++jNHSO1TxVdslb+7plc1nkvK+lmbww3LvRdNGP4huQJBAItzzDvwnKRwNySIyQFonm+6IQbQuwJJBJWNYmLjvq47bUm4z4UJhMJ5qKGTNfsVjoVweF/VSquB7Dgz4D98z0kCQEGuRgqA2eEGvnCYsC0VxEl7MER00lfFLVoKG1XZEf0OrcxLj8eQbumJtfc3gNFOVMvQVjPDQCcd7RbqQEzXxuc="

    /**
     * MD5 加密
     * @return 加密后的密码
     */
    fun getMD5Str(str: String): String {
        try {
            val instance:MessageDigest = MessageDigest.getInstance("MD5")//获取md5加密对象
            val digest:ByteArray = instance.digest(str.toByteArray())//对字符串加密，返回字节数组
            var sb = StringBuffer()
            for (b in digest) {
                var i :Int = b.toInt() and 0xff//获取低八位有效值
                var hexString = Integer.toHexString(i)//将整数转化为16进制
                if (hexString.length < 2) {
                    hexString = "0$hexString"//如果是一位的话，补0
                }
                sb.append(hexString)
            }
            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 得到公钥
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun getPublicKey(publicKey: String): RSAPublicKey {
        // 通过X509编码的Key指令获得公钥对象
        val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
        val x509KeySpec = X509EncodedKeySpec(Base64.decode(publicKey))
        return keyFactory.generatePublic(x509KeySpec) as RSAPublicKey
    }

    /**
     * 得到私钥
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun getPrivateKey(privateKey: String): RSAPrivateKey {
        // 通过PKCS#8编码的Key指令获得私钥对象
        val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
        val pkcs8KeySpec = PKCS8EncodedKeySpec(Base64.decode(privateKey))
        return keyFactory.generatePrivate(pkcs8KeySpec) as RSAPrivateKey
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    fun publicEncrypt(data: String, publicKey: RSAPublicKey): String? {
        try {
            val cipher = Cipher.getInstance(CIPHER_RSA_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            return Base64.encode(
                    rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.toByteArray(Charsets.UTF_8), publicKey.getModulus().bitLength()))
        } catch (e: Exception) {
            throw RuntimeException("加密字符串[$data]时遇到异常", e)
        }

    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     */

    fun privateDecrypt(data: String, privateKey: RSAPrivateKey): String {
        try {
            val cipher = Cipher.getInstance(CIPHER_RSA_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            return String(rsaSplitCodec(cipher,
                    Cipher.DECRYPT_MODE,
                    Base64.decode(data),
                    privateKey.getModulus().bitLength()),Charsets.UTF_8)
        } catch (e: Exception) {
            throw RuntimeException("解密字符串[$data]时遇到异常", e)
        }

    }

    /**
     * 私钥加密
     * @param data
     * @param privateKey
     * @return
     */

    fun privateEncrypt(data: String, privateKey: RSAPrivateKey): String? {
        try {
            val cipher = Cipher.getInstance(CIPHER_RSA_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, privateKey)
            return Base64.encode(rsaSplitCodec(cipher,
                    Cipher.ENCRYPT_MODE,
                    data.toByteArray(Charsets.UTF_8),
                    privateKey.getModulus().bitLength()))
        } catch (e: Exception) {
            throw RuntimeException("加密字符串[$data]时遇到异常", e)
        }

    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     */

    fun publicDecrypt(data: String, publicKey: RSAPublicKey): String {
        try {
            val cipher = Cipher.getInstance(CIPHER_RSA_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, publicKey)
            return String(rsaSplitCodec(cipher,
                    Cipher.DECRYPT_MODE,
                    Base64.decode(data),
                    publicKey.getModulus().bitLength()),Charsets.UTF_8)
        } catch (e: Exception) {
            throw RuntimeException("解密字符串[$data]时遇到异常", e)
        }

    }

    private fun rsaSplitCodec(cipher: Cipher, opmode: Int, datas: ByteArray, keySize: Int): ByteArray {
        var maxBlock = 0
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8
        } else {
            maxBlock = keySize / 8 - 18
        }
        val out = ByteArrayOutputStream()
        var offSet = 0
        var buff: ByteArray
        var i = 0
        try {
            while (datas.size > offSet) {
                if (datas.size - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock)
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.size - offSet)
                }
                out.write(buff, 0, buff.size)
                i++
                offSet = i * maxBlock
            }
        } catch (e: Exception) {
            throw RuntimeException("加解密阀值为[$maxBlock]的数据时发生异常", e)
        }

//		IOUtils.closeQuietly(out);
        return out.toByteArray()
    }

}