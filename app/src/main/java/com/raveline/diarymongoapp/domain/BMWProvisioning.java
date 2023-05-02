/*
package com.bmwgroup.apinext.telematicscomponents.provisioninglib;

import android.content.Context;
import android.content.Intent;
import java.lang.Deprecated;
*/
/**
 * This class provide methods to get Provisioning values from provisioning service.
 *//*

public class BMWProvisioning {

    private static Object lock = new Object();

    */
/**
     * Get data for the given class.
     * @param context The context to use. Usually your {@code Application}
     * or {@code Activity} object.
     * @param clazz The pojo class you want to retrieve data.
     * @param callback The callback that will be triggered with the available data.
     * @param <T> The type of your data class.
     *//*

    public static <T> void getData(Context context, Class<T> clazz, BMWProvisioningCallback callback){
        synchronized (lock) {
            Context applicationContext = context.getApplicationContext();
            ServiceConnectionManager connection = (ServiceConnectionManager) DepInjection.getServiceConnection(
                    applicationContext,
                    clazz.getName(),
                    callback,
                    lock
            );
            setClassDataTypeAndBindService(applicationContext, clazz, connection);
        }
    }

    */
/**
     * Get data from all domains.
     * @param context The context to use. Usually your {@code Application}
     * or {@code Activity} object.
     * @param callback The callback that will be triggered with the available data.
     *//*

    public static void getDomainsData(Context context, BMWProvisioningCallback callback){
        synchronized (lock) {
            Context applicationContext = context.getApplicationContext();
            ServiceConnectionManager connection = (ServiceConnectionManager) DepInjection.getDomainsServiceConnection(
                    applicationContext,
                    callback,
                    lock
            );
            bindService(applicationContext, connection);
        }
    }

    */
/**
     * Subcribe to changes on provisioning values for the given class.
     * In order to receive notifications of new provisioning values when using this function you should
     * look at all the instructions available on Developer Portal.
     * @param context The context to use. Usually your {@code Application}
     * or {@code Activity} object.
     * @param clazz The pojo class you want to retrieve data.
     * @param callback The callback that will be triggered with the available data when you subcribe.
     * @param <T> The type of your data class.
     *//*

    public static <T> void subscribeTo(Context context, Class<T> clazz, BMWProvisioningCallback callback){
        synchronized (lock){
            Context applicationContext = context.getApplicationContext();
            ServiceConnectionManager connection = (ServiceConnectionManager) DepInjection.getSubscribeServiceConnection(
                    applicationContext,
                    clazz.getName(),
                    callback,
                    lock
            );
            setClassDataTypeAndBindService(applicationContext, clazz, connection);
        }
    }

    */
/**
     * Unsubscribe to further changes on provisioning values for the given class.
     * @param context The context to use. Usually your {@code Application}
     * or {@code Activity} object.
     * @param clazz The pojo class representing the domain you want to unsubscribe.
     * @deprecated  callback Deprecated argument
     * @param <T> The type of your data class.
     *//*

    public static <T> void unsubscribeTo(Context context, Class<T> clazz, BMWProvisioningCallback callback) {
        synchronized (lock) {
            Context applicationContext = context.getApplicationContext();
            ServiceConnectionManager connection = (ServiceConnectionManager) DepInjection.getUnSubscribeServiceConnection(
                    applicationContext,
                    clazz.getName(),
                    callback,
                    lock
            );
            setClassDataTypeAndBindService(applicationContext, clazz, connection);
        }
    }

    public static <T> void unsubscribeTo(Context context, Class<T> clazz) {
        synchronized (lock) {
            Context applicationContext = context.getApplicationContext();
            ServiceConnectionManager connection = (ServiceConnectionManager) DepInjection.getUnSubscribeServiceConnection(
                    applicationContext,
                    clazz.getName(),
                    null,
                    lock
            );
            setClassDataTypeAndBindService(applicationContext, clazz, connection);
        }
    }

    */
/**
     * Get the provisioning status indicating if provisioning is in DAS or OTA
     * @param context The context to use. Usually your {@code Application}
     * or {@code Activity} object.
     * @param callback The callback that will be triggered with the available status.
     *//*

    public static void getProvisioningStatus(Context context, BMWProvisioningCallback callback){
        synchronized (lock) {
            Context applicationContext = context.getApplicationContext();
            ServiceConnectionManager connection = (ServiceConnectionManager) DepInjection.getProvisioningStatusServiceConnection(
                    applicationContext,
                    callback,
                    lock
            );
            bindService(applicationContext, connection);
        }
    }

    */
/**
     * ONLY FOR DEVELOPMENT - Used in Provisioning Editor
     * Sends a provisioning XML to the Provisioning Service.
     * @param context The context to use. Usually your {@code Application}
     * or {@code Activity} object.
     * @deprecated callback Deprecated argument
     * @param provisioningXML The Provisioning XML to send.
     * @param uuid The Provisioning UUID to send.
     *//*

    @Deprecated
    public static void sendProvisioning(Context context, BMWProvisioningCallback callback, String provisioningXML, String uuid){
        synchronized (lock) {
            Context applicationContext = context.getApplicationContext();
            ServiceConnectionManager connection = (ServiceConnectionManager) DepInjection.getSendProvisioningServiceConnection(
                    applicationContext,
                    callback,
                    lock
            );
            setProvisioningXMLAndBindService(
                    applicationContext,
                    connection,
                    provisioningXML,
                    uuid);
        }
    }

    public static void sendProvisioning(Context context, String provisioningXML, String uuid){
        synchronized (lock) {
            Context applicationContext = context.getApplicationContext();
            ServiceConnectionManager connection = (ServiceConnectionManager) DepInjection.getSendProvisioningServiceConnection(
                    applicationContext,
                    null,
                    lock
            );
            setProvisioningXMLAndBindService(
                    applicationContext,
                    connection,
                    provisioningXML,
                    uuid);
        }
    }

    private static void bindService(Context context, ServiceConnectionManager connection){
        Intent serviceIntent = DepInjection.getServiceIntent();
        context.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    private static <T> void setClassDataTypeAndBindService(
            Context context,
            Class<T> clazz,
            ServiceConnectionManager connection
    ){
        connection.setClassDataType(clazz);
        Intent serviceIntent = DepInjection.getServiceIntent();
        context.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    private static <T> void setProvisioningXMLAndBindService(
            Context context,
            ServiceConnectionManager connection,
            String provisioningXML,
            String uuid
    ){
        connection.setProvisioningXML(provisioningXML);
        connection.setProvisioningUuid(uuid);
        Intent serviceIntent = DepInjection.getServiceIntent();
        context.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }
}


*/
