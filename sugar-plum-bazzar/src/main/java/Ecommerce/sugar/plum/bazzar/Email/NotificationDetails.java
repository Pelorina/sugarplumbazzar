package Ecommerce.sugar.plum.bazzar.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationDetails {
    private String recipient;
    private String subject;
    private String messageBody;
}
