Vue.component("allComments", {
  data: function() {
    return {
      comments: null,
      mode: ""
    }
  },

  template: `
    <div>

    <div class="row-items" v-for="(c, index) in comments">
      <div class="col-comments">
        <h1 class="customer-ns"> {{c.customer.username}}</h1>
        <h1 class="text-comment" v-if="mode=='admin'"> {{c.restaurant.name}} </h1>
        <h1 class="text-comment"> {{c.text}}</h1>
        <h1 class="grade-comment"> {{c.grade}} </h1>
        <button v-if="mode=='manager'" v-on:click="approveComment(c)"> Odobri komentar </button>
      </div>
    </div>

    </div>
  `,

  mounted() {

    axios
      .get('/rest/isLogged')
      .then(response => {
        if (response.data != null) {
          this.mode = response.data.role;
        } else {
          this.mode = "notLogged";
        }
        if (response.data.role == 'admin') {
          axios
            .get('/getCommentsForAdmin')
            .then(response => {
              this.comments = response.data;
            })
        } else if (response.data.role == 'manager') {
          axios
            .get('/getAllComments')
            .then(response => {
              this.comments = response.data;
            });
        }
      });


  },

  methods: {
    approveComment: function(comment) {
      axios
        .post('/approveComment', comment)
        .then(response => (router.push(`/`)));
    }
  }
});
