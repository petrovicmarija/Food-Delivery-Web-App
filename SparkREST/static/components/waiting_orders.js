Vue.component("waiting_orders", {
	data: function() {
		return {
			orders: null,
			error: ""
		}
	},
	template: `
		<div>
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
		      <h1 class="price"> {{o.price}},00 RSD</h1>
		      <h1 class="price"> {{o.status}} </h1>
		      <div v-for="(i, index) in o.orderedItems" >
		  		 <label>&nbsp; &nbsp; &nbsp; &nbsp;{{i.name}} x{{i.amount}}</label>
		  	  </div>
		    </div>
		    <div>
	    		</br></br></br></br></br>
	    		<button v-bind:hidden="isDisabledRequest(o)" v-on:click="sendRequest(o)"> Pošalji zahtev za isporuku </button>
	    	</div>
		  </div>		  
		 </div>
	`,
	mounted() {
		axios
			.get('/rest/getWaitingOrders')
			.then(response => {
				this.orders = response.data;
			});
		
	},
	methods: {
		isVisible: function(order) {
			if(order.status == 'waiting') {
				return false;
			} else {
				return true;
			}
		},
		isDisabledRequest: function(order) {
			if(order.status != 'waiting') {
				return true;
			} else {
				return false;
			}
		},
		sendRequest: function(order) {
			axios
				.post('/rest/newRequest', order)
				.then(response => 
				{
					if(response.data == 'OK') {
						router.push(`/`);
					} else {
						this.error = "Već ste poslali zahtev";
					}
				});
		}
	}
});