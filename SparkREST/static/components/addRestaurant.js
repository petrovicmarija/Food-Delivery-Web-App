function cyrilicToLatinic(string) {
    var cyrillic = 'А_Б_В_Г_Д_Ђ_Е_Ё_Ж_З_И_Й_Ј_К_Л_Љ_М_Н_Њ_О_П_Р_С_Т_Ћ_У_Ф_Х_Ц_Ч_Џ_Ш_Щ_Ъ_Ы_Ь_Э_Ю_Я_а_б_в_г_д_ђ_е_ё_ж_з_и_й_ј_к_л_љ_м_н_њ_о_п_р_с_т_ћ_у_ф_х_ц_ч_џ_ш_щ_ъ_ы_ь_э_ю_я'.split('_')
    var latin = 'A_B_V_G_D_Đ_E_Ë_Ž_Z_I_J_J_K_L_Lj_M_N_Nj_O_P_R_S_T_Ć_U_F_H_C_Č_Dž_Š_Ŝ_ʺ_Y_ʹ_È_Û_Â_a_b_v_g_d_đ_e_ë_ž_z_i_j_j_k_l_lj_m_n_nj_o_p_r_s_t_ć_u_f_h_c_č_dž_š_ŝ_ʺ_y_ʹ_è_û_â'.split('_')

    return string.split('').map(function(char) {
      var index = cyrillic.indexOf(char)
      if (!~index)
        return char
      return latin[index]
    }).join('')
  }

Vue.component("add_restaurant", {
  data: function() {
    return {
      restaurantName: '',
      restaurantType: '',
      restaurantStatus: '',
      items: null,
      country: "",
      state: "",
      city: "",
      zipcode: "",
      longitude: "",
      latitude: "",
      street: "",
      number: "",
      address: "",
	    imagePath: "",
      places: null,
      errorName: "",
      errorStatus: "",
      errorType: "",
      errorAddress: "",
      formErrorMessage: "",
      restaurantImage: "",
      restImageForBackend: "",
      managers: [],
      restaurantManager: ""
    }
  },

  template: `
  <div>
    <form class="add-form">
      <label> Naziv restorana: </label>
      <input type="text" v-model="restaurantName" name="name" v-on:change="signalChange"/>
      <p style="color: red;"> {{errorName}} </p>

      <label> Tip restorana: </label>
      <select v-model="restaurantType" v-on:change="signalChange">
        <option value="italian"> Italian </option>
        <option value="chinese"> Chinese </option>
        <option value="vegan"> Vegan </option>
        <option value="asian"> Asian </option>
        <option value="mexican"> Mexican </option>
        <option value="grill"> Grill </option>
        <option value="vegetarian"> Vegetarian </option>
        <option value="pizza"> Pizza </option>
        <option value="fastfood"> Fast food </option>
      </select>
      <p style="color: red;"> {{errorType}} </p>

      <label> Status: </label>
      <select v-model="restaurantStatus" v-on:change="signalChange">
        <option value="open"> Open </option>
        <option value="closed"> Closed </option>
      </select>
      <p style="color: red;"> {{errorType}} </p>

      <label> Adresa: </label>
      <input type="search" id="address" v-on:change="signalChange"/>
      <p style="color: red;"> {{errorAddress}} </p>

      <div class="map-div">
        <div id="map"> </div>
      </div>

      <label> Grad: </label>
      <input type="text" id="city" class="input-apt" disabled/>

      <label> Država: </label>
      <input type="text" id="country" class="input-apt" disabled/> </br>

      <label> Menadžer: </label>
      <div class="add-manager">

        <div class="manager-cb">

        <select  id="selectManager" v-model="restaurantManager">
          <option value="" disabled selected> </option>
          <option v-for="(m, index) in managers" :value="m.username"> {{m.name + " " + m.surname}} </option>
        </select>
        </div>
        <div class="manager-btn">
          <input type="submit" value="Dodaj menadžera" v-on:click="newManager"/> </br>
        </div>
      </div> </br> </br> </br>

      <div >
        <div class="restaurant-picture">
          <img :src="restaurantImage" class="restaurant-image" alt="Restaurant Image">
        </div> </br>
        <div>
          <input type="button" id="loadFileXml" class="UploadRestaurantImage" value="Dodaj sliku restorana" onclick="document.getElementById('file').click();" />
          <input type="file" style="display: none; border: none;" @change="imageAdded" id="file" name="file"/>

        </div>

      </div>

      <input type="submit" v-on:click="addRestaurant" value="Dodaj restoran"/>
    </form>

	<input class="hidden" id="latitude" hidden/>
    <input class="hidden" id="longitude" hidden/>
    <input class="hidden" id="zipcode" hidden/>


  </div>
  `,

  mounted() {
    
    axios
      .get('/rest/getAvailableManagers')
      .then(response => {
        this.managers = response.data;
      });

      const map = new ol.Map({ target: "map" });

      map.setView(
        new ol.View({
          center: ol.proj.fromLonLat([19.7245,45.2889]),
          zoom: 12
        })
      );

      const apiKey = "AAPK8cad6fdb843d461b83eff8dfd08b20dcBLf02R4yV5jXllwflSmWOdjsqUK-9v-etIhDJdP7et4xmJgOD9xBn2FDg-DOwbvs";
      const basemapId = "ArcGIS:Navigation";
      const basemapURL = "https://basemaps-api.arcgis.com/arcgis/rest/services/styles/" + basemapId + "?type=style&token=" + apiKey;

      olms(map, basemapURL);

      const popup = new Popup();
      map.addOverlay(popup);

      map.on("click", (e) => {
        const coords = ol.proj.transform(e.coordinate, "EPSG:3857", "EPSG:4326");

        const authentication = new arcgisRest.ApiKey({
          key: apiKey
        });

      arcgisRest
      .reverseGeocode(coords, { authentication })
      .then((result) => {
        const message = `${result.address.LongLabel}<br>` + `${result.location.x.toLocaleString()}, ${result.location.y.toLocaleString()}`;
        document.querySelector('#address').value = cyrilicToLatinic(result.address.Address);
        document.querySelector('#country').value = cyrilicToLatinic(result.address.CountryCode);
        document.querySelector('#city').value = cyrilicToLatinic(result.address.City);
        document.querySelector('#longitude').value = result.location.x.toLocaleString();
        document.querySelector('#latitude').value = result.location.y.toLocaleString();
        document.querySelector('#zipcode').value = result.address.Postal;
        popup.show(e.coordinate, message);

      })

      .catch((error) => {
        popup.hide();
        console.error(error);
      })
    });

  },

  methods: {

    imageAdded(e) {
      const file = e.target.files[0];
      this.createBase64Image(file);
      this.restaurantImage = URL.createObjectURL(file);
    },

    createBase64Image(file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        let img = e.target.result;
        console.log(img);
        this.restImageForBackend = img;
      }
      reader.readAsDataURL(file);
    },

    checkValidForm: function() {
      this.address = document.querySelector('#address').value;
      this.country = document.querySelector('#country').value;
      this.state = document.querySelector('#country').value;
      this.zipcode = document.querySelector('#zipcode').value;
      this.longitude = document.querySelector('#longitude').value;
      this.latitude = document.querySelector('#latitude').value;
      this.city = document.querySelector('#city').value;

      let flag = true;

      if (this.restaurantName == '') {
        this.errorName = "Ime restorana je obavezno polje!";
        return false;
      }
      if (this.restaurantType == '') {
        this.errorType = "Tip restorana je obavezno polje!";
        return false;
      }
      if (this.RestaurantStatus == '') {
        this.errorStatus = "Status restorana je obavezno polje!";
        return false;
      }
      if (this.address == '') {
        this.errorAddress = "Adresa je obavezno polje!";
        return false;
      }

      return true;
    },

    signalChange: function() {
      this.formErrorMessage='';
    },

    newManager: function() {
      event.preventDefault();
      router.push(`/add_manager`);
    },

    addRestaurant: function() {
      event.preventDefault();

      if (this.checkValidForm()) {
        this.address = cyrilicToLatinic(document.querySelector('#address').value);
        this.country = cyrilicToLatinic(document.querySelector('#country').value);
        this.state = cyrilicToLatinic(document.querySelector('#country').value);
        this.zipcode = cyrilicToLatinic(document.querySelector('#zipcode').value);
        this.longitude = cyrilicToLatinic(document.querySelector('#longitude').value);
        this.latitude = cyrilicToLatinic(document.querySelector('#latitude').value);

        if (!cyrilicToLatinic(document.querySelector('#city').value)) {
          this.city = "  ";
        } else {
          this.city = cyrilicToLatinic(document.querySelector('#city').value);
        }

        if (this.zipcode == "") {
          this.zipcode = 0;
        }

        let stateCurr = {
          state: this.state
        };

        let cityCurr = {
          state: stateCurr,
          city: this.city,
          zipcode: this.zipcode,
        };

        let addressCurr = {
          city: cityCurr,
          address: this.address
        };

        let locationCurr = {
          latitude: this.latitude,
          longitude: this.longitude,
          address: addressCurr
        };

        if (this.restaurantType === 'chinese') {
          this.restaurantType = "chinese";
        } else if (this.restaurantType === 'mexican') {
          this.restaurantType = "mexican";
        } else if (this.restaurantType === 'pizza') {
          this.restaurantType = "pizza";
        } else if (this.restaurantType === 'asian') {
          this.restaurantType = "asian";
        } else if (this.restaurantType === 'italian') {
          this.restaurantType = "italian";
        } else if (this.restaurantType === 'vegan') {
          this.restaurantType = "vegan";
        } else if (this.restaurantType === 'vegetarian') {
          this.restaurantType = "vegetarian";
        } else if (this.restaurantType === 'grill') {
          this.restaurantType = "grill";
        } else {
          this.restaurantType = "american";
        }

        if (this.restaurantStatus === 'open') {
          this.restaurantStatus = "open";
        } else {
          this.restaurantStatus = "closed";
        }

        let restaurantParameters = {
          name: this.restaurantName,
          type: this.restaurantType,
          items: this.items,
  		    status: this.restaurantStatus,
          location: locationCurr,
  		    imgPath: this.restaurantImage
        };

        let managerParams = {
          username: this.restaurantManager,
          restaurant: restaurantParameters
        }

        if (this.restaurantManager == '') {
          axios
            .post('/rest/addRestaurant', JSON.stringify(restaurantParameters))
            .then(response => (router.push(`/`)));
        } else {
          axios
            .post('/rest/addManagerToRestaurant', JSON.stringify(managerParams))
            .then(response => (router.push(`/`)));

          axios
            .post('/rest/addRestaurant', JSON.stringify(restaurantParameters))
            .then(response => (router.push(`/`)));
        }
      }
    }
  }
});
