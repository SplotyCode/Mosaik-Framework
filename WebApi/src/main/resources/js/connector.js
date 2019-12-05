var errorHandler;

function showDebug(http, textStatus, message, responseBody) {
    var body = "Http Error: " + http + "<br>";
    body += "Text Status: " + textStatus + "<br>";
    body += "Backend Error: " + message + "<br><br>";
    body += "<div style='text-align: left !important;'>" + responseBody + "</div>";
    createModal({
        title: "Debug Backend Error",
        body: body,
        size: "large",
    });
}

function createDebugButton(xhr, textStatus, errorThrown) {
    var http = errorThrown + " (" + xhr.status + ")";
    var fun = "showDebug('" + http + "', '" + textStatus + "', '" + xhr.getResponseHeader('message') + "', `" + xhr.responseText + "`)";
    return '<a href="#" onclick="' + fun + ';return false;">Debug</a>';
}

function setUpAlertifyErrorHandler() {
    errorHandler = function(xhr, textStatus, errorThrown) {
        var message;
        var debug = " " + createDebugButton(xhr, textStatus, errorThrown);
        if (xhr && !isEmpty(message = xhr.getResponseHeader('message'))) {
            alertify.error(message + debug);
        } else if (errorThrown) {
            alertify.error(errorThrown + debug);
        } else {
            alertify.error("Action failed!" + debug);
        }
    };
}

function makeFormSilent(query, data) {
    $(query).each(function() {
        $(this).on('submit', function(event) {        
            data = $.extend({
                type: this.method,
                data: $(this).serialize(),
                success: function(data) {
                    eval($(this).attr("onSuccess"));
                }
            }, data);
            sendGET(this.action, event, data);
        });
    });
}

function makeSilent(query, data) {
    $(query).each(function() {
        $(this).click(function(event) {
            var url = $(this).attr("data-href")
            if (url == undefined) {
                url = $(this).attr("href");
                if (url == undefined) {
                    console.log($(this) + " has not data-href or href attribute")
                }
            }
            data = $.extend({
                success: function(data) {
                    eval($(this).attr("onSuccess"));
                }
            }, data);
            cancelAndSendGET(url, event, data);
        });
    });
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
        error: function (xhr, textStatus, errorThrown) {
            window["errorHandler"](xhr, textStatus, errorThrown);
        }
    }, data);
    
    $.ajax(data);
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