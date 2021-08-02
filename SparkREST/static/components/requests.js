Vue.component("requests", {
	data: function() {
		return {
			requests: null,
			empty: ""
		}
	},
	template: `
		<div>
		  <div class="row-items" v-for="(r, index) in requests">
		    <div class="col-information">
		      <h1 class="item-name"> {{r.restaurantName}}</h1>
		      <h1 class="price"> {{r.orderPrice}},00 RSD </h1>
		    </div>
		    <div>
	    		</br></br></br>
	    		<h1 class="info">Dostavljač koji je poslao zahtev: {{r.deliverer}} </h1>
	    		<button v-on:click="acceptRequest(r)"> Prihvati zahtev </button>
	    		<button v-on:click="rejectRequest(r)"> Odbij zahtev </button>
	    	</div>
		  </div>
		  <div>
		  	<div class="row-items">
			    <div class="col-information">
			      <h1 class="item-name"> {{this.empty}}</h1>
			    </div>
		    </div>
		  </div>		  
		 </div>
	`,
	mounted() {
		axios
			.get('/rest/getManagerRequests')
			.then(response => {
				if(response.data == 'Empty request list') {
					this.empty = "Nemate novih zahteva";
				} else {
					this.requests = response.data;
				}
			});
	},
	methods: {
		acceptRequest: function(requestDTO) {
			axios
				.put('/rest/acceptRequest', requestDTO)
				.then(response => (router.push(`/`)));
		},
		rejectRequest: function(requestDTO) {
			axios
				.put('/rest/rejectRequest', requestDTO)
				.then(response => (router.push(`/`)));
		}
	}
});