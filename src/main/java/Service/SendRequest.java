/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import ConstantVariable.Constant;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import Entity.ObjectJson;
import com.google.gson.Gson;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SendRequest {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    Gson gson;
    @Autowired
    ProxyWithSSH proxyWithSSH;
    @Autowired
    Codenvy codenvy;

    public ObjectJson sendGet(String API_KEY, String ID_IMG) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Constant.Url_2Capcha_Get)
                    .queryParam("action", "get")
                    .queryParam("key", API_KEY)
                    .queryParam("id", ID_IMG)
                    .queryParam("json", "1");

            HttpEntity<?> entity = new HttpEntity<>(headers);
            //check connect 
            codenvy.checkConnect();
            HttpEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);
            return gson.fromJson(response.getBody(), ObjectJson.class);

        } catch (Exception e) {
            System.out.println("loi send get " + e.getMessage());
        }
        return null;
    }

    public ObjectJson sendPost(String API_KEY, String ImgBase64) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("method", "base64");
            map.add("key", API_KEY);
            map.add("body", ImgBase64);
            map.add("json", "1");

            HttpEntity<MultiValueMap<String, String>> requestx = new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);
            //check connect 
            codenvy.checkConnect();
            ResponseEntity<String> response1 = restTemplate.exchange(Constant.Url_2Capcha_Post, HttpMethod.POST, requestx, String.class);
            return gson.fromJson(response1.getBody(), ObjectJson.class);
        } catch (Exception e) {
            System.out.println("loi send post " + e.getMessage());
        }
        return null;
    }
}
