function sortManagerTable(n) {
	var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
	table = document.getElementById("manager-table");
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

function filterManagerTable(event) {
	var filter = event.target.value.toUpperCase();
	var rows = document.querySelector("#manager-table tbody").rows;

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

Vue.component("managers", {
	data: function() {
		return {
			managers: null,
			mode: "notLogged"
		}
	},
	template:`
		<div class="table-body">
			</br></br></br>
			<img class="logo" alt="" src="./images/logo1.png">

			<div class="search-div">
				<input type="text" id="search-input" placeholder="Search for managers.."  />
			</div>

			<table class="table-all" id="manager-table">
                <thead>
                    <tr class="header">
                        <th onclick="sortManagerTable(0)">Ime</th>
                        <th onclick="sortManagerTable(1)">Prezime</th>
                        <th onclick="sortManagerTable(2)">Korisničko ime</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(m, index) in managers">
                        <td>{{m.name}}</td>
                        <td>{{m.surname}}</td>
                        <td>{{m.username}}</td>
                        <td><button v-on:click="deleteManager(m)">Obriši</button></td>
                    </tr>
                </tbody>
            </table>
		</div>
	`,
	mounted() {
		axios
			.get('/rest/activeManagers')
			.then(response => (this.managers = response.data));

		axios
			.get('/rest/isLogged')
			.then(response => {
				if(response.data != null) {
					this.mode = response.data.role;
				} else {
					this.mode = "notLogged";
				}
			});

		document.querySelector('#search-input').addEventListener('keyup', filterManagerTable, false);

	},
	methods: {
		deleteManager: function(manager) {
			axios
				.delete('/rest/deleteUser/' + manager.username)
				.then(response => (this.$router.go()));
		}
	}
});
