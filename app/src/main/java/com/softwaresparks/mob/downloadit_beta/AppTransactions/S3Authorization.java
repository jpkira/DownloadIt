package com.softwaresparks.mob.downloadit_beta.AppTransactions;

import android.util.Log;

import com.softwaresparks.mob.downloadit_beta.AppViews.LobbyActivity;
import com.softwaresparks.mob.downloadit_beta.R;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by xLipse on 1/18/2017.
 */

public class S3Authorization {

    private LobbyActivity lobby;

    private String url;
    private String pre_sign;
    private String signature;
    private String string2sign;
    private String filetype;

    public S3Authorization(LobbyActivity lobby) {
        this.lobby = lobby;
        filetype = lobby.getResources().getString(R.string.filetype);
    }

    public String getGenerateURL(String httpVerb, String gmtDate, String uri) {
        return generateURL(httpVerb, gmtDate, uri);
    }

    public String getGenerateURL(String httpVerb, String filetype, String gmtDate, String uri) {
        this.filetype = filetype;
        return generateURL(httpVerb, gmtDate, uri);
    }


    public String getPreSignature() {
        return pre_sign;
    }

    private String generateURL(String httpVerb, String gmtDate, String uri) {

        url = lobby.getResources().getString(R.string.s3url);
        pre_sign = lobby.getResources().getString(R.string.s3presig);
        signature = lobby.getResources().getString(R.string.s3sig);

        if (httpVerb.equals("GET")) {
            Log.d(S3Constants.LOBBY_TAG, "S3Auth GET");
            string2sign = httpVerb + "\n\n\n" + gmtDate + "\n" + S3Constants.BUCKET + uri;
        } else if (httpVerb.equals("PUT")) {
            Log.d(S3Constants.LOBBY_TAG, "S3Auth PUT");
            string2sign = httpVerb + "\n\n" + filetype + "\n" + gmtDate + "\n" + S3Constants.BUCKET + "/" + uri;
        }

        Log.d(S3Constants.LOBBY_TAG, string2sign);

        try {
            URLEncoder.encode(string2sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(S3Constants.LOBBY_TAG, "URLEncode1: " + String.valueOf(e));
        }

        try {
            signature = sha1(string2sign, S3Constants.SECRET_KEY);
            pre_sign = signature;
        } catch (UnsupportedEncodingException e) {
            Log.d(S3Constants.LOBBY_TAG, "Sha1-a" + String.valueOf(e));
        } catch (NoSuchAlgorithmException e) {
            Log.d(S3Constants.LOBBY_TAG, "Sha1-b" + String.valueOf(e));
        } catch (InvalidKeyException e) {
            Log.d(S3Constants.LOBBY_TAG, "Sha1-c" + String.valueOf(e));
        }

        try {
            URLEncoder.encode(signature, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(S3Constants.LOBBY_TAG, "URLEncode2: " + String.valueOf(e));
        }

        SimpleDateFormat gmtFormat = new SimpleDateFormat("EEE, d MMM yyyy H:m:s Z");

        try {
            Date date = gmtFormat.parse(gmtDate);
            date.setTime(date.getTime() + 60 * 60 *1000);
            String exp_gmtDate = gmtFormat.format(date);

            url = S3Constants.SERVER_URL
                    + "?AWSAccessKeyId='" + S3Constants.ACCESS_KEY
                    + "'&Expires='" + exp_gmtDate
                    + "'&Signature='" + signature + "'";

        } catch (ParseException e) {
            Log.d(S3Constants.LOBBY_TAG, "URL: " + String.valueOf(e));
        }

        return url;

    }

    private static String sha1(String s, String keyString) throws
            UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {

        SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(key);

        byte[] bytes = mac.doFinal(s.getBytes("UTF-8"));

        return new String( Base64.encodeBase64(bytes) );
    }

}
