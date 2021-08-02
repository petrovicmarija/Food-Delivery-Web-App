function removeFromArray(arr, value) {
    return arr.filter(function(ele) {
      return ele != value;
    });
  }

Vue.component("manager_order_list", {
	data: function() {
		return {
			orders: null,
			filters_show: false,
			showSearch: false,
			ascending: false,
			startPrice: 0,
			endPrice: 0,
			startDate: "",
			endDate: "",
			sortBy: "",
			ascending: false,
      status: []
		}
	},
	template: `
		<div>
		<div class = "search-form">
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
				<option value="priceAsc"> Po ceni (rastuće) </option>
				<option value="priceDesc"> Po ceni (opadajuće) </option>
				<option value="dateAsc"> Po datumu (rastuće) </option>
				<option value="dateDesc"> Po datumu (opadajuće) </option>
			</select>

		</div>

		  <button style="position: absolute; top: 10px; right: 40px;" v-on:click="viewRequests">Zahtevi od dostavljača</button>
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
		      <h1 class="price"> {{o.status}} </h1>
		      <div v-for="(i, index) in o.orderedItems" >
		  		 <label>&nbsp; &nbsp; &nbsp; &nbsp;{{i.name}} x{{i.amount}}</label>
		  	  </div>
		    </div>
		    <div>
	    		</br></br></br>
	    		<p v-bind:hidden="isVisible(o)"> Promeni status porudžbine: </p>
	    		<button v-bind:hidden="isDisabledPrepare(o)" v-on:click="changeStatusToPreparing(o)"> U pripremi </button>
	    		<button v-bind:hidden="isDisabledWait(o)" v-on:click="changeStatusToWaiting(o)"> Čeka dostavljača </button>
	    	</div>
		  </div>
		 </div>
	`,
	mounted() {
		axios
			.get('/rest/getManagerOrders')
			.then(response => {
				this.orders = response.data;
			});

	},
	methods: {
		isDisabledPrepare: function(order) {
			if(order.status != 'processing') {
				return true;
			} else {
				return false;
			}
		},
		isDisabledWait: function(order) {
			if(order.status != 'preparing') {
				return true;
			} else {
				return false;
			}
		},
		isVisible: function(order) {
			if(order.status == 'processing' || order.status == 'preparing') {
				return false;
			} else {
				return true;
			}
		},
		changeStatusToPreparing: function(order) {
			axios
				.post('/rest/changeStatusToPreparing', order.id)
				.then(response => (router.push(`/`)));
		},
		changeStatusToWaiting: function(order) {
			axios
				.post('/rest/changeStatusToWaiting', order.id)
				.then(response => (router.push(`/`)));
		},
		viewRequests: function() {
			event.preventDefault();
			router.push(`/requests`);
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
				startPrice: this.startPrice,
				endPrice: this.endPrice,
				startDate: this.startDate,
				endDate: this.endDate,
				orders: this.orders
			}

			axios
				.post("/searchManagerOrders", JSON.stringify(searchParams))
				.then(response => {
					this.orders = response.data;
					if (response.data.length == 0) {
						toast("Nema rezultata pretrage");
					}
				});
		},
		filterOrders: function() {

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
          orderStatus: this.status,
          orders: this.orders
        }

        if (document.getElementById('processing').checked == false && document.getElementById('preparing').checked == false && document.getElementById('waiting').checked == false
            && document.getElementById('transport').checked == false && document.getElementById('delivered').checked == false && document.getElementById('canceled').checked == false) {
              axios
                .get('/rest/getManagerOrders')
                .then(response => {
                  this.orders = response.data;
                });
        } else {
          axios
            .post("/filterOrdersByStatus", JSON.stringify(filterParams))
            .then(response => {
              this.orders = response.data;
              if (response.data.length == 0) {
                toast("Nema rezultata pretrage");
              }
            })

        }

		},
		sortOrders: function() {
			if (this.sortBy == "priceAsc" || this.sortBy == "dateAsc") {
				this.ascending = true;
			} else if (this.sortBy == "priceDesc" || this.sortBy == "dateDesc") {
				this.ascending = false;
			}

			let sortParams = {
				orders: this.orders,
				ascending: this.ascending
			}

			if (this.sortBy == "priceAsc" || this.sortBy == "priceDesc") {
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
