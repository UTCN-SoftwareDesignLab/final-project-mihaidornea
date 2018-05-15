$(function() {
    'use strict';

    var client;

    function showMessage(mesg)
    {
        $('#messages').append('<tr>' +
            '<td>' + mesg.status + '</td>' +
            '</tr>');
    }

    function setConnected(connected) {
        $("#connect").prop("disabled", connected);
        $("#disconnect").prop("disabled", !connected);
        $('#from').prop('disabled', connected);
        $('#text').prop('disabled', !connected);
        if (connected) {
            $("#conversation").show();
            $('#text').focus();
        }
        else $("#conversation").hide();
        $("#messages").html("");
    }

    $(document).ready(function() {
        client = Stomp.over(new SockJS('/chat'));
        client.connect({}, function (frame) {
            setConnected(true);
            client.subscribe('/broker/messages', function (message) {
                showMessage(JSON.parse(message.body));
            });
        });
    });


    $('#send').click(function() {
        var topic = "Register";
        client.send("/app/chat/" + topic, {}, JSON.stringify({
            'content': $("#username").val()
            + "/" + $("#password").val()
            + "/" + $("#firstName").val()
            + "/" + $("#lastName").val()
            + "/" + $("#latitude").val()
            + "/" + $("#longitude").val()
        }));
        $("#username").val("")
        $("#password").val("")
        $("#firstName").val("")
        $("#lastName").val("")
        $("#latitude").val("")
        $("#longitude").val("")

    });
});