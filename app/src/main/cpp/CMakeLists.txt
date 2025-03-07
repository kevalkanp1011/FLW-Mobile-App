cmake_minimum_required(VERSION 3.11)
project(Sakhi LANGUAGES CXX)

# Fetch environment variables
set(ENCRYPTED_PASS_KEY "$ENV{ENCRYPTED_PASS_KEY}")
set(ABHA_CLIENT_SECRET "$ENV{ABHA_CLIENT_SECRET}")
set(ABHA_CLIENT_ID "$ENV{ABHA_CLIENT_ID}")
set(BASE_TMC_URL "$ENV{BASE_TMC_URL}")
set(BASE_ABHA_URL "$ENV{BASE_ABHA_URL}")
set(ABHA_TOKEN_URL "$ENV{ABHA_TOKEN_URL}")
set(ABHA_AUTH_URL "$ENV{ABHA_AUTH_URL}")
set(CHAT_URL "$ENV{CHAT_URL}")

# Pass the values to the compiler using add_definitions
add_definitions(
        -DENCRYPTED_PASS_KEY=\"${ENCRYPTED_PASS_KEY}\"
        -DABHA_CLIENT_SECRET=\"${ABHA_CLIENT_SECRET}\"
        -DABHA_CLIENT_ID=\"${ABHA_CLIENT_ID}\"
        -DBASE_TMC_URL=\"${BASE_TMC_URL}\"
        -DBASE_ABHA_URL=\"${BASE_ABHA_URL}\"
        -DABHA_TOKEN_URL=\"${ABHA_TOKEN_URL}\"
        -DABHA_AUTH_URL=\"${ABHA_AUTH_URL}\"
        -DCHAT_URL=\"${CHAT_URL}\"
)

# Define the library name
set(LIBRARY_NAME "sakhi")

# Add the native-lib.cpp as the source file for the shared library
add_library(
        ${LIBRARY_NAME}
        SHARED
        native-lib.cpp
)

# Find the log library
find_library(
        log-lib
        log
)

# Specifies libraries CMake should link to your target library.
target_link_libraries(
        ${LIBRARY_NAME}
        ${log-lib}
)

# Include directories
# include_directories(${CMAKE_SOURCE_DIR}/include)
