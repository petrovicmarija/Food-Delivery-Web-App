Vue.component("add_item", {
  data: function() {
    return {
      itemName: '',
      itemPrice: "",
      itemType: '',
      itemAmount: "",
      itemDescription: '',
      imagePath: '',
      errorName: '',
      errorPrice: '',
      errorType: '',
      errorRestaurant: '',
      errorAmount: '',
      errorDescription: '',
      itemImage: "",
      itemImageForBackend: "",
      restaurantName: '',
      formErrorMessage: "",
      mode: "",
      user: null,
      username: "",
      restaurant: null ,
      managerRestaurant: ""
    }
  },

  template: `

  <div>
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

      <input type="submit" v-on:click="addItem" value="Dodaj artikal"/>
    </form>
  </div>

  `,

  mounted() {

    axios
      .get('/rest/isLogged')
      .then(response => {
        if(response.data != null) {
          this.mode = response.data.role;
          this.user = response.data;
          this.username = response.data.username;
          axios
            .get('/rest/getManagerRestaurant/' + this.username)
            .then(response => {
              this.restaurant = response.data;
            });
        } else {
          this.mode = "notLogged";
        }
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


    checkValidForm: function() {
      if (this.itemName == '') {
        this.errorName = "Naziv artikla je obavezno polje!";
        return false;
      }
      if (this.itemType == '') {
        this.errorType = "Tip artikla je obavezno uneti!";
        return false;
      }
      if (this.itemPrice == '') {
        this.errorPrice = "Cena artikla je obavezno polje!";
        return false;
      }
      if (this.itemAmount == '') {
        this.errorAmount = "Količina artikla je obavezno polje!";
        return false;
      }
      if (this.itemDescription == '') {
        this.errorDescription = "Opis je obavezno uneti!";
        return false;
      }

      return true;
    },

    addItem: function() {
      event.preventDefault();

      if (this.checkValidForm()) {

        if (this.itemType === 'food') {
          this.itemType = "food";
        } else {
          this.itemType = "drink";
        }

        let restaurantCurr = {
          name: this.restaurantName
        };

        let itemParameters = {
          name: this.itemName,
          price: this.itemPrice,
          type: this.itemType,
          restaurant: restaurantCurr,
          amount: this.itemAmount,
          description: this.itemDescription,
          imagePath: this.itemImage
        };

        axios
          .post('/rest/addItem', JSON.stringify(itemParameters))
          .then(response => (router.push(`/`)));
      }

    }
  }

});
