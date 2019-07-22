var errorHandler;

function makeSilent(query) {
    $(query).each(function(){
        $(this).on('click', function(e) {
            var url = $(this).attr("data-href")
            if (url == undefined) {
                url = $(this).attr("href");
                if (url == undefined) {
                    console.log($(this) + " has not href attribute")
                }
            }
            sendGETClick(url, e)
        });
    });
}

function sendGETClick(url, event) {
    event.preventDefault();
    sendGET(url)
}

function sendGET(url) {
    $.ajax({
        url: $(this).attr("href"),
        type: 'get'
    }).fail(errorHandler);
}

function getWebSocket(url) {
    if (!window.WebSocket) {
      window.WebSocket = window.MozWebSocket;
    }
    if (window.WebSocket) {
        return new WebSocket(url)
    } else {
        console.log("web socket unsupported");
    }
}