const restaurants = { template : '<restaurants> </restaurants>' }
const login = { template : '<login> </login>' }
const registration = { template : '<registration> </registration>' }
const addEmployee = { template: '<addEmployee> </addEmployee>'}
const add_restaurant = { template: '<add_restaurant> </add_restaurant>' }
const userProfile = { template: '<profile></profile>' }
const restaurant_info = { template: '<restaurant_info> </restaurant_info>' }
const customers = { template: '<customers> </customers>'}
const managers = { template: '<managers> </managers>'}
const deliverers = { template: '<deliverers> </deliverers>' }
const add_item = { template: '<add_item> </add_item>' }
const search_restaurant = { template: '<search-restaurant> </search-restaurant>' }
const shoppingCart = { template: '<shopping_cart> </shopping_cart>' }
const add_manager = { template: '<add_manager> </add_manager>' }
const customer_orders = { template: '<customer_orders> </customer_orders>' }
const undelivered_orders_customer = { template: '<undelivered_orders_customer> </undelivered_orders_customer>' }
const manager_order_list = { template: '<manager_order_list> </manager_order_list>' }
const waiting_orders = { template: '<waiting_orders> </waiting_orders>' }
const requests = { template: '<requests> </requests>' }
const delivererOrders = { template: '<delivererOrders> </delivererOrders>' }
const delivererNotifications = { template: '<delivererNotifications> </delivererNotifications>' }
const suspiciousCustomers = { template: '<suspiciousCustomers></suspiciousCustomers>' }
const undeliveredOrdersDeliverer = { template: '<undeliveredOrdersDeliverer></undeliveredOrdersDeliverer>' }
const customersForManager = { template: '<customersForManager></customersForManager>' }
const addComment = { template: '<addComment> </addComment>'}
const allComments = { template: '<allComments> </allComments>' }


const router = new VueRouter({
	mode: 'hash',
		routes : [
			{ path: '/', name: 'home', component: restaurants},
			{ path: '/login', component: login},
			{ path: '/registration', component: registration},
			{ path: '/addEmployee', component: addEmployee},
			{ path: '/add_restaurant', component: add_restaurant },
			{ path: '/profile/:username', component: userProfile },
			{ path: '/details', component: restaurant_info },
			{ path: '/customers', component: customers},
			{ path: '/managers', component: managers },
			{ path: '/deliverers', component: deliverers },
			{ path: '/add_item', component: add_item},
			{ path: '/search_restaurant', component: search_restaurant },
			{ path: '/shoppingCart/:username', component: shoppingCart },
			{ path: "/search_restaurant", component: search_restaurant },
			{ path: "/shoppingCart/:username", component: shoppingCart },
		  { path: "/search_restaurant", component: search_restaurant },
			{ path: "/shoppingCart", component: shoppingCart },
			{ path: '/add_manager', component: add_manager },
			{ path: '/customer_orders', component: customer_orders },
			{ path: '/undelivered_orders_customer', component: undelivered_orders_customer },
			{ path: '/manager_order_list', component: manager_order_list },
			{ path: '/waiting_orders', component: waiting_orders },
			{ path: '/requests', component: requests },
			{ path: '/delivererOrders', component: delivererOrders },
			{ path: '/delivererNotifications', component: delivererNotifications },
			{ path: '/suspiciousCustomers', component: suspiciousCustomers },
			{ path: '/undeliveredOrdersDeliverer', component: undeliveredOrdersDeliverer },
			{ path: '/customersForManager', component: customersForManager },
			{ path: '/addComment', component: addComment },
			{ path: '/allComments', component: allComments }

		]
});

var app = new Vue({
	router,
	el: '#restaurants'
});
