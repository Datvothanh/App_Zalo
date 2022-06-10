package hcmute.edu.vn.app_zalo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import hcmute.edu.vn.app_zalo.Common.Common;
import hcmute.edu.vn.app_zalo.Model.UserModel;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.edt_first_name)
    TextInputEditText edt_first_name;
    @BindView(R.id.edt_last_name)
    TextInputEditText edt_last_name;
    @BindView(R.id.edt_phone)
    TextInputEditText edt_phone;
    @BindView(R.id.edt_date_of_birth)
    TextInputEditText edt_date_of_birth;
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.edt_bio)
    TextInputEditText edt_bio;

    FirebaseDatabase database;
    DatabaseReference userRef;

    MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .build();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    Calendar calendar = Calendar.getInstance();
    boolean isSelectBirthDate= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        setDefaultData();
    }

    private void setDefaultData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        edt_phone.setText(user.getPhoneNumber());
        edt_phone.setEnabled(false);

        edt_date_of_birth.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus)
                materialDatePicker.show(getSupportFragmentManager(),materialDatePicker.toString());
        });

        btn_register.setOnClickListener(v -> {
            if(!isSelectBirthDate){
                Toast.makeText(this, "Please enter birthdate", Toast.LENGTH_SHORT);
                return;
            }
            UserModel userModel = new UserModel();

            userModel.setFirstName(edt_first_name.getText().toString());
            userModel.setLastName(edt_last_name.getText().toString());
            userModel.setBio(edt_bio.getText().toString());
            userModel.setPhone(edt_phone.getText().toString());
            userModel.setBirthDate(calendar.getTimeInMillis());
            userModel.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userRef.child(userModel.getUid())
                    .setValue(userModel)
                    .addOnFailureListener(e ->{
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnSuccessListener(aVoid ->{
                        Toast.makeText(this, "Register success!", Toast.LENGTH_SHORT).show();
                        Common.currentUser = userModel;
                        startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                        finish();
                    });
        });
    }

    private void init(){
        ButterKnife.bind(this);
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Common.USER_REFERENCES);
        materialDatePicker.addOnPositiveButtonClickListener(selection ->  {
          calendar.setTimeInMillis(selection);
          edt_date_of_birth.setText(simpleDateFormat.format(selection));
          isSelectBirthDate=true;
        });
    }

}