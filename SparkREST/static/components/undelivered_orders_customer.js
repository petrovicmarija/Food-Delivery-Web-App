Vue.component("undelivered_orders_customer", {
	data: function() {
		return {
			orders: null
		}
	},
	template: `
		<div>
		<h1>Nedostavljene porudžbine</h1>
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
	    	</div>
		  </div>		  
		 </div>
	`,
	mounted() {
		axios
			.get('/rest/getCustomerUndeliveredOrders')
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
		}
	}
});