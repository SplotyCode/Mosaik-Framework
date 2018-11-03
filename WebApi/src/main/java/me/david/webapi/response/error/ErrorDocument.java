package me.david.webapi.response.error;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.david.webapi.response.content.ResponseContent;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class ErrorDocument {

    private int errorCode = 500;
    private ResponseContent content;


}
