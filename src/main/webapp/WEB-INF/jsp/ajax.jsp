<%@page isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Timeline</title>
    <script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.4.1.min.js"></script>
    <h1>Timeline Discussion Board</h1>
</head>
<body>

<script type="text/javascript">
	var lastRefreshTime;
	var numberOfMessage;
	var get_three_more = function() {
		//alert('More Button Clicked');
		$.ajax({
			url : 'more',
			type: 'POST',
			data : { 'numberOfMessage' : numberOfMessage, 'lastRefreshTime' : lastRefreshTime },
			success : function(data) {
				numberOfMessage += 3;
				var div_content = "";
				var json = JSON.parse(data)
				var table_head = "<table width=\"400\" >";
				var table_tail = "</table>";
				for (var i=0; i<json.length; ++i) {
					var tr1 = "<tr height=\"25\">";
					var td11 = "<td width=\"250\">" + json[i]["_username"] + "</td>";
					var td12 = "<td width=\"150\">" + json[i]["_ago"] + "</td></tr>";
					var tr2 = "<tr height=\"0\">";
					var td21 =  "<td colspan=\"2\">" + json[i]["_content"] + "</td></tr>";
					var tr3 = "<tr height=\"0\">";
					var td31="<td colspan='2'>"+"<img src='http://localhost:8080/lab02-timeline/img/" +
							json[i]["_uuidstr"]+
							".jpg' alt='---Image here.---' width='192' height='108' />"+"</td></tr>";
					var tr4 = "<tr><td colspan=\"2\"><hr></td></tr>";
					if(json[i]["_path"]){
						div_content+=(tr1+td11+td12+tr2+td21+tr3+td31+tr4);
					} else {
						div_content += (tr1+td11+td12+tr2+td21+tr4);
					}
				}
				div_content = table_head + div_content + table_tail;
				$("#id_dynamic_div").html(div_content);
			}
		});
	};
	var check_form = function(event) {
		if (!($("#id_username").val() && $("#id_content").val())) {
			$("#id_error_message").html("<font color=\"red\">Username and content cannot be empty!</font>");
			event.preventDefault();
		}
		if ($("#id_username").val().length>15 || $("#id_content").val().length>140) {
			$("#id_error_message").html("<font color=\"red\">Username or content cannot be too long!(username <=15 and content<=140)</font>");
			event.preventDefault();
		}
	};
	$(window.documemt).ready(function() {
		//alert(${lastRefreshTime});
		lastRefreshTime = ${lastRefreshTime};
		numberOfMessage = ${numberOfMessage};
		get_three_more();
		setInterval(updateAjaxWrapper, 5000);
		function updateAjaxWrapper() {
			$.ajax({
				url : 'update',
				type: 'POST',
				data : { 'lastRefreshTime' : lastRefreshTime },
				success : function(data) {
					$("#id_update").html(data);
				}
			});
		}
		$("#id_more").click(get_three_more);
		$("#id_new_message_form").submit(check_form);
	});
</script>
<div style="height:400;width:500;overflow:auto">
    <table id="id_table">
        <tr height="25">
            <td width="250">Messages</td>
            <td width="150">
                <button id="id_update" onclick="window.location.reload();">0 Update(s)</button>
            </td>
        </tr>
    </table>
    <div id="id_dynamic_div"></div>
</div>
<button id="id_more">More</button>
<br>

<form id="id_new_message_form" action="newMessage" method="post" enctype="multipart/form-data">
    <table width="500">
        <tr>
            <td>Username</td>
        </tr>
        <tr>
            <td>
                <input id="id_username" type="text" name="username" size="25"/>
            </td>
        </tr>
        <tr>
            <td>What do you want to say?</td>
        </tr>
        <tr>
            <td>
                <textarea id="id_content" name="content" form="id_new_message_form" rows="5"
                          cols="50"></textarea>
            </td>
        </tr>
        <tr>
            <td>
                <input id="id_image" name="image" type="file" accept=".jpg"/>
            </td>
        </tr>
        <tr>
            <td>
                <input type="submit" value="Submit">
            </td>
        </tr>
        <tr>
            <td id="id_error_message">
            </td>
        </tr>
    </table>
</form>
</body>
</html>