$(document).ready(
		function() {
			var userdata = JSON.parse(openape.getUser(localStorage
					.getItem("token")).responseText);
			localStorage.setItem("username", userdata.username);
			localStorage.setItem("userid", userdata.id);
			if (userdata.roles.includes("admin")) {
				localStorage.setItem("role", "admin");
			}

			$(".btnChangePassword").click(function() {
				let
				pwOld = $(".iptPasswordOld").getValue();
				let
				pw1 = $(".iptPassword1").getValue();
				let
				pw2 = $(".iptPassword2").getValue();
				if (pw1 == pw2) {
					window.openape.changePassword(pwOld, pw1);
				}
			});
		});
