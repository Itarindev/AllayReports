package allayplugins.stompado.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {

    private String id, name;
    private int count;
    private boolean reported;

}