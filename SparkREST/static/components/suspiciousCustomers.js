Vue.component("suspiciousCustomers", {
	data: function() {
		return {
			customers: null,
			mode: "notLogged"
		}
	},
	template:`
		<div class="table-body">
			</br></br></br>
			<img class="logo" alt="" src="./images/logo1.png">
			<h1 class="item-name">Sumnjivi korisnici:</h1>
			<table class="table-all" id="customer-table">
                <thead >
                    <tr class="header">
                        <th>Ime</th>
                        <th>Prezime</th>
                        <th>Korisničko ime</th>
                        <th>Poeni</th>
                        <th>Tip kupca</th>
                        <th>Blokiran</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(c, index) in customers">
                        <td>{{c.name}}</td>
                        <td>{{c.surname}}</td>
                        <td>{{c.username}}</td>
                        <td>{{c.points}}</td>
                        <td>{{c.customerType.typeName}}</td>
                        <td>{{c.isBlocked}}</td>
                        <td><button v-on:click="blockCustomer(c)">Blokiraj</button></td>
                    </tr>
                </tbody>
            </table>
		</div>
	`,
	mounted() {
		axios
			.get('/rest/suspiciousCustomers')
			.then(response => (this.customers = response.data));

		axios
			.get('/rest/isLogged')
			.then(response => {
				if(response.data != null) {
					this.mode = response.data.role;
				} else {
					this.mode = "notLogged";
				}
			});

	},
	methods: {
		blockCustomer: function(customer) {
			axios
				.put('/rest/blockUser', customer.username)
				.then(response => (this.$router.go()));
		}
	}
});