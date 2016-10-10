package com.mSIHAT.client.utils;

/**
 * Created by alamchristian on 3/2/16.
 */
public class Constants {
    public static final String API_URL = "http://healthcarepro.azurewebsites.net/alamazure/";
  //  public static final String API_URL = "http://192.168.0.1:16114/alamazure/";
    public static final String PAYPAL_CLIENT_ID = "ARbRdSlh38xDR_eQz556iS5_S3NEIMUOChHm4cugHOWqLtc8xgetcZBTFdIF6xdudC__vNWZK-rTxG2p";

    public static final String KEY_UPDATE_DETAILS_PURPOSE = "update_details_activity";
    public static final String EXTRA_DETAILS_PURPOSE = "details_purpose";
    public static final String EXTRA_DETAILS_PURPOSE_PATIENT = "details_purpose_patient";
    public static final String EXTRA_DETAILS_PURPOSE_MULTI_APPOINTMENTS = "details_purpose_multi_appointments";
    public static final String EXTRA_REGISTER_PURPOSE_PATIENT = "register_purpose_patient";

    public static final String EXTRA_UPDATE_ACCOUNT_DETAILS = "update_account";

    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_USER_FULLNAME = "user_name";
    public static final String EXTRA_USER_EMAIL = "user_email";
    public static final String PARCEL_USER_OBJECT = "user_object";

    public static final String EXTRA_PATIENT_ID = "patient_id";

    public static final String EXTRA_SUBSERVICE_ID = "subservice_id";

    public static final String EXTRA_PRACTITIONER_ID = "practitioner_id";

    public static final String EXTRA_CITY_ID = "city_id";

    public static final String EXTRA_IS_MULTI_APPOINTMENT = "is_multi";
    public static final String EXTRA_MULTI_APPOINTMENT_ID = "multi_appointment_id";
    public static final String EXTRA_MULTI_APPOINTMENT_FREQUENCY = "multi_appointment_frequency";
    public static final String EXTRA_MULTI_APPOINTMENT_AMOUNT = "multi_appointment_amount";

    public static final String EXTRA_APPOINTMENT_ID = "appointment_id";
    public static final String EXTRA_APPOINTMENT_DATETIME = "appointment_datetime";
  public static final String EXTRA_SLOT_ID = "slot_id";
  public static final String EXTRA_APPOINTMENT_DATE = "appointment_date";
  public static final String EXTRA_APPOINTMENT_TIME = "appointment_time";

    public static final String EXTRA_APPOINTMENT_STATUS = "appointment_status";
    public static final String EXTRA_FINAL_RATE = "final_rate";

    //fragment tags
    public static final String MAIN_FRAGMENT_TAG = "main_fragment_tag";
    public static final String NEW_APPOINTMENT_FRAGMENT_TAG = "new_app_frag_tag";
    public static final String AVAILABLE_PRACTITIONER_FRAGMENT_TAG = "available_prac_frag_tag";

    //MyPatients onActivityResult request code
    public static final int ACTIVITY_RESULT_PATIENT_REGISTER = 131;
    public static final int ACTIVITY_RESULT_PATIENT_EDIT = 132;

    //MyAccount onActivityResult request code
    public static final int ACTIVITY_RESULT_ACCOUNT_UPDATE = 133;

    public static final int APPOINTMENT_STATUS_PENDING = 1;
    public static final int APPOINTMENT_STATUS_CONFIRMED = 2;
    public static final int APPOINTMENT_STATUS_COMPLETED = 3;

    public static final String PARCEL_APPOINTMENT_DETAILS = "parcel_appointment_details";
    public static final String PARCEL_APPOINTMENT_DETAILS_PATIENT = "parcel_appointment_patient";
    public static final String PARCEL_APPOINTMENT_DETAILS_PRACTITIONER = "parcel_appointment_practitioner";
    public static final String PARCEL_APPOINTMENT_DETAILS_CONDITIONS = "parcel_appointment_conditions";

    public static final int RESULT_NEUTRAL = 27;
}
