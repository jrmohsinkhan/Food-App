package com.example.foodapp.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.example.foodapp.model.LoginResponse
import kotlinx.serialization.json.Json
import java.io.File
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object SecureStorage {

    private const val KEY_ALIAS = "MySecureKey"
    private const val FILE_NAME = "secure_data.json"

    // Generate or retrieve secret key
    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        return if (keyStore.containsAlias(KEY_ALIAS)) {
            (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
        } else {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
            val keyGenSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()

            keyGenerator.init(keyGenSpec)
            keyGenerator.generateKey()
        }
    }

    // Save secure JSON
    fun saveData(context: Context, response: LoginResponse) {
        val json = Json.encodeToString(response)
        val secretKey = getSecretKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(json.toByteArray())

        val encryptedData = Base64.encodeToString(iv + encryptedBytes, Base64.DEFAULT)

        val file = File(context.filesDir, FILE_NAME)
        file.writeText(encryptedData)
    }

    // Read secure JSON
    fun readData(context: Context): LoginResponse? {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return null

        val encryptedData = Base64.decode(file.readText(), Base64.DEFAULT)

        val secretKey = getSecretKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        val iv = encryptedData.copyOfRange(0, 12)
        val ciphertext = encryptedData.copyOfRange(12, encryptedData.size)

        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        val decryptedJson = String(cipher.doFinal(ciphertext))

        // ✅ Deserialize JSON back into LoginResponse
        return Json.decodeFromString<LoginResponse>(decryptedJson)
    }

    // Clear stored data
    fun clearData(context: Context) {
        val file = File(context.filesDir, FILE_NAME)
        if (file.exists()) file.delete()
    }
}
