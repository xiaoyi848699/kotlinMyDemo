# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

#导入头文件路径
INCLUDE_DIRECTORIES(
src/main/cpp/openssllib/include/openssl/
)
#INCLUDE_DIRECTORIES(F:/sdk/ndk-bundle/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/)

#链接库文件路径（.a .so 文件）
LINK_DIRECTORIES(src/main/cpp/openssllib/${ANDROID_ABI}/)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#编译源代码
add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp
             src/main/cpp/MyBASE64.cpp
             src/main/cpp/MyRSA.cpp
             src/main/cpp/My3DES.cpp
             src/main/cpp/MyAES.cpp
             src/main/cpp/MyMD5.cpp)

add_library( # Sets the name of the library.
            file-encrypt-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/ndk-file-encrypt.c)

add_library( # Sets the name of the library.
        file-splite-merge

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/file-splite-merge.c)
add_library( # Sets the name of the library.
        apk-bspatch

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/bspatch.c)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.
#链接Android 日志库文件
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
#编译.so 文件
target_link_libraries( # Specifies the target library.
                       native-lib
                       # Links the target library to the log library
                       # included in the NDK.
                        # 链接动态库
                       libcrypto.a
                       libssl.a
                       libz.so
                       ${log-lib} )
target_link_libraries( # Specifies the target library.
                       file-encrypt-lib
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )
target_link_libraries( # Specifies the target library.
                       file-splite-merge
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )
target_link_libraries( # Specifies the target library.
                       apk-bspatch
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )