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

    function getParameterByName(name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }

    $(document).ready(function() {
        client = Stomp.over(new SockJS('/chat'));
        client.connect({}, function (frame) {
            setConnected(true);
            var username = getParameterByName("username");
            var password = getParameterByName("password");
            client.subscribe('/broker/' + username, function (message) {
                showMessage(JSON.parse(message.body));
            });
            client.send("/app/login/" + username, {}, JSON.stringify({
                'content' : { username  + "/" + password
            }));
        });
    });

    $('#disconnect').click(function() {
        if (client != null) {
            client.disconnect();
            setConnected(false);
        }
        client = null;
    });

});