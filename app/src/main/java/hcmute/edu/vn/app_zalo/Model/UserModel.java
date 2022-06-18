package hcmute.edu.vn.app_zalo.Model;

public class UserModel {
    private String uid,firstName,lastName,phone,bio;//uid:mã tài khoản, firstName: tên đầu , lastName:tên cuối , phone: số điện thoại, bio: trạng thái
    private double birthDate; //Ngày tháng năm sinh
    private boolean gender; //Biến kiểm tra

    public UserModel(){
        birthDate=System.currentTimeMillis();
        gender=true;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public double getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(double birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}
