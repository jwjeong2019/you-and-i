<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>YouAndI Chat Room</title>
<style>
	#container {
		display: flex;
		flex-direction: column;
		width: 30%;
	}
	#inputbox {
		/* display: flex; */
		margin-top: 15px;
		justify-content: space-between;
		width: 100%;
	}
</style>
</head>
<body>
	<h1>Enjoy Chat!</h1>
	<div id="container">
		<textarea id="messageWindow" rows="20" cols="50" readonly="readonly"></textarea>
		<div id="inputbox">
			<div>
				<span>ID</span>
				<input id="inputUser" type="text">
			</div>
			<div>
				<span>MESSAGE</span>
				<input id="message" type="text">
				<input type="button" value="Send" onclick="sendMessage()">
				<input type="button" value="Disconnect" onclick="disconnect()">
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var webSocket = new WebSocket('ws://localhost:8080/youandi/broadsocket');
	var messageWindow = document.getElementById('messageWindow');
	
	webSocket.onopen = function(message) {
		messageWindow.value += 'Connecting Server...\n';
	};
	webSocket.onmessage = function(message) {
		messageWindow.value += message.data + '\n';
	};
	webSocket.onerror = function (message) {
		messageWindow.value += 'error...\n';
	};
	webSocket.onclose = function (message) {
		messageWindow.value += 'Disconnecting Server...\n';
	};
	
	function sendMessage() {
		var inputUser = document.getElementById("inputUser");
		var message = document.getElementById("message");
		
		messageWindow.value += inputUser.value + '(me): ' + message.value + '\n';
		
		webSocket.send('{{' + inputUser.value + '}}' + message.value);
		
		message.value = '';
	}
	function disconnect() {
		webSocket.close();
	}
</script>
</html>