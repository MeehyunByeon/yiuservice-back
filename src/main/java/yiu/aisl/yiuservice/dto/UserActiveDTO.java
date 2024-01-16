package yiu.aisl.yiuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActiveDTO {
    private List<DeliveryResponse> deliveryPosts;
    private List<Comment_DeliveryResponse> deliveryComments;
    private List<TaxiResponse> taxiPosts;
    private List<Comment_TaxiResponse> taxiComments;
}
