function removeFromArray(arr, value) {
    return arr.filter(function(ele) {
      return ele != value;
    });
  }

Vue.component("customer_orders", {
	data: function() {
		return {
			orders: null,
			filters_show: false,
			showSearch: false,
			ascending: false,
			restaurantName: "",
			startPrice: 0,
			endPrice: 0,
			startDate: "",
			endDate: "",
			sortBy: "",
			ascending: false,
      type: [],
      status: []
		}
	},
	template: `
		<div>
			<div class = "search-form">
				<input type="text" v-model="restaurantName" placeholder="Naziv restorana" />
				<input type="date" v-model="startDate"/>
				<input type="date" v-model="endDate"/>
				<input type="number" placeholder="Cena (od)" v-model="startPrice"/>
				<input type="number" placeholder="Cena (do)" v-model="endPrice"/>
				<button v-on:click="searchOrders" class="search-button"> <img class="search-image" src="./images/search.png"> </button> <br>

				<a href="#search_restaurant" @click="showFilters" style="margin-left: 41px;"> Filteri </a>
				<div v-bind:hidden="filters_show == false">
				<div v-bind:hidden="filters_show == false">
					<h1 class="filter-restaurants"> Tip restorana: </h1>
					<div class="byType">
					<label class="container"> Italian
						<input type="checkbox" value="italian" name="type" id="italian">
						<span class="checkmark"></span>
					</label>

					<label class="container"> Chinese
						<input type="checkbox" value="chinese" name="type" id="chinese">
						<span class="checkmark"></span>
					</label>

					<label class="container"> Vegan
						<input type="checkbox" value="vegan" name="type" id="vegan">
						<span class="checkmark"></span>
					</label>

					<label class="container"> Grill
						<input type="checkbox" value="grill" name="type" id="grill">
						<span class="checkmark"></span>
					</label>

					<label class="container"> Pizza
						<input type="checkbox" value="pizza" name="type" id="pizza">
						<span class="checkmark"></span>
					</label>

					<label class="container"> Mexican
						<input type="checkbox" value="mexican" name="type" id="mexican">
						<span class="checkmark"></span>
					</label>
					</div> <br>

          <div class="byType">
          <label class="container"> Processing
            <input type="checkbox" value="processing" name="status" id="processing">
            <span class="checkmark"></span>
          </label>

          <label class="container"> Preparing
            <input type="checkbox" value="preparing" name="status" id="preparing">
            <span class="checkmark"></span>
          </label>

          <label class="container"> Waiting
            <input type="checkbox" value="waiting" name="status" id="waiting">
            <span class="checkmark"></span>
          </label>

          <label class="container"> Transport
            <input type="checkbox" value="transport" name="status" id="transport">
            <span class="checkmark"></span>
          </label>

          <label class="container"> Delivered
            <input type="checkbox" value="delivered" name="status" id="delivered">
            <span class="checkmark"></span>
          </label>

          <label class="container"> Canceled
            <input type="checkbox" value="canceled" name="status" id="canceled">
            <span class="checkmark"></span>
          </label>
          </div>

					<button v-on:click="filterOrders" style="margin-left: 41px;"> primeni filter </button>
				</div>
				</div>

				<select class="sort-cb" v-model="sortBy" v-on:change="sortOrders">
					<option value="" disabled selected> Sortiraj </option>
					<option value="nameAsc"> Po nazivu restorana (rastuće) </option>
					<option value="nameDesc"> Po nazivu restorana (opadajuće) </option>
					<option value="priceAsc"> Po ceni (rastuće) </option>
					<option value="priceDesc"> Po ceni (opadajuće) </option>
					<option value="dateAsc"> Po datumu (rastuće) </option>
					<option value="dateDesc"> Po datumu (opadajuće) </option>
				</select>

			</div>
		  <button style="position: absolute; top: 10px; right: 40px;" v-on:click="getUndeliveredOrders">Nedostavljene porudžbine</button>
		  <div class="row-items" v-for="(o, index) in orders">
		    <div class="col-with-pic"> </br>
	          <div class="col-picture">
	            <div>
	          <img :src="o.restLogo" class="restaurant-image"> </br> </br>
	            </div>
	          </div>
	        </div>
		    <div class="col-information">
		      <h1 class="item-name"> {{o.restaurant}}</h1>
		      <h1 class="price"> {{o.price}},00 RSD </h1>
		      <div v-for="(i, index) in o.orderedItems" >
		  		 <label>&nbsp; &nbsp; &nbsp; &nbsp;{{i.name}} x{{i.amount}}</label>
		  	  </div>
		    </div>
		    <div>
	    		</br></br></br></br></br>
	    		<button v-bind:hidden="isDisabled(o)" v-on:click="cancelOrder(o)"> Otkaži porudžbinu </button>
					<button class="see-more"> <a :href="'#/addComment?name=' + o.restaurant" class="link"> Ostavi komentar </a> </button>
	    	</div>
		  </div>
		 </div>
	`,
	mounted() {
		axios
			.get('/rest/getCustomerOrders')
			.then(response => {
				this.orders = response.data;
			});

	},
	methods: {
		isDisabled: function(order) {
			if(order.status == 'processing') {
				return false;
			} else {
				return true;
			}
		},
		cancelOrder: function(order) {
			axios
				.post('/rest/cancelOrder', order.id)
				.then(response => (router.push(`/`)));
		},
		getUndeliveredOrders: function() {
			event.preventDefault();
      		router.push(`/undelivered_orders_customer`);
		},
		showFilters: function() {
			this.filters_show = !this.filters_show;
		},
		searchOrders: function() {

      if (this.startPrice == "") {
        this.startPrice = 0;
      }
      if (this.endPrice == "") {
        this.endPrice = 0;
      }

			let searchParams = {
				restaurantName: this.restaurantName,
				startPrice: this.startPrice,
				endPrice: this.endPrice,
				startDate: this.startDate,
				endDate: this.endDate,
				orders: this.orders
			}

			axios
				.post("/searchCustomerOrders", JSON.stringify(searchParams))
				.then(response => {
					this.orders = response.data;
					if (response.data.length == 0) {
						toast("Nema rezultata pretrage");
					}
				});
		},
		filterOrders: function() {

      if (document.getElementById('italian').checked == true) {
          this.type.push("italian");
        }
        if (document.getElementById('chinese').checked == true) {
          this.type.push("chinese");
        }
        if (document.getElementById('vegan').checked == true) {
          this.type.push("vegan");
        }
        if (document.getElementById('grill').checked == true) {
          this.type.push("grill");
        }
        if (document.getElementById('pizza').checked == true) {
          this.type.push("pizza");
        }
        if (document.getElementById('mexican').checked == true) {
          this.type.push("mexican");
        }

        for (let t of this.type) {
          if (document.getElementById(t).checked == false) {
            this.type = removeFromArray(this.type, t);
          }
        }

        console.log(this.type);

        if (document.getElementById('processing').checked == true) {
            this.status.push("processing");
          }
          if (document.getElementById('preparing').checked == true) {
            this.status.push("preparing");
          }
          if (document.getElementById('waiting').checked == true) {
            this.status.push("waiting");
          }
          if (document.getElementById('transport').checked == true) {
            this.status.push("transport");
          }
          if (document.getElementById('delivered').checked == true) {
            this.status.push("delivered");
          }
          if (document.getElementById('canceled').checked == true) {
            this.status.push("canceled");
          }

          for (let t of this.status) {
            if (document.getElementById(t).checked == false) {
              this.status = removeFromArray(this.status, t);
            }
          }

          console.log(this.status);

        let filterParams = {
          type: this.type,
          orderStatus: this.status,
          orders: this.orders
        }

        if (document.getElementById('italian').checked == false && document.getElementById('chinese').checked == false && document.getElementById('vegan').checked == false
            && document.getElementById('grill').checked == false && document.getElementById('pizza').checked == false && document.getElementById('mexican').checked == false
            && document.getElementById('processing').checked == false && document.getElementById('preparing').checked == false && document.getElementById('waiting').checked == false
                && document.getElementById('transport').checked == false && document.getElementById('delivered').checked == false && document.getElementById('canceled').checked == false) {
              axios
                .get('/rest/getCustomerOrders')
                .then(response => {
                  this.orders = response.data;
                });
        } else if (document.getElementById('italian').checked == false && document.getElementById('chinese').checked == false && document.getElementById('vegan').checked == false
            && document.getElementById('grill').checked == false && document.getElementById('pizza').checked == false && document.getElementById('mexican').checked == false) {
              axios
                .post("/filterOrdersByStatus", JSON.stringify(filterParams))
                .then(response => {
                  this.orders = response.data;
                  if (response.data.length == 0) {
                    toast("Nema rezultata pretrage");
                  }
                });
        } else if (document.getElementById('processing').checked == false && document.getElementById('preparing').checked == false && document.getElementById('waiting').checked == false
            && document.getElementById('transport').checked == false && document.getElementById('delivered').checked == false && document.getElementById('canceled').checked == false) {
              axios
                .post("/filterOrdersByType", JSON.stringify(filterParams))
                .then(response => {
                  this.orders = response.data;
                  if (response.data.length == 0) {
                    toast("Nema rezultata pretrage");
                  }
                });
        } else {
          axios
            .post("/filterOrders", JSON.stringify(filterParams))
            .then(response => {
              this.orders = response.data;
              if (response.data.length == 0) {
                toast("Nema rezultata pretrage");
              }
            })

        }

		},
		sortOrders: function() {

			if (this.sortBy == "nameAsc" || this.sortBy == "priceAsc" || this.sortBy == "dateAsc") {
				this.ascending = true;
			} else if (this.sortBy == "nameDesc" || this.sortBy == "priceDesc" || this.sortBy == "dateDesc") {
				this.ascending = false;
			}

			let sortParams = {
				orders: this.orders,
				ascending: this.ascending
			}

			if (this.sortBy == "nameAsc" || this.sortBy == "nameDesc") {
				axios
					.post("/sortOrdersByName", JSON.stringify(sortParams))
					.then(response => {
						this.orders = response.data;
					})
			} else if (this.sortBy == "priceAsc" || this.sortBy == "priceDesc") {
				axios
					.post("/sortOrdersByPrice", JSON.stringify(sortParams))
					.then(response => {
						this.orders = response.data;
					})
			} else if (this.sortBy == "dateAsc" || this.sortBy == "dateDesc") {
				axios
					.post("/sortOrdersByDate", JSON.stringify(sortParams))
					.then(response => {
						this.orders = response.data;
					})
			}
		}
	}
});
