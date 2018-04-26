$(document).ready(
		function() {
			var userdata = JSON.parse(openape.getUser(localStorage
					.getItem("token")).responseText);
			localStorage.setItem("username", userdata.username);
			localStorage.setItem("userid", userdata.id);
			if (userdata.roles.includes("admin")) {
				localStorage.setItem("role", "admin");
			}

			$("#btnChangePassword").click(function() {
				$('#changePWMainErrSection').empty();
				let
				pwOld = $("#iptPasswordOld").val();
				let
				pw1 = $("#iptPassword1").val();
				let
				pw2 = $("#iptPassword2").val();
				if (pw1 === pw2) {
					var response = window.openape.changePassword(pwOld, pw1);
					if(response.status == 403) {
						$('#changePWMainErrSection').append("<img src='img/attention_icon.png' width='20' height='20'> "+"Wrong old password");
					}
				} else {
					$('#changePWMainErrSection').append("<img src='img/attention_icon.png' width='20' height='20'> "+"Passwords don't match");
				}
			});
		});
