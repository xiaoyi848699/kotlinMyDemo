#include "MyRSA.h"
#include <cstddef>
#include <stdlib.h>
#include "Log.h"


#include "openssllib/include/openssl/bio.h"
#include "openssllib/include/openssl/evp.h"
#include "openssllib/include/openssl/rsa.h"
#include "openssllib/include/openssl/pem.h"


#define  PUBLIC_EXPONENT   RSA_F4
#define   MODULUS "9c847aae8aa567d36af169dbed35f42f9568d137067b30a204476897020e7d88914d1c03a671c62be4a05fbd645bd358b2ff38ad2e5166003414eb7b155301d1f6cacaa54260681073e1c02947379614e6b6123e5b35af50dc675f1c673565979cc4acb967976e209bad50d24ab38b6822198644de43874e4fb92714f6fd677d"


#define PUBLICKEY "-----BEGIN PUBLIC KEY-----\nMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCwgKPvbsEqNbBKEuYqQMkCA792\nPddSfNdi5ANcxGneQ38tfeVl3hNGPVpsYMTQYqWeEv+NBufKBadpfBm6HZuOm5nu\nLfSZPPjrM60XBPU5tvg7LJEsS0Uh3hv1pKk8TJVdcfyak56CyMNXXu2r7lXL80GR\n7aXgB+lfj3zfpJIMlQIDAQAB\n-----END PUBLIC KEY-----\n"

#define PRIVATE_KEY  "-----BEGIN RSA PRIVATE KEY-----\nMIICXAIBAAKBgQCwgKPvbsEqNbBKEuYqQMkCA792PddSfNdi5ANcxGneQ38tfeVl\n3hNGPVpsYMTQYqWeEv+NBufKBadpfBm6HZuOm5nuLfSZPPjrM60XBPU5tvg7LJEs\nS0Uh3hv1pKk8TJVdcfyak56CyMNXXu2r7lXL80GR7aXgB+lfj3zfpJIMlQIDAQAB\nAoGBAJ62zrObcG+4X5H8dKRCJX5+SEjXSyyNvlDaoHtm05xeLZqGvyVfEQ30Vb9n\nNRP94NfIVaxHLV9oviYIxkmqHhocAT2Yo1NQpWohljsrmiPHtYqfsgW+w+zSylXe\n+FHrLK+oUOxhDo0hFhhdGRuCnK8GD97Y4EtLg1CsfCIEYQsBAkEA3hABuD9jS2Gs\n/2P1bEHhefxsBUn9I/LaIBwtodA6s7rmGvkdagDT8PByfZnyQ1VeOQ3xC4lL48Tl\nXOxQ2mT35QJBAMt6I6wkieeLWIQKLMLiOkqZw47sP+IbD6zwiioXJ/uW8sHJT6no\nRLrKvQplvdJxuuqz6USr+1IJ1tRiK6PrFvECQD0HEyHqscQ2vM+XTgyJcokO2TT+\n54XoqQ+oDtZonqlkVPbWvcGzJowR2LUyCMV+gZ2WekdcXTHkm9BU1cefdg0CQDwd\nT6CyAtQXhQUthF/nOlWD7BSzk4QXfqAXSJp60OMxi3LZKOIHrxaIgyUpPdWIqDJj\nCM2zCcMJCSEiB8ab0+ECQG682Gh3+xWjXsWsC+UGfyTl2suYwFlpkrl8sk3i/giE\nKoawU1mQhj+BmArC5oixIWQtjxd1rP+8KQ2JB4A0xno=\n-----END RSA PRIVATE KEY-----\n"


#define  PADDING   RSA_PKCS1_PADDING          //填充方式




std::string MyRSA::decryptRSA(const std::string &data) {


    int ret, flen;
    BIO *bio = NULL;
    RSA *r = NULL;

    if ((bio = BIO_new_mem_buf((void *) PRIVATE_KEY, -1)) == NULL)       //从字符串读取RSA公钥
    {
        LOGE("BIO_new_mem_buf failed!\n");
    }

    r = PEM_read_bio_RSAPrivateKey(bio, NULL, NULL, NULL);


    flen = RSA_size(r);


    static std::string gkbn;
    gkbn.clear();


    unsigned char *dst = NULL;


    dst = (unsigned char *) malloc(flen+1);

    bzero(dst, flen);


    int state = RSA_private_decrypt(data.length(), (unsigned char *) data.c_str(), dst, r,
                                    RSA_PKCS1_PADDING);//RSA_NO_PADDING //RSA_PKCS1_PADDING

    if (state < 0) {

        LOGE("RSA 解密失败");



    }

    gkbn.assign((char *) dst, state);//防止 尾部0 被截断


    BIO_free_all(bio);


    free(dst);

   // CRYPTO_cleanup_all_ex_data(); //清除管理CRYPTO_EX_DATA的全局hash表中的数据，避免内存泄漏
    dst=NULL;
    r=NULL;
    bio=NULL;

    return gkbn;
}


std::string MyRSA::encryptRSA(const std::string &data, int *lenreturn) {


    int ret, flen;
    BIO *bio = NULL;
    RSA *r = NULL;

    if ((bio = BIO_new_mem_buf((void *) PUBLICKEY, -1)) == NULL)       //从字符串读取RSA公钥
    {
        LOGE("BIO_new_mem_buf failed!\n");
    }

    r = PEM_read_bio_RSA_PUBKEY(bio, NULL, NULL, NULL);

    flen = RSA_size(r);


//    if (PADDING == RSA_PKCS1_PADDING || PADDING == RSA_SSLV23_PADDING) {
//        flen -= 11;
//    }
    lenreturn = &flen;


    static std::string gkbn;
    gkbn.clear();


    char *dst = (char *) malloc(flen+1);
    bzero(dst, flen);


    int status = RSA_public_encrypt(data.length(), (unsigned char *) data.c_str(),
                                    (unsigned char *) dst, r, RSA_PKCS1_PADDING);


    if (status < 0) {

        LOGE("RSA 加密失败");

    }



    gkbn.assign((char *) dst, status);


    RSA_free(r);
    BIO_free_all(bio);

    free(dst);
    dst=NULL;
    r=NULL;
    bio=NULL;

    //CRYPTO_cleanup_all_ex_data(); //清除管理CRYPTO_EX_DATA的全局hash表中的数据，避免内存泄漏

    return gkbn;

}


