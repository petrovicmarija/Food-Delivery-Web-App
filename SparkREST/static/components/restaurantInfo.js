Vue.component("restaurant_info", {

  data: function() {
    return {
      restaurantType: '',
      restaurantImage: "",
      restaurant: null,
      address: "",
      city: "",
      state: "",
      restaurantName: "",
	    name: '',
	    mode: "notLogged",
      items: null,
      comments: null,
      avgGrade: "",
      selectedItem: null,
      amount: 0,
      errorMessage: "",
      emptyCartMessage: "",
      canEdit: false,
      managerRestaurant: null,
      editMode: false,
      itemName: "",
      itemType: "",
      itemPrice: "",
      restaurantName: "",
      itemAmount: "",
      itemDescription: "",
      itemImage: "",
      itemImageForBackend: "",
      oldItem: null,
      open: false,
      approvedComments: null,
      commentMessage: ""
    }
  },

  template: `

  <div>
    <img :src="restaurantImage" class="rest-image">
	<button v-if="mode=='customer'" style="position: absolute; top: 10px; right: 40px;" v-on:click="viewShoppingCart">Korpa</button>
	<p style="color:red;text-transform:none;">{{emptyCartMessage}}</p>
    <div class="all">
    <div class="wrapper-restaurant">
      <div class="left">
        <h4> {{restaurant.name}} </h4>
        <p> Ocena: {{avgGrade}} </p>
      </div>

      <div class ="right">
        <div class="info">
          <h3> Informacije </h3>
          <div class="info_data">
            <div class="data">
              <h4> Adresa: </h4>
              <p> {{restaurant.location.address.address + ", " + restaurant.location.address.city.city}} </p>
            </div>
            <div class="data">
              <h4> Tip restorana: </h4>
              <p> {{restaurant.type}} </p>
            </div>
            <div class="data">
              <h4> Status: </h4>
              <p> {{isOpen()}} </p>
            </div>
            </div>
        </div>
      </div>
    </div>
  </div>

  </br> </br> </br> </br> </br> </br> </br> </br> </br>

  <div v-bind:hidden="editMode==true">
    <div class="row-items" v-for="(i, index) in items">
      <div class="col-with-pic"> </br>
        <div class="col-picture">
          <div>
            <img :src="i.imagePath" class="restaurant-image" alt="i.name"> </br> </br>
          </div>
        </div>
      </div>

      <div class="col-information">
        <h1 class="item-name"> {{i.name}} </h1>
        <h1 class="description"> {{i.description}} </h1>
        <h1 class="price"> {{i.price}},00 RSD </h1>
      </div>
      <div>
      	</br></br></br></br></br>
      	<span>
  	    	<button v-if="mode=='customer' && open==true" v-on:click="increment(i.name)">+</button>
  	    	<label v-bind:id="i.name" v-if="mode=='customer' && open==true">0</label>
  	    	<button v-if="mode=='customer' && open==true" v-on:click="decrement(i.name)">-</button>
  	    	<button v-if="mode=='customer' && open==true" class="see-more" v-on:click="addToCart(i, i.name)"> Dodaj u korpu </button>
          <button v-if="mode=='manager' && canEdit==true" class="see-more" v-on:click="changeMode(i)"> Izmeni artikal </button>
  	    	<p style="color:red;text-transform:none;">{{errorMessage}}</p>
      	</span>
      </div>
    </div>
  </div>

  <div v-bind:hidden="editMode==false">
  <form class="add-form">
    <label> Naziv artikla: </label>
    <input type="text" v-model="itemName" name="name"/>
    <p style="color: red;"> {{errorName}} </p>

    <label> Tip artikla: </label>
    <select v-model="itemType">
      <option value="food"> Food </option>
      <option value="drink"> Drink </option>
    </select>
    <p style="color: red;"> {{errorType}} </p>

    <label> Cena: </label>
    <input type="number" v-model="itemPrice"/>
    <p style="color: red;"> {{errorPrice}} </p>

    <label> Restoran: </label>
    <select id="selectRestaurant" v-model="restaurantName">
      <option value="" disabled selected> </option>
      <option :value="restaurant.name"> {{restaurant.name}} </option>
    </select>
    <p style="color: red;"> {{errorRestaurant}} </p>

    <label> Količina: </label>
    <input type="number" v-model="itemAmount"/>
    <p style="color: red;"> {{errorAmount}} </p>

    <label> Opis: </label>
    <input type="text" v-model="itemDescription"/> </br>
    <p style="color: red;"> {{errorDescription}} </p>

    <div class="item-picture">
      <div>
        <img :src="itemImage" class="item-image" alt="Item Image"> </br>
        <input type="button" id="loadFileXml" class="UploadItemImage" value="Dodaj sliku artikla" onclick="document.getElementById('file').click();" />
        <input type="file" style="display: none; border: none;" @change="imageAdded" id="file" name="file"/>
      </div>
    </div>

    <input type="submit" v-on:click="editItem" value="Izmeni artikal"/>
  </form>
  </div>
  <div class="comments">
    <h2 class="comments-header"> Comments </h2>
  </div>
  <div class="row-items" v-for="(c, index) in approvedComments">
    <div class="col-comments">
      <h1 class="customer-ns"> {{c.customer.name + " " +c.customer.surname}}</h1>
      <h1 class="text-comment"> {{c.text}} </h1>
      <h1 class="grade-comment"> {{c.grade}}</h1>
      <h1 class="item-name"> {{commentMessage}} </h1>
    </div>
  </div>

  </div>
  `,

  mounted() {

    axios
      .get('/rest/restaurants/' + this.$route.query.name)
      .then(response => {
        this.restaurant = response.data;
		    this.name = response.data.name;
        this.restaurantImage = response.data.imgPath;
        this.items = response.data.items;
        axios
          .get('/rest/isLogged')
          .then(response => {
            if(response.data != null) {
              this.mode = response.data.role;
              this.user = response.data;
              axios
                .get('/rest/getManagerRestaurant/' + this.user.username)
                .then(response => {
                  this.managerRestaurant = response.data;
                  if (this.restaurant.name == this.managerRestaurant.name) {
                    this.canEdit = true;
                  } else {
                    this.canEdit = false;
                  }
                });
            } else {
              this.mode = "notLogged";
            }
          });
          axios
            .get('/getRestaurantComments/' + this.name)
            .then(response => {
              if (response.data != null) {
                this.approvedComments = response.data;
              } else {
                this.commentMessage = "Nema komentara.";
              }
            });
      });

    axios
      .get('/rest/getComments')
      .then(response => {
        this.comments = response.data;
        let cnt = 0;
        let avg = 0;
        let sum = 0;
        for (let comment of this.comments) {
          if (comment.restaurant.name == this.name) {
            sum += comment.grade;
            cnt ++;
          }
        }
        avg = sum/cnt;
        this.avgGrade = avg;
      });

  },

  methods: {

    imageAdded(e) {
      const file = e.target.files[0];
      this.createBase64Image(file);
      this.itemImage = URL.createObjectURL(file);
    },

    createBase64Image(file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        let img = e.target.result;
        console.log(img);
        this.itemImageForBackend = img;
      }
      reader.readAsDataURL(file);
    },

		addToCart: function(item, index) {
			var amountInput = document.getElementById(index).innerHTML;
			if(amountInput < 1) {
				this.errorMessage = "Unesite željnu količinu!";
			} else {
				let itemParameters = {
					name : item.name,
					price : item.price,
					type : item.type,
					restaurant : item.restaurant,
					amount : amountInput,
					description : item.description,
					imagePath : item.imagePath
				};
				axios
					.post('/rest/addToCart', JSON.stringify(itemParameters));
				document.getElementById(index).innerHTML = '0';

				this.errorMessage = "Artikal dodat u korpu!";
			}
		},
		increment: function(index) {
			var amount = 0;
			amount = parseFloat(document.getElementById(index).innerHTML);
			amount += 1;
			document.getElementById(index).innerHTML = amount;
		},
		decrement: function(index) {
			var amount = 0;
			amount = parseFloat(document.getElementById(index).innerHTML);
			if(amount > 0) {
				amount -= 1;
				document.getElementById(index).innerHTML = amount;
			}
		},
		viewShoppingCart: function() {
			axios
				.get('/rest/isCartEmpty')
				.then( response => {
					if(response.data == "YES") {
						this.emptyCartMessage = "Korpa je prazna!"
					} else {
						router.push(`/shoppingCart/` + this.user.username);
					}
				});
		},

    changeMode: function(item) {
      this.editMode = true;
      this.itemName = item.name;
      this.itemPrice = item.price;
      this.itemType = item.type;
      this.restaurantName = item.restaurant.name;
      this.itemAmount = item.amount;
      this.itemDescription = item.description;
      this.oldItem = item;
    },

    editItem: function() {
      event.preventDefault();

      let restaurantCurr = {
        name: this.restaurantName
      };

      let itemParams = {
        name: this.itemName,
        price: this.itemPrice,
        type: this.itemType,
        restaurant: restaurantCurr,
        amount: this.itemAmount,
        description: this.itemDescription,
        imagePath: this.itemImage,
        oldItem: this.oldItem
      };

      axios
        .put('/rest/editItem', JSON.stringify(itemParams));

      this.editMode = false;
    },

    isOpen: function() {
      var today = new Date();
      var hours = today.getHours();

      if (hours >= 8 && hours <= 19) {
        this.open = true;
        return "open";
      } else {
        this.open = true;
        return "closed";
      }

      console.log(this.open);
    }
  }

});
