package demo.xy.com.xytdcq.nkd.encryption

class EncryptionFile {
    //加密文件
    companion object {
        external fun CryptorEncrypt(normal_path_jstr: String,crypt_path_jstr: String)
        //解密文件
        external fun CryptorDecrypt(crypt_path_jstr: String,decrypt_path_jstr: String)

    }

}