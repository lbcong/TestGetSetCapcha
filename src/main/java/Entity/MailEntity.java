/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author Alex
 */
public class MailEntity {

    @JsonProperty("data")
    List<DataObjectMail> data;
    @JsonProperty("message")
    String message;
}
