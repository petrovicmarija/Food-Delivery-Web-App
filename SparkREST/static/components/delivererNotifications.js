Vue.component("delivererNotifications", {
	data: function() {
		return {
			notifications: null,
			empty: ""
		}
	},
	template: `
	<div>
		<img class="logo" src="./images/logo1.png">
		<div class="table-body">
			<div>
				<h1 class="item-name"> {{this.empty}}</h1>
	  		</div>
			<table class="table-all" id="customer-table" v-bind:hidden="isEmpty(this.empty)">
                <thead>
                    <tr class="header">
                        <th>ID porudžbine</th>
                        <th>Sadržaj</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(n, index) in notifications">
                        <td>{{n.orderId}}</td>
                        <td>{{n.content}}</td>
                        <td><button v-on:click="editNotificationStatus(n)">OK</button></td>
                    </tr>
                </tbody>
            </table>
		</div>
	</div>
	`,
	mounted() {
		event.preventDefault();
		axios
			.get('/rest/getDelivererNotifications')
			.then(response => {
				if(response.data == 'Empty') {
					this.empty = "Nemate novih obaveštenja.";
				} else {
					this.notifications = response.data;
				}
			});
	},
	methods: {
		editNotificationStatus: function(notification) {
			event.preventDefault();
			axios
				.put('/rest/editNotificationStatus', notification)
				.then(response => (this.$router.go()));
		},
		isEmpty: function(empty) {
			if(empty == '') {
				return false;
			} else {
				return true;
			}
		}
		
	}
});