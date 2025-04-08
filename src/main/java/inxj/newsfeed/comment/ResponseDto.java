package comment;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseDto {
    Long id;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}
