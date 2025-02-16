package cn.hjf.job.model.document.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "SDP")
public class RTCSessionDescriptionInit {
    private String sdp;
    private String type;
}
