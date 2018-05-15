$(function() {
    'use strict';

    var client;

    function showMessage(mesg)
    {
        $('#messages').append('<tr>' +
            '<td>' + mesg.status + '</td>' +
            '</tr>');
    }

    function getParameterByName(name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
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
            var username = getParameterByName("username");
            var password = getParameterByName("password");
            var firstName = getParameterByName("firstName");
            var lastName = getParameterByName("lastName");
            var latitude = getParameterByName("latitude");
            var longitude = getParameterByName("longitude");
            client.subscribe('/broker/' + username, function (message) {
                showMessage(JSON.parse(message.body));
            });
            client.send("/app/register/" + username, {}, JSON.stringify({
                'content': {
                    'username' : username,
                    'password' : password,
                    'firstName' : firstName,
                    'lastName' : lastName,
                    'latitude' : latitude,
                    'longitude' : longitude
                }
            }))
        });
    });
});