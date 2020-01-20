var errorHandler;
var supportsLoader = false;
var msgInfo = function (msg) {
    console.log(msg);
}

function showDebug(http, textStatus, message, responseBody) {
    var metaData = "Http Error: " + http + "<br>";
    metaData += "Text Status: " + textStatus + "<br>";
    metaData += "Backend Error: " + message + "<br>";
    var body = createModal({
        title: "Debug Backend Error",
        size: "large",
    }).find(".modal-body");
    $("<p/>").html(metaData).appendTo(body);

    var errorBody = $(responseBody);
    var raw = errorBody.find("pre code").remove();

    errorBody.appendTo(body);
    $("<div/>").css("cssText", "text-align: left !important").text(raw).appendTo(body);
}

function createDebugButton(message, body, errorType, httpErrorMsg, httpError) {
    var http = httpErrorMsg + " (" + httpError + ")";
    var fun = "showDebug('" + http + "', '" + errorType + "', '" + message + "', `" + body + "`)";
    return '<a href="#" onclick="' + fun + ';return false;">Debug</a>';
}

function setUpAlertifyErrorHandler() {
    errorHandler = function(message, body, errorType, httpErrorMsg, httpError) {
        var debug = " " + createDebugButton(message, body, errorType, httpErrorMsg, httpError);
        if (!isEmpty(message)) {
            alertify.error(message + debug);
        } else if (errorThrown) {
            alertify.error(errorThrown + debug);
        } else {
            alertify.error("Action failed!" + debug);
        }
        console.error(message + " stauts: " + errorType + " http: " + httpErrorMsg + "(" + httpError + ")");
    };
    msgInfo = function (msg) {
        alertify.success(msg);
    };
}

function makeFormSilent(query, data) {
    $(query).each(function() {
        let element = $(this);
        element.unbind("submit.silent");
        element.on("submit.silent", function(event) {
            data = $.extend({
                type: this.method,
                data: $(this).serialize(),
                success: function(data) {
                    handleSilentSucess(element, data);
                }
            }, data);
            cancelAndSendGET(this.action, event, data);
        });
    });
}

function makeSilent(query, data) {
    $(query).each(function() {
        let element = $(this);
        element.unbind("click.silent");
        element.on("click.silent", function(event) {
            var url = $(this).attr("data-href")
            if (url == undefined) {
                url = $(this).attr("href");
                if (url == undefined) {
                    console.log($(this) + " has not data-href or href attribute")
                }
            }
            data = $.extend({
                success: function(data) {
                   handleSilentSucess(element, data); 
                }
            }, data);
            cancelAndSendGET(url, event, data);
        });
    });
}

function handleSilentSucess(element, data) {
    var scope = {
        scope: this,
        data: data,
        element: element
    }
    with (scope) {
       eval(element.attr("data-onSuccess"));
    }
    let action = element.attr("data-action");
    if (action !== undefined) {
        msgInfo("Successfully " + action)
    }
    element.trigger("silentfinished", [data]);
}

function cancelAndSendGET(url, event, data) {
    if (event) {
        event.preventDefault();
    }
    sendGET(url, data)
}

function sendGET(url, data) {
    data = $.extend({
        url: url,
        cache: false,
        type: "get",
        error: function (xhr, errorType, httpErrorMsg) {
            var message = xhr.getResponseHeader("message");
            var body = xhr.responseText;
            window["errorHandler"](message, body, errorType, httpErrorMsg, xhr.status);
        }
    }, data);

    var ajax = $.ajax(data);
    
    if (supportsLoader) {
        var done = false, inLoader = false;
        ajax.done(function(data) {
            done = true;
            if (inLoader) {
                hideLoader();
            }
        });
        setTimeout(function(){
            if (!done) {
                showLoader();
                inLoader = true;
            }
        }, 1200);
    }
}

function loadText(url, handler, options) {
    var done = false, inLoader = false;
    fetch(url, options).then(function(response) {
        var message = response.headers.get("message");
        response.text().then(function(text) {
            if (response.ok) {
                handler(text, response);
            } else {
                errorHandler(message, text, "http-error", response.statusText, response.status);
            }
            done = true;
            if (inLoader) {
                hideLoader();
            }
        }).catch(function(err) {
            done = true;
            if (inLoader) {
                hideLoader();
            } 
            errorHandler(message, undefined, (err ? err : "read-error"), response.statusText, response.status);
        });
    }).catch(function(err) {  
        done = true;
        if (inLoader) {
            hideLoader();
        }
        errorHandler(undefined, undefined, (err ? err : "connect-error"), undefined, undefined);
    });
    if (supportsLoader) {
        setTimeout(function() {
            if (!done) {
                showLoader();
                inLoader = true;
            }
        }, 1200);
    }
}

function getWebSocket(url) {
    if (!window.WebSocket) {
        window.WebSocket = window.MozWebSocket;
    }
    if (window.WebSocket) {
        return new WebSocket(url);
    } else {
        return undefined;
    }
}