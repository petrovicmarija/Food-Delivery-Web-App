Vue.component("customersForManager", {
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
			<table class="table-all" id="customer-table">
                <thead >
                    <tr class="header">
                        <th>Ime</th>
                        <th>Prezime</th>
                        <th>Korisničko ime</th>
                        <th>Poeni</th>
                        <th>Tip kupca</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(c, index) in customers">
                        <td>{{c.name}}</td>
                        <td>{{c.surname}}</td>
                        <td>{{c.username}}</td>
                        <td>{{c.points}}</td>
                        <td>{{c.customerType.typeName}}</td>
                    </tr>
                </tbody>
            </table>
		</div>
	`,
	mounted() {
		axios
			.get('/rest/getCustomersForManager')
			.then(response => (this.customers = response.data));
	}
});