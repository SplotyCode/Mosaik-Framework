package me.david.webapi.response.content.manipulate;

import me.david.webapi.response.content.ResponseContent;

public interface ManipulateableContent extends ResponseContent {

    ResponseManipulator manipulate();

}
