package com.mSIHAT.client.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestAppointmentService;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.FragmentCompletedCallback;
import com.mSIHAT.client.models.Feedback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alamchristian on 4/15/16.
 */
public class FeedbackDialog extends DialogFragment implements View.OnClickListener {
    private static final String ARG_APPOINTMENT_ID = "appointment_id";

    ProgressDialog progressDialog;

    RatingBar feedback_rating;
    EditText edit_comment;

    private int appointment_id;

    private Button btn_submit, btn_cancel;

    RestAppointmentService appointmentService;

    private FragmentCompletedCallback mListener;

    public FeedbackDialog(){

    }

    public static FeedbackDialog newInstance(int appointment_id){
        FeedbackDialog dialog = new FeedbackDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_APPOINTMENT_ID, appointment_id);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof FragmentCompletedCallback){
            mListener = (FragmentCompletedCallback) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement FragmentCompletedCallback");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        appointment_id = getArguments().getInt(ARG_APPOINTMENT_ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_appointment_feedback, null);
        findViewById(dialogView);
        builder.setView(dialogView).setTitle(R.string.submit_your_feedback);

        return builder.create();
    }

    private void findViewById(View dialogView){
        feedback_rating = (RatingBar) dialogView.findViewById(R.id.rating_appointment);
        edit_comment = (EditText) dialogView.findViewById(R.id.edit_feedback_comment);
        btn_submit = (Button) dialogView.findViewById(R.id.btn_feedback_submit);
        btn_submit.setOnClickListener(this);
        btn_cancel = (Button) dialogView.findViewById(R.id.btn_feedback_cancel);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_feedback_submit:
                submitFeedback();
                break;
            case R.id.btn_feedback_cancel:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    private void submitFeedback(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.submitting));
        progressDialog.show();

        appointmentService = new RestAppointmentService();
        Feedback feedback = new Feedback(appointment_id, (int) feedback_rating.getRating(),
                edit_comment.getText().toString());
        Call<Feedback> callFeedback = appointmentService.getService().postAppointmentFeedback(feedback);
        callFeedback.enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                if(response.code() == 201){
                    Toast.makeText(FeedbackDialog.this.getContext(),
                            R.string.thank_you_for_your_feedback, Toast.LENGTH_SHORT).show();
                    mListener.OnPostCompleted(true);
                    FeedbackDialog.this.dismiss();
                } else {
                    Toast.makeText(FeedbackDialog.this.getContext(),
                            "sdsds"+String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Feedback> call, Throwable t) {
                Toast.makeText(FeedbackDialog.this.getContext(),
                        R.string.failed, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
