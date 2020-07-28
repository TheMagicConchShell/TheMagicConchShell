import Vue from 'vue';
import axios from 'axios';
import App from '@/App.vue';
import router from '@/router';
import store from '@/store';

import Paginate from 'vuejs-paginate';

import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import InfiniteLoading from 'vue-infinite-loading';
import VueCarousel from 'vue-carousel';

Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.use(InfiniteLoading);
Vue.use(VueCarousel);

Vue.component('paginate', Paginate);

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import './assets/css/common.css';

Vue.config.productionTip = false;

Vue.prototype.$axios = axios.create({
    // baseURL: 'http://localhost:8080'
    baseURL: 'http://i3a403.p.ssafy.io:8399',
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

        else {
            let message = error.response.data.message;
            let title = `Error ${error.response.status} (${error.response.data.status})`;
            Vue.prototype.$toast(title, message);
        }

        return Promise.reject(error);
    },
);

const vm = new Vue({
    router,
    store,
    render: (h) => h(App),
}).$mount('#app');

Vue.prototype.$toast = (title, message, append = false, hideDelay = 5000) => {
    vm.$bvToast.toast(`${message}`, {
        title: title,
        toaster: 'b-toaster-top-center',
        autoHideDelay: hideDelay,
        appendToast: append,
    });
};