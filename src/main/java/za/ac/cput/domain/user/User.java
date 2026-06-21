/*
 User.java

 User POJO class

 Author: Joshua Reid Adams (230317693)

 Date: 21st June 2026
*/

package za.ac.cput.domain.user;

import jakarta.persistence.*;
import za.ac.cput.domain.enums.UserStatus;
import za.ac.cput.domain.valueObject.Name;

import java.time.LocalDate;

@MappedSuperclass
public abstract class User {

    //  Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Embedded
    private Name name;

    @Column(nullable = false, unique = true)
    private String email;
    private String cellPhone;

    @Column(nullable = false)
    private String password;
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private UserStatus accountStatus;

    protected User(){

    }

    protected User(Builder builder) {
        this.userId = builder.userId;
        this.name = builder.name;
        this.email = builder.email;
        this.cellPhone = builder.cellPhone;
        this.password = builder.password;
        this.dob = builder.dob;
        this.accountStatus = builder.accountStatus;
    }

    //  Getters
    public int getUserId() {
        return userId;
    }

    public Name getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getDob() {
        return dob;
    }

    public UserStatus getAccountStatus() {
        return accountStatus;
    }

    //  toString
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name=" + name +
                ", email='" + email + '\'' +
                ", cellPhone='" + cellPhone + '\'' +
                ", dob=" + dob +
                ", accountStatus=" + accountStatus +
                '}';
    }

    public static abstract class Builder{
        //  Variables
        protected int userId;
        protected Name name;
        protected String email;
        protected String cellPhone;
        protected String password;
        protected LocalDate dob;
        protected UserStatus accountStatus;

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setCellPhone(String cellPhone) {
            this.cellPhone = cellPhone;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setName(Name name) {
            this.name = name;
            return this;
        }

        public Builder setDob(LocalDate dob) {
            this.dob = dob;
            return this;
        }

        public Builder setAccountStatus(UserStatus accountStatus) {
            this.accountStatus = accountStatus;
            return this;
        }

        public Builder copy(User user){
            this.userId = user.userId;
            this.name = user.name;
            this.email = user.email;
            this.cellPhone = user.cellPhone;
            this.password = user.password;
            this.dob = user.dob;
            this.accountStatus = user.accountStatus;

            return this;
        }

        public abstract User build();
    }
}
