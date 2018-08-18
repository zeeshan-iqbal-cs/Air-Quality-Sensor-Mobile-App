package com.example.zeeshan.airqualityperson;

//  https://stackoverflow.com/questions/5387036/programmatically-getting-the-gateway-and-subnet-mask-details
//  https://stackoverflow.com/questions/8818290/how-do-i-connect-to-a-specific-wi-fi-network-in-android-programmatically

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by asim_ on 4/30/2018.
 */

public class WiFiConnect
{
    public boolean connected = false;
    private String networkSSID ;
    private String networkPass ;
    private int netId = 0 ;
    private DHCPInfo dInfo ;

    WifiManager wifiManager ;

    private class DHCPInfo
    {
        public String s_gateway ;
        public String s_ipAddress ;
//        public String s_dns1 ;
//        public String s_dns2 ;
//        public String s_leasDuration ;
//        public String s_netmask ;
//        public String s_serverAddress ;

        DhcpInfo d;

        DHCPInfo ( )
        {
            d = wifiManager.getDhcpInfo ( ) ;
            s_gateway = String.valueOf(intToIp(d.gateway));
            s_ipAddress = String.valueOf(intToIp (d.ipAddress));
//            s_dns1 = String.valueOf(d.dns1);
//            s_dns2 = String.valueOf(d.dns2);
//            s_leasDuration = String.valueOf(d.leaseDuration);
//            s_netmask = String.valueOf(d.netmask);
//            s_serverAddress = String.valueOf(d.serverAddress);
        }

        public String intToIp(int addr)
        {
            return  ((addr & 0xFF) + "." +
                    ((addr >>>= 8) & 0xFF) + "." +
                    ((addr >>>= 8) & 0xFF) + "." +
                    ((addr >>>= 8) & 0xFF));
        }
    }


    public WiFiConnect (final Context context, final String SSID, final String AUTH)
    {
        networkSSID = SSID;
        networkPass = AUTH;

        WifiConfiguration temp = new WifiConfiguration ( ) ;
        final WifiConfiguration conf = setWFC( temp ) ;

        wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        // Check if WiFi is working
        int wifiState = wifiManager.getWifiState() ;
        if ( wifiState == WifiManager.WIFI_STATE_DISABLED || wifiState == WifiManager.WIFI_STATE_DISABLING || wifiState == WifiManager.WIFI_STATE_UNKNOWN )
        {
            wifiManager.setWifiEnabled ( true ) ;
            Thread thr = new Thread ( )
            {
                @Override
                public void run ()
                {
                    try
                    {
                        while ( wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED )
                        {
                            Thread.sleep ( 1000 ) ;
                        }
                        ConnectToZerkhez ( conf , context );
                    }
                    catch ( Exception e ) {}
                }
            };
            thr.start();
        }
        else
        {
            ConnectToZerkhez ( conf , context );
        }
    }

    private void ConnectToZerkhez ( WifiConfiguration conf , final Context context )
    {
        //remember id
        netId = wifiManager.addNetwork(conf);
        if ( netId != -1 )
        {
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();

            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isConnected(context)) {
                            if ( !( wifiManager.getConnectionInfo().getSSID().toString().equals (networkSSID) ) ) {
                                wifiManager.disconnect();//
                                wifiManager.enableNetwork(netId, true);//
                                wifiManager.reconnect();//
                            }
                            Thread.sleep(2000);

                        }
                        dInfo = new DHCPInfo();
                        connected = true ;
                    } catch (Exception e) { }
                }
            };
            t.start();
        }
    }

    private WifiConfiguration setWFC ( WifiConfiguration wfc )
    {
        wfc.SSID = "\"".concat(networkSSID).concat("\"");
        wfc.status = WifiConfiguration.Status.DISABLED;
        wfc.priority = 40;

        wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

        wfc.preSharedKey = "\"".concat(networkPass).concat("\"");

        return wfc;
    }

    public int getNetId ( )
    {
        return netId ;
    }

    public String getIP ( )
    {
        return dInfo.s_ipAddress ;
    }

    public String getGateway ( )
    {
        return dInfo.s_gateway ;
    }

    public String getSSID ( )
    {
        return wifiManager.getConnectionInfo().getSSID().toString();
    }

    public static boolean isConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null)
        {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED;
    }


}
