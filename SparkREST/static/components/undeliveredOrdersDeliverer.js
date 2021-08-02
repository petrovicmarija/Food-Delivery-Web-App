Vue.component("undeliveredOrdersDeliverer", {
	data: function() {
		return {
			orders: null
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
		      <h1 class="price"> {{o.price}},00 RSD </h1>
		      <h1 class="price"> {{o.status}} </h1>
		      <div v-for="(i, index) in o.orderedItems" >
		  		 <label>&nbsp; &nbsp; &nbsp; &nbsp;{{i.name}} x{{i.amount}}</label>
		  	  </div>
		    </div>
		    <div>
	    		</br></br></br>
	    		<p v-bind:hidden="isVisible(o)"> Promeni status porudžbine: </p>
	    		<button v-bind:hidden="isDisabledDelivered(o)" v-on:click="changeStatusToDelivered(o)"> Isporučeno </button>
	    	</div>
		  </div>		  
		 </div>
	`,
	mounted() {
		axios
			.get('/rest/getUndeliveredOrdersDeliverer')
			.then(response => {
				this.orders = response.data;
			});
	},
	methods: {
		isDisabledDelivered: function(order) {
			if(order.status == 'delivered') {
				return true;
			} else {
				return false;
			}
		},
		changeStatusToDelivered: function(order) {
			axios
				.post('/rest/changeStatusToDelivered', order)
				.then(response => (router.push(`/`)));
		},
		isVisible: function(order) {
			if(order.status == 'transport') {
				return false;
			} else {
				return true;
			}
		}
	}
});