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

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $(document).ready(function() {
        client = Stomp.over(new SockJS('/chat'));
        client.connect({}, function (frame) {
            setConnected(true);
            client.subscribe('/broker/messages', function (message) {
                showMessage(JSON.parse(message.body));
            });
        });
    });

    $('#disconnect').click(function() {
        if (client != null) {
            client.disconnect();
            setConnected(false);
        }
        client = null;
    });

    $('#send').click(function() {
        var topic = "Login";
        client.send("/app/chat/" + topic, {}, JSON.stringify({
            'content': {
                username: $("#loginUsername").val(), password: $('#loginPassword').val()
            }}));
        $('#loginUsername').val("");
        $('#loginPassword').val("");
    });
});