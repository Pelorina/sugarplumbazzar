//package Ecommerce.sugar.plum.bazzar.Utils;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//@RestController
//@RequestMapping("/email")
//public class EmailVerification {
//    private static final String API_URL ="https://emailchecker-email-verification-v1.p.rapidapi.com/api/a/v1?%7Bkey%7D=yourapikey&test%40example.com=test%40example.com\"";
//    private static final String API_KEY = "660a5648f2msh175b920c0ee1419p147b9bjsn2edfadcbba36";
//    private static final String API_KEY_NAME = "X-RapidAPI-Key";
//    private static final String API_HOST = "emailchecker-email-verification-v1.p.rapidapi.com";
//    private static final String API_HOST_NAME = "X-RapidAPI-Host";
//
//    private final OkHttpClient client = new OkHttpClient();
//
//    @GetMapping("/check")
//    public String caloriesBurn() throws IOException {
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .get()
//                .addHeader(API_KEY_NAME, API_KEY)
//                .addHeader(API_HOST_NAME, API_HOST)
//                .build();
//        try (Response response = client.newCall(request).execute()) {
//            if (response.isSuccessful() && response.body() != null) {
//                return response.body().string();
//            } else {
//                throw new IOException("Request failed: " + response);
//            }
//        }
//    }
//}
//
//
//
