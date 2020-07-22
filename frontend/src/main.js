import Vue from 'vue';
import axios from 'axios';
import App from '@/App.vue';
import router from '@/router';
import store from '@/store';
import Paginate from 'vuejs-paginate';

import BootstrapVue from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';

Vue.use(BootstrapVue);

Vue.component('paginate', Paginate);

Vue.config.productionTip = false;

Vue.prototype.$axios = axios.create({
    baseURL: 'http://localhost:8080'
});

Vue.prototype.$axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (!error.response) {
            // Server do not response
            // vm.$router.push({
            //     name: 'ERROR',
            //     params: { error: 'error' },
            // });
        }

        return Promise.reject(error);
    },
);

const vm = new Vue({
    router,
    store,
    render: (h) => h(App),
}).$mount('#app');
