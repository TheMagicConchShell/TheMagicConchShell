import Vue from 'vue';
import axios from 'axios';
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import InfiniteLoading from 'vue-infinite-loading';
// import SidebarModal from 'vue-sidebar-modal';
import App from './App.vue';
import router from './router';
import store from './store';

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import './assets/css/common.css';

Vue.config.productionTip = false;

Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.use(InfiniteLoading);
// Vue.use(SidebarModal);

const vm = new Vue({
    router,
    store,
    render: (h) => h(App),
}).$mount('#app');

axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (!error.response) {
            // Server do not response
            vm.$router.push({
                name: 'ERROR',
                params: { error: 'error' },
            });
        }

        return Promise.reject(error);
    },
);

Vue.prototype.$axios = axios;
