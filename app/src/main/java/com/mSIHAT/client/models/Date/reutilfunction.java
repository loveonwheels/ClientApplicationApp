package com.mSIHAT.client.models.Date;

/**
 * Created by ghost on 15/8/16.
 */
public class reutilfunction {

    public static String convertname_to_intial(String fullname){
        String initial = "";
        String[] splited = fullname.trim().split("\\s+");
        if(splited[0] != null){
            initial = initial+ String.valueOf(splited[0].trim().charAt(0)).toUpperCase();
        }
        if(splited[1] != null){
            initial = initial+String.valueOf(splited[1].trim().charAt(1)).toUpperCase();
        }

        return initial;
    }

    public static String convertgender(String gender){
        String genderreturn = "";

                if(gender.equals("False")){

                    genderreturn = "Female";

                }else{

                    genderreturn = "Male";

                }
        return genderreturn;
    }
}
