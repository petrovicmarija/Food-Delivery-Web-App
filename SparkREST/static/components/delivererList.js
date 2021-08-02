function sortDelivererTable(n) {
	var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
	table = document.getElementById("deliverer-table");
	switching = true;
	dir = "asc";

	while(switching) {
		switching = false;
		rows = table.rows;

		for (i = 1; i < (rows.length - 1); i++) {
			shouldSwitch = false;
			x = rows[i].getElementsByTagName("td")[n];
			y = rows[i + 1].getElementsByTagName("td")[n];

			if (dir == "asc") {
				if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
					shouldSwitch = true;
					break;
				}
			} else if (dir == "desc") {
				if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
					shouldSwitch = true;
					break;
				}
			}
		}
		if (shouldSwitch) {
			rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
			switching = true;
			switchcount ++;
		} else {
			if (switchcount == 0 && dir == "asc") {
				dir = "desc";
				switching = true;
			}
		}
	}
}

function filterDelivererTable(event) {
	var filter = event.target.value.toUpperCase();
	var rows = document.querySelector("#deliverer-table tbody").rows;

	for (var i = 0; i < rows.length; i++) {
		var firstCol = rows[i].cells[0].textContent.toUpperCase();
		var secondCol = rows[i].cells[1].textContent.toUpperCase();
		var thirdCol = rows[i].cells[2].textContent.toUpperCase();

		if (firstCol.indexOf(filter) > -1 || secondCol.indexOf(filter) > -1 || thirdCol.indexOf(filter) > -1) {
			rows[i].style.display = "";
		} else {
			rows[i].style.display = "none";
		}
	}
}

Vue.component("deliverers", {
	data: function() {
		return {
			deliverers: null,
			mode: "notLogged"
		}
	},
	template:`
		<div class="table-body">
			</br></br></br>
			<img class="logo" alt="" src="./images/logo1.png">

			<div class="search-div">
				<input type="text" id="search-input" placeholder="Search for deliverers.."  />
			</div>

			<table class="table-all" id="deliverer-table">
                <thead>
                    <tr class="header">
                        <th onclick="sortDelivererTable(0)">Ime</th>
                        <th onclick="sortDelivererTable(1)">Prezime</th>
                        <th onclick="sortDelivererTable(2)">Korisničko ime</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(d, index) in deliverers">
                        <td>{{d.name}}</td>
                        <td>{{d.surname}}</td>
                        <td>{{d.username}}</td>
                        <td><button v-on:click="deleteDeliverer(d)">Obriši</button></td>
                    </tr>
                </tbody>
            </table>
		</div>
	`,
	mounted() {
		axios
			.get('/rest/activeDeliverers')
			.then(response => (this.deliverers = response.data));

		axios
			.get('/rest/isLogged')
			.then(response => {
				if(response.data != null) {
					this.mode = response.data.role;
				} else {
					this.mode = "notLogged";
				}
			});

			document.querySelector('#search-input').addEventListener('keyup', filterDelivererTable, false);

	},
	methods: {
		deleteDeliverer: function(deliverer) {
			axios
				.delete('/rest/deleteUser/' + deliverer.username)
				.then(response => (this.$router.go()));
		}
	}
});
