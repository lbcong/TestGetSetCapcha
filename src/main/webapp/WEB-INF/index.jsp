<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.0.3/sockjs.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    </head>
    <body onload="disconnect()">
        <noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being enabled. Please enable
            Javascript and reload this page!</h2></noscript>
        <h1>Hello World!</h1>
        <s:url value="startProxy" var="startProxy"/>
        <s:url value="stopProxy" var="stopProxy"/>
        <s:url value="startAuto" var="startAuto"/>
        <p id="status_proxy"></p>
        <div>
            <input id="start_proxy" type="button" value="start_proxy" />
        </div>
        <div>
            <div>
                <button id="connect" onclick="connect();">Connect</button>
                <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
            </div>
            <div id="conversationDiv">
                <p id="response"></p>
            </div>
        </div>
        <br/>
        <br/>
        <br/>
        <br/>
        <input id="stop_proxy" type="button" value="stop_proxy" />
        <br/>
        <br/>
        <br/>
        <br/>
        <input id="start_auto" type="button" value="start_auto" />
        <div>
            <p id="status_auto"></p>
        </div>
        <div>
            <p>img capcha</p>
            <img id="img_capcha" alt="Red dot" />
        </div> 
        <div>
            <textarea style="height: 300px;width: 400px" id="error"></textarea>
        </div>
        <script type="text/javascript">
            var stompClient = null;

            function setConnected(connected) {
                document.getElementById('connect').disabled = connected;
                document.getElementById('disconnect').disabled = !connected;
                document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
                document.getElementById('response').innerHTML = '';
            }

            function connect() {
                var url = (window.location.protocol === "https:" ? "https:" : "http:") + "//" + window.location.host + window.location.pathname;
                var socket = new SockJS(url + '/hello');
                stompClient = Stomp.over(socket);
                stompClient.connect({}, function (frame) {
                    setConnected(true);
                    console.log('Connected: ' + frame);
                    stompClient.subscribe('/topic/greetings', function (greeting) {
                        showGreeting(greeting.body);
                    });
                    stompClient.subscribe('/auto/greetings', function (greeting) {
                        displayAutoStatus(greeting.body);
                    });
                    stompClient.subscribe('/error/greetings', function (greeting) {
                        displayError(greeting.body);
                    });
                });
            }

            function disconnect() {
                if (stompClient != null) {
                    stompClient.disconnect();
                }
                setConnected(false);
                console.log("Disconnected");
            }

            function showGreeting(message) {
                var response = document.getElementById('response');
                response.innerHTML = message;
            }

            function startProxy(input) {
                $.ajax({
                    type: "GET",
                    url: input,
                    timeout: 100000,
                    success: function (data) {
                        console.log("SUCCESS: ", data);
                        displayProxyStatus(data);

                    },
                    error: function (e) {
                        console.log("ERROR: ", e);
                        display(e);
                    }
                });
            }
            function stopProxy(input) {
                $.ajax({
                    type: "GET",
                    url: input,
                    timeout: 100000,
                    success: function (data) {
                        console.log("SUCCESS: ", data);
                    },
                    error: function (e) {
                        console.log("ERROR: ", e);
                        display(e);
                    }
                });
            }
            function startAuto(input) {
                $.ajax({
                    type: "GET",
                    url: input,
                    timeout: 100000,
                    success: function (data) {
                        console.log("SUCCESS: ", data);
                        displayAutoStatus(data);

                    },
                    error: function (e) {
                        console.log("ERROR: ", e);
                        display(e);
                    }
                });
            }
            $().ready(function () {
                $('#status_proxy').css('display', 'none');
                $('#img_capcha').css('display', 'none');
            });
            $().ready(function () {
                $('#start_proxy').click(function () {
                    startProxy("${startProxy}");
                });
            });
            $().ready(function () {
                $('#stop_proxy').click(function () {
                    stopProxy("${stopProxy}");
                });
            });
            $().ready(function () {
                $('#start_auto').click(function () {
                    startAuto("${startAuto}");
                });
            });
            function displayProxyStatus(data) {
                $('#status_proxy').text(data);
                $('#status_proxy').css('display', 'block');
            }
            function displayAutoStatus(data) {
                $('#status_auto').text(data);
                $('#status_auto').css('display', 'block');
            }
            function displayImg(data) {
                $('#img_capcha').attr('src', data);
                $('#img_capcha').css('display', 'block');
            }
            function displayError(data) {
                var psconsole = $('#error');
                psconsole.append("\n" + data);
                if (psconsole.length)
                    psconsole.scrollTop(psconsole[0].scrollHeight - psconsole.height());
            }
        </script>
    </body>
</html>
