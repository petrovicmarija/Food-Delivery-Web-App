Vue.component("login", {
	data: function() {
		return {
			mode: "BROWSE",
			usernameInput: '',
			passwordInput: '',
			errorMessage: ''
		}
	},
	template: `
		<div>
			<img alt="" src="./images/logo1.png">
			<h1 style="color: #99CCFF; text-align: center; font-size: 50"></h1>
			<form class="add-form">
				<label>Korisničko ime:</label>
				<input type="text" v-model="usernameInput" name="username" required/>

				<label>Lozinka:</label>
				<input type="password" v-model="passwordInput" name="password" required/>

				<input type="submit" v-on:click="tryLogin" value="Log In"/>

				<p style="color:red;text-transform:none;">{{errorMessage}}</p>
			</form>
		</div>
	`,
	methods: {
		tryLogin : function() {
			event.preventDefault();
			let loginParameters = {
				username: this.usernameInput,
				password: this.passwordInput
			};

			axios
				.post('/rest/login', JSON.stringify(loginParameters))
				.then(response => {
					if(response.data == "") {
						this.errorMessage = "Uneli ste pogrešno korisničko ime ili lozinku!";
					} else {
						this.mode = "LoggedIn";
						router.push(`/`);
					}
				});

			axios
				.get('/rest/isDeleted/' + this.usernameInput)
				.then(response => {
					if(response.data == 'YES') {
						this.errorMessage = "Nalog deaktiviran!";
					}
				});
				
			axios
				.get('/rest/isBlocked/' + this.usernameInput)
				.then(response => {
					if(response.data == 'YES') {
						this.errorMessage = "Blokirani ste!";
					}
				});

		}
	}
});
