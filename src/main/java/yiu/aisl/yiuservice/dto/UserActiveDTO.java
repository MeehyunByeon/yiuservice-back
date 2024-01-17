package yiu.aisl.yiuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActiveDTO {
    private List<DeliveryResponse> delivery;
    private List<Comment_DeliveryResponse> deliveryComments;
    private List<TaxiResponse> taxi;
    private List<Comment_TaxiResponse> taxiComments;
}
