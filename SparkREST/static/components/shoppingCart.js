Vue.component("shopping_cart", {
	data: function() {
		return {
			shoppingCart: null,
			user: null,
			items: null,
			error: ""
		}
	},
	template: `
	<div>
	  <div class="row-items" v-for="(i, index) in items">
	    <div class="col-with-pic"> </br>
	      <div class="col-picture">
	        <div>
	          <img :src="i.imagePath" class="restaurant-image" alt="i.name"> </br> </br>
	        </div>
	      </div>
	    </div>
	    <div class="col-information">
	      <h1 class="item-name"> {{i.name}}</h1>
	      <h1 class="description"> {{i.description}} </h1>
	      <h1 class="price"> {{i.price}},00 RSD </h1>
	    </div>
	    <div>
    		</br></br></br></br></br>
    		<span>
    			<button v-on:click="increment(i.name, i)" >+</button>
		    	<label v-bind:id="i.name">{{i.amount}}</label>
		    	<button v-on:click="decrement(i.name, i)">-</button>
		    			    	
    			<button class="delete-button" v-on:click="removeFromCart(i)"> Izbaci iz korpe </button>
    		</span>
    	</div>
	  </div>
	  <div class="row-items">
	  	<p v-model="shoppingCart.price">Iznos vaše porudžbine: {{shoppingCart.price}},00 RSD</p>
	  </div>
	  <button v-on:click="createOrder" style="position: absolute; right: 40px;">Poruči</button>
	 </div>
	`,
	mounted() {
		axios
			.get('/rest/isLogged')
			.then(response => {
				this.user = response.data;	
			});
			
		axios
			.get('/rest/getCustomer')
			.then(response => {
				if(response.data === "ERROR") {
					this.error = "no customer";
				} else {
					this.shoppingCart = response.data.shoppingCart;
					this.items = this.shoppingCart.items;
				}
			});
	},
	methods: {
		removeFromCart: function(i) {
			let itemParameters = {
				name : i.name,
				price : i.price,
				type : i.type,
				restaurant : i.restaurant,
				amount : i.amount,
				description : i.description,
				imagePath : i.imagePath
			};
		
			axios
				.delete('/rest/removeFromCart/' + i.name)
				.then(response => (this.$router.go()));	
		},
		
		increment: function(index, item) {
			var amount = 0;
			amount = parseFloat(document.getElementById(index).innerHTML);
			amount += 1;
			document.getElementById(index).innerHTML = amount;
			this.editShoppingCart(item, amount);
		},
		decrement: function(index, i) {
			var amount = 0;
			amount = parseFloat(document.getElementById(index).innerHTML);
			if(amount > 1) {
				amount -= 1;
				document.getElementById(index).innerHTML = amount;
				this.editShoppingCart(i, amount);
			}
		},
		
		editShoppingCart: function(item, amount) {
			let editParameters = {
				name : item.name,
				amount : amount
			};
			
			axios
				.put('/rest/editShoppingCart', JSON.stringify(editParameters))
				.then(response => {
					this.shoppingCart.price = response.data;
			});
		},
		createOrder: function() {
			axios
				.post('/rest/createOrder')
				.then(response => (router.push(`/`)));
		}
	}
});