Vue.component("addComment", {
  data: function() {
    return {
      grade: "",
      comment: "",
      user: null,
      restaurant: null,
      username: ""
    }
  },

  template: `
    <div>
      <form class="search-form">
        <h1> Restoran {{restaurant.name}} </h1>
        <label> Ocenite restoran (1-5) </label>
        <select v-model="grade" class="grade">
          <option value="1"> 1 </option>
          <option value="2"> 2 </option>
          <option value="3"> 3 </option>
          <option value="4"> 4 </option>
          <option value="5"> 5 </option>
        </select><br>
        <textarea class="comment" v-model="comment" placeholder="Ostavite komentar..."> </textarea>
        <button v-on:click="commentRestaurant"> Potvrdi </button>

      </form>
    </div>
  `,

  mounted() {

    axios
      .get('/rest/isLogged')
      .then(response => {
        if(response.data != null) {
          this.user = response.data;
          this.username = response.data.username;
        }
      });

    axios
      .get('/rest/customerOrders/' + this.$route.query.name)
      .then(response => {
        this.restaurant = response.data;
      });

  },

  methods: {
    commentRestaurant: function() {

      if (this.grade == '5') {
        this.grade = 5;
      } else if (this.grade == '4') {
        this.grade = 4;
      } else if (this.grade == '3') {
        this.grade = 3;
      } else if (this.grade == '2') {
        this.grade = 2;
      } else if (this.grade == '1') {
        this.grade = 1;
      }

      let commentParams = {
        customerUsername: this.username,
        restaurant: this.restaurant,
        text: this.comment,
        grade: this.grade
      }

      axios
        .post('/addComment', JSON.stringify(commentParams))
        .then(response => (router.push(`/`)));
    }
  }
});
