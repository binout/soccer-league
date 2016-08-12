package io.github.binout.soccer.infrastructure.mail;

import feign.*;
import io.github.binout.soccer.infrastructure.log.LoggerService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

class SendGridMailService implements MailService {

    private static final String SENDGRID_API = "https://api.sendgrid.com/v3";

    @Inject
    LoggerService loggerService;

    @Override
    public void sendMail(Mail email) {
        String apiKey = System.getenv("SENDGRID_API_KEY");
        if (apiKey != null) {
            if (email.hasRecipients()) {
                Feign.builder()
                        .requestInterceptor(r -> r.header("Authorization", "Bearer " + apiKey))
                        .target(SendGrid.class, SENDGRID_API)
                        .sendMail(toSendGridMail(email));
            }
        } else {
            loggerService.log(this.getClass(), "No SendGrid Configuration");
        }
    }

    static String toSendGridMail(Mail email) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("from", toEmail(email.from()));
            jsonObject.put("personalizations", toPersonalizations(email));
            jsonObject.put("content", toContent(email));
            return jsonObject.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static JSONArray toContent(Mail email) {
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "text/html");
            jsonObject.put("value", email.content());
            jsonArray.put(jsonObject);
            return jsonArray;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static JSONArray toPersonalizations(Mail email) {
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("to", toTos(email));
            jsonObject.put("subject", email.subject());
            jsonArray.put(jsonObject);
            return jsonArray;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static JSONArray toTos(Mail email) {
        JSONArray tos = new JSONArray();
        email.recipients().map(SendGridMailService::toEmail).forEach(tos::put);
        return tos;
    }

    private static JSONObject toEmail(String address) {
        try {
            return new JSONObject().put("email", address);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    interface SendGrid {

        @Headers("Content-Type: application/json")
        @RequestLine("POST /mail/send")
        void sendMail(String body);
    }
}