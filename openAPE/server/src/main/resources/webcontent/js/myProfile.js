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
				let
				pwOld = $("#iptPasswordOld").val();
				let
				pw1 = $("#iptPassword1").val();
				let
				pw2 = $("#iptPassword2").val();
				if (pw1 === pw2) {
					window.openape.changePassword(pwOld, pw1);
				}
			});
		});
