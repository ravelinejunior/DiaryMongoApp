/*
 * Copyright (C) 2023 BMW AG. All rights reserved.
 *//*


package com.bmwgroup.idnext.persoservice.repository.impl

import android.content.Context
import android.util.Log
import com.bmwgroup.apinext.telematicscomponents.provisioninglib.BMWProvisioning
import com.bmwgroup.apinext.telematicscomponents.provisioninglib.BMWProvisioningCallback
import com.bmwgroup.apinext.telematicscomponents.provisioninglib.LibParser
import com.bmwgroup.idnext.persoservice.util.Constants
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class ProvisioningDataRepo(val context: Context) {

    companion object {
        private const val LOG_TAG = "${Constants.REPO_LOG_TAG} #ProvisioningDataRepo"
        private const val PERSO_ROOT = "perso"
        private const val MOTIVATED_LOGIN = "motivated_login"
        private const val CONNECTED_DRIVE = "connected_drive"
        private const val ENABLED = "enabled"

        private lateinit var instance: ProvisioningDataRepo

        @Synchronized
        fun getInstance(context: Context): ProvisioningDataRepo {
            if (!::instance.isInitialized) {
                instance = ProvisioningDataRepo(context)
            }
            return instance
        }
    }

    @JacksonXmlRootElement(localName = PERSO_ROOT)
    data class PersoProvisioningData(
            @JacksonXmlProperty(localName = MOTIVATED_LOGIN)
            val motivatedLogin: Boolean,
            @JacksonXmlProperty(localName = CONNECTED_DRIVE)
            val connectedDriveEnabled: ConnectedDriveEnabled?
    )

    @JacksonXmlRootElement(localName = CONNECTED_DRIVE)
    data class ConnectedDriveEnabled constructor(
    @JacksonXmlProperty(localName = ENABLED)
    val enabled: Boolean? = null
            )

    private var latestPersoProvisiongData = PersoProvisioningData(true, null)

    fun getMotivatedLogin(): Boolean {
        return latestPersoProvisiongData.motivatedLogin
    }

    fun getConnectedDriveEnabled(): Boolean {
        return latestPersoProvisiongData.connectedDriveEnabled?.enabled ?: true
    }

    fun subscribeToPersoProvisionData() {
        BMWProvisioning.subscribeTo(
                context, PersoProvisioningData::class.java,
                object : BMWProvisioningCallback {
            override fun <T : Any?> onData(newData: T) {
                Log.i(LOG_TAG, "Subscribed to PersoProvisioningData Class")
                processProvisioningData(newData)
            }
        }
        )
    }

    fun unsubscribeToPersoProvisionData() {
        BMWProvisioning.unsubscribeTo(
                context, PersoProvisioningData::class.java
        )
    }

    fun processIntent(domain: String, value: String) {
        when (domain) {
            PERSO_ROOT -> {
                processProvisioningData(LibParser(PersoProvisioningData::class.java).getObject(value))
            }
            else -> Log.e(LOG_TAG, "Got Update from Provisioning Receiver for unknown Domain $domain")
        }
    }

    private fun processProvisioningData(newData: Any?) {
        newData?.let {
            try {
                latestPersoProvisiongData = it as PersoProvisioningData
                Log.i(LOG_TAG, "Got new provisioning data for perso $latestPersoProvisiongData")
            } catch (e: ClassCastException) {
                Log.e(LOG_TAG, "New data cannot be cast to perso!")
            }
        }
    }
}

*/
