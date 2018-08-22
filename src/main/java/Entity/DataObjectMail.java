/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 *
 * @author Alex
 */
public class DataObjectMail {

    @JsonProperty("id")
    private String id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("status_iclound")
    private int status_iclound;
    @JsonProperty("status_facebook")
    private int status_facebook;
    @JsonProperty("password_iclound")
    private String password_iclound;
    @JsonProperty("password_facebook")
    private String password_facebook;
    @JsonProperty("status")
    private int status;
    @JsonProperty("created_at")
    private Date created_at;
    private Date updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   

    public String getPassword_iclound() {
        return password_iclound;
    }

    public void setPassword_iclound(String password_iclound) {
        this.password_iclound = password_iclound;
    }

    public String getPassword_facebook() {
        return password_facebook;
    }

    public void setPassword_facebook(String password_facebook) {
        this.password_facebook = password_facebook;
    }

    public int getStatus_iclound() {
        return status_iclound;
    }

    public void setStatus_iclound(int status_iclound) {
        this.status_iclound = status_iclound;
    }

    public int getStatus_facebook() {
        return status_facebook;
    }

    public void setStatus_facebook(int status_facebook) {
        this.status_facebook = status_facebook;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

   

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

}
