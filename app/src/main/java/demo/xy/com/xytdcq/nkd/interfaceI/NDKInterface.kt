package demo.xy.com.xytdcq.nkd.interfaceI

class NDKInterface {

    companion object {
        /**
         * 加密文件
         */
        external fun cryptorEncrypt(normal_path_jstr: String, crypt_path_jstr: String)
        /**
         * 解密文件
         */
        external fun cryptorDecrypt(crypt_path_jstr: String, decrypt_path_jstr: String)
        /**
         * 拆分文件
         */
        external fun spliteFlie(path_jstr: String,path_pattern_jstr: String,file_num:Int)
        /**
         * 合并文件
         */
        external fun mergeFlie(path_pattern_jstr: String,merge_path_jstr: String,file_num:Int)
        /**
         * 合并文件

         */
        external fun bsdfff(oldApkPath: String,newApkPath: String,patch: String)

    }

}