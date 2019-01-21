package io.github.splotycode.mosaik.webapi.response.error;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class ErrorDocument {

    private int errorCode = 500;
    private ResponseContent content;


}
